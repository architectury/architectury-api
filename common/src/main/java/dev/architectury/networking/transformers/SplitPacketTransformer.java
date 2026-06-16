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

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.transformers.client.ClientSplitPacketTransformer;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
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
    
    @ApiStatus.Internal
    public record PartKey(NetworkManager.Side side, @Nullable UUID playerUUID) {
    }
    
    public record PartData(
            Identifier id,
            int partsExpected,
            List<RegistryFriendlyByteBuf> parts
    ) {
        private PartData(Identifier id, int partsExpected) {
            this(id, partsExpected, new ArrayList<>());
        }
    }
    
    private final Map<PartKey, PartData> cache = Collections.synchronizedMap(new HashMap<>());
    
    public SplitPacketTransformer() {
        PlayerEvent.PLAYER_QUIT.register(player -> {
            cache.keySet().removeIf(key -> Objects.equals(key.playerUUID, player.getUUID()));
        });
        EnvExecutor.runInEnv(Env.CLIENT, () -> () -> ClientSplitPacketTransformer.init(cache));
    }
    
    @Override
    public void inbound(NetworkManager.Side side, Identifier id, RegistryFriendlyByteBuf buf, NetworkManager.PacketContext context, TransformationSink sink) {
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
                    for (RegistryFriendlyByteBuf part : data.parts) {
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
                    for (RegistryFriendlyByteBuf part : data.parts) {
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
                    LOGGER.warn("Received invalid END packet for SplitPacketTransformer with packet id " + id + " for side " + side + " with size " + data.parts.size() + ", parts expected is " + data.partsExpected);
                    for (RegistryFriendlyByteBuf part : data.parts) {
                        if (part != buf) {
                            part.release();
                        }
                    }
                } else {
                    RegistryFriendlyByteBuf byteBuf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(data.parts.toArray(new ByteBuf[0])), buf.registryAccess());
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
    public void outbound(NetworkManager.Side side, Identifier id, RegistryFriendlyByteBuf buf, TransformationSink sink) {
        int maxSize = (side == NetworkManager.Side.C2S ? 32767 : 1048576) - 1 - 20 - id.toString().getBytes(StandardCharsets.UTF_8).length;
        if (buf.readableBytes() <= maxSize) {
            ByteBuf stateBuf = Unpooled.buffer(1);
            stateBuf.writeByte(ONLY);
            RegistryFriendlyByteBuf packetBuffer = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(stateBuf, buf), buf.registryAccess());
            sink.accept(side, id, packetBuffer);
        } else {
            int partSize = maxSize - 4;
            int parts = (int) Math.ceil(buf.readableBytes() / (float) partSize);
            for (int i = 0; i < parts; i++) {
                RegistryFriendlyByteBuf packetBuffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), buf.registryAccess());
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
