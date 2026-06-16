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

package dev.architectury.hooks.fluid;

import dev.architectury.fluid.FluidStackTemplate;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.Optional;

public class FluidStackTemplateHooks {
    private FluidStackTemplateHooks() {
    }
    
    @ExpectPlatform
    public static Component getName(FluidStackTemplate stack) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static String getTranslationKey(FluidStackTemplate stack) {
        throw new AssertionError();
    }
    
    /**
     * Platform-specific FluidStack read.
     */
    @ExpectPlatform
    public static FluidStackTemplate read(RegistryFriendlyByteBuf buf) {
        throw new AssertionError();
    }
    
    /**
     * Platform-specific FluidStack write.
     */
    @ExpectPlatform
    public static void write(FluidStackTemplate stack, RegistryFriendlyByteBuf buf) {
        throw new AssertionError();
    }
    
    /**
     * Platform-specific FluidStack read.
     */
    @ExpectPlatform
    public static Optional<FluidStackTemplate> read(HolderLookup.Provider provider, Tag tag) {
        throw new AssertionError();
    }
    
    /**
     * Platform-specific FluidStack write.
     */
    @ExpectPlatform
    public static Tag write(HolderLookup.Provider provider, FluidStackTemplate stack, Tag tag) {
        throw new AssertionError();
    }
}
