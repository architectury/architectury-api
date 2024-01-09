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

package dev.architectury.neoforge;

import dev.architectury.event.EventHandler;
import dev.architectury.event.events.common.ChunkWatchEvent;
import dev.architectury.networking.SpawnEntityPacket;
import dev.architectury.registry.level.biome.forge.BiomeModificationsImpl;
import dev.architectury.utils.ArchitecturyConstants;
import net.neoforged.bus.api.SubscribeEvent;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.neoforged.fml.common.Mod;

@Mod(ArchitecturyConstants.MOD_ID)
public class ArchitecturyNeoForge {
    public ArchitecturyNeoForge() {
        EventHandler.init();
        BiomeModificationsImpl.init();
        
        EnvExecutor.runInEnv(Env.CLIENT, () -> SpawnEntityPacket.Client::register);
    }
    
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    private static class ForgeBusSubscriber {
        @SubscribeEvent
        private static void event(net.neoforged.neoforge.event.level.ChunkWatchEvent.Watch event) {
            ChunkWatchEvent.WATCH.invoker().listen(event.getChunk(), event.getLevel(), event.getPlayer());
        }
        
        @SubscribeEvent
        private static void event(net.neoforged.neoforge.event.level.ChunkWatchEvent.Sent event) {
            ChunkWatchEvent.SENT.invoker().listen(event.getChunk(), event.getLevel(), event.getPlayer());
        }
        
        @SubscribeEvent
        private static void event(net.neoforged.neoforge.event.level.ChunkWatchEvent.UnWatch event) {
            ChunkWatchEvent.UNWATCH.invoker().listen(event.getPos(), event.getLevel(), event.getPlayer());
        }
    }
}
