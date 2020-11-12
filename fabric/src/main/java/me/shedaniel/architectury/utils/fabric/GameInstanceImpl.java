/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.shedaniel.architectury.utils.fabric;

import me.shedaniel.architectury.event.EventHandler;
import me.shedaniel.architectury.event.events.LifecycleEvent;
import me.shedaniel.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class GameInstanceImpl {
    private static MinecraftServer server = null;
    
    public static MinecraftServer getServer() {
        MinecraftServer server = null;
        if (GameInstanceImpl.server != null) server = GameInstanceImpl.server;
        if (Platform.getEnv() == EnvType.CLIENT) {
            server = getServerFromClient();
        }
        return server;
    }
    
    public static void init() {
        EventHandler.init();
        LifecycleEvent.SERVER_STARTING.register(server -> GameInstanceImpl.server = server);
        LifecycleEvent.SERVER_STOPPED.register(server -> GameInstanceImpl.server = null);
    }
    
    @Environment(EnvType.CLIENT)
    private static MinecraftServer getServerFromClient() {
        return Minecraft.getInstance().getSingleplayerServer();
    }
}
