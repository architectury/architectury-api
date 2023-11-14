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

import com.google.common.collect.Lists;
import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.utils.ArchitecturyConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class ReloadListenerRegistryImpl {
    private static List<PreparableReloadListener> serverDataReloadListeners = Lists.newArrayList();
    
    static {
        EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
            bus.addListener(ReloadListenerRegistryImpl::addReloadListeners);
        });
    }
    
    public static void register(PackType type, PreparableReloadListener listener, @Nullable ResourceLocation listenerId, Collection<ResourceLocation> dependencies) {
        if (type == PackType.SERVER_DATA) {
            serverDataReloadListeners.add(listener);
        } else if (type == PackType.CLIENT_RESOURCES) {
            registerClient(listener);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private static void registerClient(PreparableReloadListener listener) {
        ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(listener);
    }
    
    public static void addReloadListeners(AddReloadListenerEvent event) {
        for (PreparableReloadListener listener : serverDataReloadListeners) {
            event.addListener(listener);
        }
    }
}
