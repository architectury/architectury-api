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

import me.shedaniel.architectury.networking.simple.MessageType;
import me.shedaniel.architectury.networking.simple.SimpleNetworkManager;
import me.shedaniel.architectury.test.TestMod;

public interface TestModNet {
    SimpleNetworkManager NET = SimpleNetworkManager.create(TestMod.MOD_ID);
    
    // An example Client to Server message
    MessageType BUTTON_CLICKED = NET.registerC2S("button_clicked", ButtonClickedMessage::new);
    
    // An example Server to Client message
    MessageType SYNC_DATA = NET.registerS2C("sync_data", SyncDataMessage::new);
    
    static void initialize() {
    }
}
