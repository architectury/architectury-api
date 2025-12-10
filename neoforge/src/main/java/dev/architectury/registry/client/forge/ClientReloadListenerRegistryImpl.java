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

package dev.architectury.registry.client.forge;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = "architectury", value = Dist.CLIENT)
public class ClientReloadListenerRegistryImpl {
    private static Map<Identifier, PreparableReloadListener> clientDataReloadListeners = new HashMap<>();
    private static Map<Identifier, Collection<Identifier>> clientDataReloadListenerDependencies = new HashMap<>();
    
    public static void register(PreparableReloadListener listener, Identifier listenerId, Collection<Identifier> dependencies) {
        clientDataReloadListeners.put(listenerId, listener);
        clientDataReloadListenerDependencies.put(listenerId, dependencies);
    }
    
    @SubscribeEvent
    public static void addClientReloadListeners(AddClientReloadListenersEvent event) {
        clientDataReloadListeners.forEach(event::addListener);
        clientDataReloadListenerDependencies.forEach((listener, dependencies) -> dependencies.forEach(dependency -> event.addDependency(listener, dependency)));
    }
}
