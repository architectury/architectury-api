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

package dev.architectury.utils.fabric;

import dev.architectury.event.EventHandler;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class GameInstanceImpl {
    private static MinecraftServer server = null;
    
    public static MinecraftServer getServer() {
        MinecraftServer server = null;
        if (GameInstanceImpl.server != null) server = GameInstanceImpl.server;
        if (Platform.getEnvironment() == Env.CLIENT) {
            server = getServerFromClient();
        }
        return server;
    }
    
    public static void init() {
        EventHandler.init();
        LifecycleEvent.SERVER_BEFORE_START.register(server -> GameInstanceImpl.server = server);
        LifecycleEvent.SERVER_STOPPED.register(server -> GameInstanceImpl.server = null);
    }
    
    @Environment(EnvType.CLIENT)
    private static MinecraftServer getServerFromClient() {
        return Minecraft.getInstance().getSingleplayerServer();
    }
}
