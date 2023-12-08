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

package dev.architectury.hooks.forgelike.forge;

import com.mojang.serialization.Codec;
import dev.architectury.platform.hooks.forge.EventBusesHooksImpl;
import dev.architectury.utils.ArchitecturyConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class ForgeLikeHooksImpl {
    private static final Logger LOGGER = LogManager.getLogger(ForgeLikeHooksImpl.class);
    
    public static void registerBiomeModifier(ResourceLocation id, Supplier<Codec<? extends BiomeModifier>> codecSupplier) {
        EventBusesHooksImpl.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
            bus.<RegisterEvent>addListener(event -> {
                event.register(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, registry -> {
                    registry.register(id, codecSupplier.get());
                });
            });
        });
    }
    
    public static void registerBucketItemCapability(Item item) {
        EventBusesHooksImpl.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
            bus.<RegisterCapabilitiesEvent>addListener(event -> {
                if (BuiltInRegistries.ITEM.containsValue(item)) {
                    event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidBucketWrapper(stack), item);
                } else {
                    LOGGER.warn("Tried to register a bucket item capability for an item that is not registered: {}", item);
                }
            });
        });
    }
}
