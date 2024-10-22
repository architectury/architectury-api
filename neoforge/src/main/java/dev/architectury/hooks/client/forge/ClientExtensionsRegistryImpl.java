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

package dev.architectury.hooks.client.forge;

import dev.architectury.utils.ArchitecturyConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@EventBusSubscriber(modid = ArchitecturyConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientExtensionsRegistryImpl {
    private static final List<Consumer<@Nullable RegisterClientExtensionsEvent>> CALLBACKS = new ArrayList<>();
    private static boolean called = false;
    
    public static void register(Consumer<@Nullable RegisterClientExtensionsEvent> callback) {
        if (ClientExtensionsRegistryImpl.called) {
            callback.accept(null);
        } else {
            CALLBACKS.add(callback);
        }
    }
    
    @SubscribeEvent
    public static void onEvent(RegisterClientExtensionsEvent event) {
        ClientExtensionsRegistryImpl.called = true;
        for (Consumer<@Nullable RegisterClientExtensionsEvent> callback : CALLBACKS) {
            callback.accept(event);
        }
    }
}
