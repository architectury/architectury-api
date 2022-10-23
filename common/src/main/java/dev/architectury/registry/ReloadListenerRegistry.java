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

package dev.architectury.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public final class ReloadListenerRegistry {
    private ReloadListenerRegistry() {
    }
    
    public static void register(PackType type, PreparableReloadListener listener) {
        register(type, listener, null);
    }
    
    public static void register(PackType type, PreparableReloadListener listener, @Nullable ResourceLocation listenerId) {
        register(type, listener, listenerId, List.of());
    }
    
    @ExpectPlatform
    public static void register(PackType type, PreparableReloadListener listener, @Nullable ResourceLocation listenerId, Collection<ResourceLocation> dependencies) {
        throw new AssertionError();
    }
}
