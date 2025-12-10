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

package dev.architectury.fluid.forge;

import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.hooks.client.forge.ClientExtensionsRegistryImpl;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Supplier;

public class ArchitecturyFluidAttributesClient {
    public static void registerClient(FluidType fluid, Supplier<ArchitecturyFluidAttributes> attributes) {
        ClientExtensionsRegistryImpl.register(event -> {
            if (event != null) {
                event.registerFluidType(initializeClient(attributes.get()), fluid);
            } else {
                try {
                    Class<?> clazz = Class.forName("net.neoforged.neoforge.client.extensions.common.ClientExtensionsManager");
                    Field field = clazz.getDeclaredField("FLUID_TYPE_EXTENSIONS");
                    field.setAccessible(true);
                    Method method = clazz.getDeclaredMethod("register", Object.class, Map.class, Object[].class);
                    method.setAccessible(true);
                    method.invoke(null, initializeClient(attributes.get()), (Map<?, ?>) field.get(null), new Object[]{fluid});
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public static IClientFluidTypeExtensions initializeClient(ArchitecturyFluidAttributes attributes) {
        return new IClientFluidTypeExtensions() {
            @Override
            public int getTintColor() {
                return attributes.getColor();
            }
            
            @Override
            public Identifier getStillTexture() {
                return attributes.getSourceTexture();
            }
            
            @Override
            public Identifier getFlowingTexture() {
                return attributes.getFlowingTexture();
            }
            
            @Override
            @Nullable
            public Identifier getOverlayTexture() {
                return attributes.getOverlayTexture();
            }
            
            @Override
            public Identifier getStillTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return attributes.getSourceTexture(state, getter, pos);
            }
            
            @Override
            public Identifier getFlowingTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return attributes.getFlowingTexture(state, getter, pos);
            }
            
            @Override
            @Nullable
            public Identifier getOverlayTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return attributes.getOverlayTexture(state, getter, pos);
            }
            
            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return attributes.getColor(state, getter, pos);
            }
            
            @Override
            public int getTintColor(FluidStack stack) {
                return attributes.getColor(convertSafe(stack));
            }
            
            @Override
            public Identifier getStillTexture(FluidStack stack) {
                return attributes.getSourceTexture(convertSafe(stack));
            }
            
            @Override
            public Identifier getFlowingTexture(FluidStack stack) {
                return attributes.getFlowingTexture(convertSafe(stack));
            }
            
            @Override
            @Nullable
            public Identifier getOverlayTexture(FluidStack stack) {
                return attributes.getOverlayTexture(convertSafe(stack));
            }
        };
    }
    
    @Nullable
    public static dev.architectury.fluid.FluidStack convertSafe(@Nullable FluidStack stack) {
        return stack == null ? null : FluidStackHooksForge.fromForge(stack);
    }
    
    @Nullable
    public static dev.architectury.fluid.FluidStack convertSafe(@Nullable FluidState state) {
        return state == null ? null : dev.architectury.fluid.FluidStack.create(state.getType(), dev.architectury.fluid.FluidStack.bucketAmount());
    }
}
