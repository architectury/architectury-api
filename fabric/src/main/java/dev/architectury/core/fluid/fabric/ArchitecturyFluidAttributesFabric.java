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
import dev.architectury.core.fluid.ArchitecturyFluidProperties;
import dev.architectury.fluid.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class ArchitecturyFluidAttributesFabric implements FluidVariantAttributeHandler {
    private final ArchitecturyFluidProperties properties;
    private final ArchitecturyFluidAttributes attributes;
    
    public ArchitecturyFluidAttributesFabric(ArchitecturyFluidProperties properties) {
        this.properties = properties;
        this.attributes = properties.getAttributes();
    }
    
    @Override
    public Component getName(FluidVariant variant) {
        return attributes.getName(FluidStack.create(variant.getFluid(), FluidStack.bucketAmount(), variant.getNbt()));
    }
    
    @Override
    public Optional<SoundEvent> getFillSound(FluidVariant variant) {
        return Optional.ofNullable(attributes.getFillSound(FluidStack.create(variant.getFluid(), FluidStack.bucketAmount(), variant.getNbt())));
    }
    
    @Override
    public Optional<SoundEvent> getEmptySound(FluidVariant variant) {
        return Optional.ofNullable(attributes.getEmptySound(FluidStack.create(variant.getFluid(), FluidStack.bucketAmount(), variant.getNbt())));
    }
    
    @Override
    public int getLuminance(FluidVariant variant) {
        return attributes.getLuminosity(FluidStack.create(variant.getFluid(), FluidStack.bucketAmount(), variant.getNbt()));
    }
    
    @Override
    public int getTemperature(FluidVariant variant) {
        return attributes.getTemperature(FluidStack.create(variant.getFluid(), FluidStack.bucketAmount(), variant.getNbt()));
    }
    
    @Override
    public int getViscosity(FluidVariant variant, @Nullable Level world) {
        return attributes.getViscosity(FluidStack.create(variant.getFluid(), FluidStack.bucketAmount(), variant.getNbt()), world, null);
    }
    
    @Override
    public boolean isLighterThanAir(FluidVariant variant) {
        return attributes.isLighterThanAir(FluidStack.create(variant.getFluid(), FluidStack.bucketAmount(), variant.getNbt()));
    }
}
