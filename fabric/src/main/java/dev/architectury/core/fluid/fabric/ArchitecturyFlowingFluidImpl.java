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

import dev.architectury.core.fluid.ArchitecturyFluidProperties;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.world.level.material.FlowingFluid;

public class ArchitecturyFlowingFluidImpl {
    public static void addFabricFluidAttributes(FlowingFluid fluid, ArchitecturyFluidProperties properties) {
        FluidVariantAttributes.register(fluid, new ArchitecturyFluidAttributesFabric(properties));
        EnvExecutor.runInEnv(Env.CLIENT, () -> () -> Client.run(fluid, properties));
    }
    
    private static class Client {
        private static void run(FlowingFluid fluid, ArchitecturyFluidProperties properties) {
            FluidVariantRendering.register(fluid, new ArchitecturyFluidRenderingFabric(properties));
        }
    }
}
