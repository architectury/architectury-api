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
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ArchitecturyFlowingFluidImpl {
    private static final Map<Fluid, ArchitecturyFluidAttributes> ATTRIBUTES = new ConcurrentHashMap<>();
    
    public static void addFabricFluidAttributes(FlowingFluid fluid, ArchitecturyFluidAttributes attributes) {
        ATTRIBUTES.put(fluid, attributes);
        FluidVariantAttributes.register(fluid, new ArchitecturyFluidAttributesFabric(attributes));
        EnvExecutor.runInEnv(Env.CLIENT, () -> () -> Client.run(fluid, attributes));
    }
    
    public static ArchitecturyFluidAttributes getAttributes(Fluid fluid) {
        return ATTRIBUTES.get(fluid);
    }
    
    private static class Client {
        private static void run(FlowingFluid fluid, ArchitecturyFluidAttributes attributes) {
            FluidVariantRendering.register(fluid, new ArchitecturyFluidRenderingFabric(attributes));
            FluidRenderingRegistry.register(fluid, createModel(attributes), new ArchitecturyFluidRenderingFabric(attributes));
        }

        private static FluidModel.Unbaked createModel(ArchitecturyFluidAttributes attributes) {
            var overlayTexture = attributes.getOverlayTexture();
            BlockTintSource tintSource = state -> attributes.getColor();
            return new FluidModel.Unbaked(
                    new Material(attributes.getSourceTexture()),
                    new Material(attributes.getFlowingTexture()),
                    new Material(overlayTexture != null ? overlayTexture : attributes.getFlowingTexture()),
                    tintSource
            );
        }
    }
}
