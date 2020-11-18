/*
 * This file is part of architectury.
 * Copyright (C) 2020 shedaniel
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

package me.shedaniel.architectury.registry.forge;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.ResourcePackType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;

import java.util.List;

public class ReloadListenersImpl {
    private static List<IFutureReloadListener> serverDataReloadListeners = Lists.newArrayList();
    
    static {
        MinecraftForge.EVENT_BUS.<AddReloadListenerEvent>addListener(event -> {
            for (IFutureReloadListener listener : serverDataReloadListeners) {
                event.addListener(listener);
            }
        });
    }
    
    public static void registerReloadListener(ResourcePackType type, IFutureReloadListener listener) {
        if (type == ResourcePackType.SERVER_DATA) {
            serverDataReloadListeners.add(listener);
        } else if (type == ResourcePackType.CLIENT_RESOURCES) {
            reloadClientReloadListener(listener);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private static void reloadClientReloadListener(IFutureReloadListener listener) {
        ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(listener);
    }
}
