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

package dev.architectury.registry.forge;

import dev.architectury.platform.Platform;
import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.utils.ArchitecturyConstants;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ReloadListenerRegistryImpl {
    private static Map<ResourceLocation, PreparableReloadListener> clientDataReloadListeners = new HashMap<>();
    private static Map<ResourceLocation, Collection<ResourceLocation>> clientDataReloadListenerDependencies = new HashMap<>();

    private static Map<ResourceLocation, PreparableReloadListener> serverDataReloadListeners = new HashMap<>();
    private static Map<ResourceLocation, Collection<ResourceLocation>> serverDataReloadListenerDependencies = new HashMap<>();
    
    static {
        EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
            if(Platform.getEnvironment() == Env.CLIENT) {
                bus.addListener(ReloadListenerRegistryImpl::addClientReloadListeners);
            }
        });

        NeoForge.EVENT_BUS.addListener(ReloadListenerRegistryImpl::addServerReloadListeners);
    }
    
    public static void register(PackType type, PreparableReloadListener listener, ResourceLocation listenerId, Collection<ResourceLocation> dependencies) {
        if (type == PackType.SERVER_DATA) {
            serverDataReloadListeners.put(listenerId, listener);
            serverDataReloadListenerDependencies.put(listenerId, dependencies);
        } else if (type == PackType.CLIENT_RESOURCES) {
            clientDataReloadListeners.put(listenerId, listener);
            clientDataReloadListenerDependencies.put(listenerId, dependencies);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void addClientReloadListeners(AddClientReloadListenersEvent event) {
        clientDataReloadListeners.forEach(event::addListener);
        clientDataReloadListenerDependencies.forEach((listener, dependencies) -> dependencies.forEach(dependency -> event.addDependency(listener, dependency)));
    }

    public static void addServerReloadListeners(AddServerReloadListenersEvent event) {
        serverDataReloadListeners.forEach(event::addListener);
        serverDataReloadListenerDependencies.forEach((listener, dependencies) -> dependencies.forEach(dependency -> event.addDependency(listener, dependency)));
    }
}
