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
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import dev.architectury.networking.transformers.SplitPacketTransformer;
import dev.architectury.test.TestMod;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;

public interface TestModNet {
    SimpleNetworkManager NET = SimpleNetworkManager.create(TestMod.MOD_ID);
    
    // An example Client to Server message
    MessageType BUTTON_CLICKED = NET.registerC2S("button_clicked", ButtonClickedMessage::new);
    
    // An example Server to Client message
    MessageType SYNC_DATA = NET.registerS2C("sync_data", SyncDataMessage::new);
    ResourceLocation BIG_DATA = new ResourceLocation(TestMod.MOD_ID, "big_data");
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
    }
    
    static void initializeClient() {
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf(BIG_STRING, Integer.MAX_VALUE / 4);
            // write twice
            buf.writeUtf(BIG_STRING, Integer.MAX_VALUE / 4);
            NetworkManager.sendToServer(BIG_DATA, buf);
        });
    }
}