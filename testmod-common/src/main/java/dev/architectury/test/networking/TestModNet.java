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

package dev.architectury.test.networking;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import dev.architectury.networking.transformers.SplitPacketTransformer;
import dev.architectury.test.TestMod;
import io.netty.buffer.Unpooled;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

public interface TestModNet {
    SimpleNetworkManager NET = SimpleNetworkManager.create(TestMod.MOD_ID);
    
    // An example Client to Server message
    MessageType BUTTON_CLICKED = NET.registerC2S("button_clicked", ButtonClickedMessage::new);
    
    // An example Server to Client message
    MessageType SYNC_DATA = NET.registerS2C("sync_data", SyncDataMessage::new);
    ResourceLocation BIG_DATA = new ResourceLocation(TestMod.MOD_ID, "big_data");
    ResourceLocation SERVER_TO_CLIENT_TEST = new ResourceLocation(TestMod.MOD_ID, "s2c_test");
    CustomPacketPayload.Type<ServerToClientTestPayload> SERVER_TO_CLIENT_TEST_PAYLOAD = new CustomPacketPayload.Type<>(new ResourceLocation(TestMod.MOD_ID, "s2c_test_payload"));
    CustomPacketPayload.Type<BigDataPayload> BIG_DATA_PAYLOAD = new CustomPacketPayload.Type<>(new ResourceLocation(TestMod.MOD_ID, "big_data_payload"));
    String BIG_STRING = StringUtils.repeat('a', 100000);
    
    static void initialize() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, BIG_DATA, Collections.singletonList(new SplitPacketTransformer()), (buf, context) -> {
            String utf = buf.readUtf(Integer.MAX_VALUE / 4);
            if (utf.equals(BIG_STRING)) {
                TestMod.SINK.accept("Network Split Packets worked");
            } else {
                throw new AssertionError(utf);
            }
            utf = buf.readUtf(Integer.MAX_VALUE / 4);
            if (utf.equals(BIG_STRING)) {
                TestMod.SINK.accept("Network Split Packets worked");
            } else {
                throw new AssertionError(utf);
            }
        });
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, BIG_DATA_PAYLOAD, new StreamCodec<>() {
            @Override
            public BigDataPayload decode(RegistryFriendlyByteBuf object) {
                return new BigDataPayload(object.readUtf(Integer.MAX_VALUE / 4));
            }
            
            @Override
            public void encode(RegistryFriendlyByteBuf object, BigDataPayload payload) {
                object.writeUtf(payload.data, Integer.MAX_VALUE / 4);
            }
        }, List.of(new SplitPacketTransformer()), (value, context) -> {
            if (value.data().equals(BIG_STRING)) {
                TestMod.SINK.accept("Network Split Packets worked");
            } else {
                throw new AssertionError(value.data());
            }
        });
    
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, SERVER_TO_CLIENT_TEST, (buf, context) -> {
            long num = buf.readLong();
            if (num == 0xA4C5E75EC7941L) {
                TestMod.SINK.accept("S2C worked!, 0xA4C5E75EC7941L");
            } else {
                throw new AssertionError(num);
            }
        });
    
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, SERVER_TO_CLIENT_TEST_PAYLOAD, new StreamCodec<>() {
            @Override
            public ServerToClientTestPayload decode(RegistryFriendlyByteBuf object) {
                return new ServerToClientTestPayload(object.readLong());
            }
        
            @Override
            public void encode(RegistryFriendlyByteBuf object, ServerToClientTestPayload payload) {
                object.writeLong(payload.num);
            }
        }, (value, context) -> {
            if (value.num() == 0xA4C5E75EC7941L) {
                TestMod.SINK.accept("S2C worked!, 0xA4C5E75EC7941L");
            } else {
                throw new AssertionError(value.num());
            }
        });
    
        PlayerEvent.PLAYER_JOIN.register(player -> {
            NetworkManager.sendToPlayer(player, new ServerToClientTestPayload(0xA4C5E75EC7941L));
        });
    }
    
    static void initializeClient() {
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> {
            RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), player.registryAccess());
            buf.writeUtf(BIG_STRING, Integer.MAX_VALUE / 4);
            // write twice
            buf.writeUtf(BIG_STRING, Integer.MAX_VALUE / 4);
            NetworkManager.sendToServer(BIG_DATA, buf);
            NetworkManager.sendToServer(new BigDataPayload(BIG_STRING));
        });
    }
    
    record BigDataPayload(String data) implements CustomPacketPayload {
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TestModNet.BIG_DATA_PAYLOAD;
        }
    }
    
    record ServerToClientTestPayload(long num) implements CustomPacketPayload {
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TestModNet.SERVER_TO_CLIENT_TEST_PAYLOAD;
        }
    }
}