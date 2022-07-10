/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022 architectury
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package dev.architectury.networking.transformers;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;

@ApiStatus.Experimental
public class SplitPacketTransformer implements PacketTransformer {
    private static final Logger LOGGER = LogManager.getLogger(SplitPacketTransformer.class);
    private static final byte START = 0x0;
    private static final byte PART = 0x1;
    private static final byte END = 0x2;
    private static final byte ONLY = 0x3;
    
    private static class PartKey {
        private final NetworkManager.Side side;
        @Nullable
        private final UUID playerUUID;
        
        public PartKey(NetworkManager.Side side, @Nullable UUID playerUUID) {
            this.side = side;
            this.playerUUID = playerUUID;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PartKey)) return false;
            PartKey key = (PartKey) o;
            return side == key.side && Objects.equals(playerUUID, key.playerUUID);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(side, playerUUID);
        }
        
        @Override
        public String toString() {
            return "PartKey{" +
                    "side=" + side +
                    ", playerUUID=" + playerUUID +
                    '}';
        }
    }
    
    private static class PartData {
        private final ResourceLocation id;
        private final int partsExpected;
        private final List<FriendlyByteBuf> parts;
        
        public PartData(ResourceLocation id, int partsExpected) {
            this.id = id;
            this.partsExpected = partsExpected;
            this.parts = new ArrayList<>();
        }
    }
    
    private final Map<PartKey, PartData> cache = Collections.synchronizedMap(new HashMap<>());
    
    public SplitPacketTransformer() {
        PlayerEvent.PLAYER_QUIT.register(player -> {
            cache.keySet().removeIf(key -> Objects.equals(key.playerUUID, player.getUUID()));
        });
        EnvExecutor.runInEnv(Env.CLIENT, () -> new Client()::init);
    }
    
    private class Client {
        @Environment(EnvType.CLIENT)
        private void init() {
            ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(player -> {
                cache.keySet().removeIf(key -> key.side == NetworkManager.Side.S2C);
            });
        }
    }
    
    @Override
    public void inbound(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, NetworkManager.PacketContext context, TransformationSink sink) {
        PartKey key = side == NetworkManager.Side.S2C ? new PartKey(side, null) : new PartKey(side, context.getPlayer().getUUID());
        PartData data;
        switch (buf.readByte()) {
            case START:
                data = new PartData(id, buf.readInt());
                if (cache.put(key, data) != null) {
                    LOGGER.warn("Received invalid START packet for SplitPacketTransformer with packet id " + id + " for side " + side);
                }
                buf.retain();
                data.parts.add(buf);
                break;
            case PART:
                if ((data = cache.get(key)) == null) {
                    LOGGER.warn("Received invalid PART packet for SplitPacketTransformer with packet id " + id + " for side " + side);
                    buf.release();
                } else if (!data.id.equals(id)) {
                    LOGGER.warn("Received invalid PART packet for SplitPacketTransformer with packet id " + id + " for side " + side + ", id in cache is " + data.id);
                    buf.release();
                    for (FriendlyByteBuf part : data.parts) {
                        if (part != buf) {
                            part.release();
                        }
                    }
                    cache.remove(key);
                } else {
                    buf.retain();
                    data.parts.add(buf);
                }
                break;
            case END:
                if ((data = cache.get(key)) == null) {
                    LOGGER.warn("Received invalid END packet for SplitPacketTransformer with packet id " + id + " for side " + side);
                    buf.release();
                } else if (!data.id.equals(id)) {
                    LOGGER.warn("Received invalid END packet for SplitPacketTransformer with packet id " + id + " for side " + side + ", id in cache is " + data.id);
                    buf.release();
                    for (FriendlyByteBuf part : data.parts) {
                        if (part != buf) {
                            part.release();
                        }
                    }
                    cache.remove(key);
                } else {
                    buf.retain();
                    data.parts.add(buf);
                }
                if (data.parts.size() != data.partsExpected) {
                    LOGGER.warn("Received invalid END packet for SplitPacketTransformer with packet id " + id + " for side " + side + " with size " + data.parts + ", parts expected is " + data.partsExpected);
                    for (FriendlyByteBuf part : data.parts) {
                        if (part != buf) {
                            part.release();
                        }
                    }
                } else {
                    FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.wrappedBuffer(data.parts.toArray(new ByteBuf[0])));
                    sink.accept(side, data.id, byteBuf);
                    byteBuf.release();
                }
                cache.remove(key);
                break;
            case ONLY:
                sink.accept(side, id, buf);
                break;
            default:
                throw new IllegalStateException("Illegal split packet header!");
        }
    }
    
    @Override
    public void outbound(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, TransformationSink sink) {
        int maxSize = (side == NetworkManager.Side.C2S ? 32767 : 1048576) - 1 - 20 - id.toString().getBytes(StandardCharsets.UTF_8).length;
        if (buf.readableBytes() <= maxSize) {
            ByteBuf stateBuf = Unpooled.buffer(1);
            stateBuf.writeByte(ONLY);
            FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.wrappedBuffer(stateBuf, buf));
            sink.accept(side, id, packetBuffer);
        } else {
            int partSize = maxSize - 4;
            int parts = (int) Math.ceil(buf.readableBytes() / (float) partSize);
            for (int i = 0; i < parts; i++) {
                FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
                if (i == 0) {
                    packetBuffer.writeByte(START);
                    packetBuffer.writeInt(parts);
                } else if (i == parts - 1) {
                    packetBuffer.writeByte(END);
                } else {
                    packetBuffer.writeByte(PART);
                }
                int next = Math.min(buf.readableBytes(), partSize);
                packetBuffer.writeBytes(buf.retainedSlice(buf.readerIndex(), next));
                buf.skipBytes(next);
                sink.accept(side, id, packetBuffer);
            }
    
            buf.release();
        }
    }
}
