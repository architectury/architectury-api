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

package me.shedaniel.architectury.event;

import dev.architectury.injectables.annotations.ExpectPlatform;
import me.shedaniel.architectury.event.events.BlockEvent;
import me.shedaniel.architectury.event.events.EntityEvent;
import me.shedaniel.architectury.event.events.PlayerEvent;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public final class EventHandler {
    private EventHandler() {
    }
    
    private static boolean initialized = false;
    
    public static void init() {
        if (initialized) return;
        initialized = true;
        if (Platform.getEnvironment() == Env.CLIENT)
            registerClient();
        registerCommon();
        if (Platform.getEnvironment() == Env.SERVER)
            registerServer();
        
        registerDelegates();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    private static void registerClient() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    private static void registerCommon() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.SERVER)
    private static void registerServer() {
        throw new AssertionError();
    }
    
    @SuppressWarnings("deprecation")
    private static void registerDelegates() {
        BlockEvent.PLACE.register((EntityEvent.PLACE_BLOCK.invoker()::placeBlock));
        BlockEvent.BREAK.register((PlayerEvent.BREAK_BLOCK.invoker()::breakBlock));
    }
}
