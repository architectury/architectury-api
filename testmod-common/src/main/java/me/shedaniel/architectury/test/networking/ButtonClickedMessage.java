/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

package me.shedaniel.architectury.test.networking;

import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.networking.simple.BaseC2SMessage;
import me.shedaniel.architectury.networking.simple.MessageType;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;

public class ButtonClickedMessage extends BaseC2SMessage {
    private final int buttonId;
    
    /**
     * To send this message from client to server, call new ButtonClickedMessage(id).sendToServer()
     */
    public ButtonClickedMessage(int id) {
        buttonId = id;
    }
    
    public ButtonClickedMessage(FriendlyByteBuf buf) {
        buttonId = buf.readVarInt();
    }
    
    @Override
    public MessageType getType() {
        return TestModNet.BUTTON_CLICKED;
    }
    
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(buttonId);
    }
    
    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.getPlayer().sendMessage(new TextComponent("You clicked button #" + buttonId), Util.NIL_UUID);
    }
}
