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

package dev.architectury.core.fluid.fabric;

import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.fabric.FluidStackHooksFabric;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRenderHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
class ArchitecturyFluidRenderingFabric implements FluidVariantRenderHandler, FluidRenderHandler {
    private final ArchitecturyFluidAttributes attributes;
    
    public ArchitecturyFluidRenderingFabric(ArchitecturyFluidAttributes attributes) {
        this.attributes = attributes;
    }
    
    @Override
    public int getColor(FluidVariant fluidVariant, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return attributes.getColor(FluidStackHooksFabric.fromFabric(fluidVariant, FluidStack.bucketAmount()).getFluid().defaultFluidState(), level, pos);
    }
}