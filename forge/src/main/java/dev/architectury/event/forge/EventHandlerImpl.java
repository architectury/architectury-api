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

package dev.architectury.event.forge;

import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.utils.ArchitecturyConstants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

public class EventHandlerImpl {
    @OnlyIn(Dist.CLIENT)
    public static void registerClient() {
        MinecraftForge.EVENT_BUS.register(EventHandlerImplClient.class);
        EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
            bus.register(EventHandlerImplClient.ModBasedEventHandler.class);
        });
    }
    
    public static void registerCommon() {
        MinecraftForge.EVENT_BUS.register(EventHandlerImplCommon.class);
        EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
            bus.register(EventHandlerImplCommon.ModBasedEventHandler.class);
        });
    }
    
    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void registerServer() {
        MinecraftForge.EVENT_BUS.register(EventHandlerImplServer.class);
        EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
            bus.register(EventHandlerImplServer.ModBasedEventHandler.class);
        });
    }
}
