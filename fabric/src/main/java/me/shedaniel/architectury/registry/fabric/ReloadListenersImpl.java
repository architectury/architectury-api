/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

package me.shedaniel.architectury.registry.fabric;

import com.google.common.primitives.Longs;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ReloadListenersImpl {
    private static final SecureRandom RANDOM = new SecureRandom();
    
    public static void registerReloadListener(PackType type, PreparableReloadListener listener) {
        byte[] bytes = new byte[8];
        RANDOM.nextBytes(bytes);
        ResourceLocation id = new ResourceLocation("architectury:reload_" + StringUtils.leftPad(Math.abs(Longs.fromByteArray(bytes)) + "", 19, '0'));
        ResourceManagerHelper.get(type).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return id;
            }
            
            @Override
            public String getName() {
                return listener.getName();
            }
            
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
                return listener.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
            }
        });
    }
}
