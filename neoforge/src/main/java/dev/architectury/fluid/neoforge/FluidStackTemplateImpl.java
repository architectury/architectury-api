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

package dev.architectury.fluid.neoforge;

import com.mojang.serialization.Codec;
import dev.architectury.hooks.fluid.neoforge.FluidStackTemplateHooksForge;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.architectury.utils.Amount.toInt;

@ApiStatus.Internal
public enum FluidStackTemplateImpl implements dev.architectury.fluid.FluidStackTemplate.FluidStackTemplateAdapter<FluidStackTemplate, FluidStack> {
    INSTANCE;
    
    static {
        dev.architectury.fluid.FluidStackTemplate.init();
    }
    
    public static Function<dev.architectury.fluid.FluidStackTemplate, Object> toValue;
    public static Function<Object, dev.architectury.fluid.FluidStackTemplate> fromValue;
    
    public static dev.architectury.fluid.FluidStackTemplate.FluidStackTemplateAdapter<Object, Object> adapt(Function<dev.architectury.fluid.FluidStackTemplate, Object> toValue, Function<Object, dev.architectury.fluid.FluidStackTemplate> fromValue) {
        FluidStackTemplateImpl.toValue = toValue;
        FluidStackTemplateImpl.fromValue = fromValue;
        return (dev.architectury.fluid.FluidStackTemplate.FluidStackTemplateAdapter<Object, Object>) (dev.architectury.fluid.FluidStackTemplate.FluidStackTemplateAdapter<?, ?>) INSTANCE;
    }
    
    @Override
    public FluidStackTemplate of(Supplier<Fluid> fluid, long amount, @Nullable DataComponentPatch patch) {
        Fluid fluidType = Objects.requireNonNull(fluid).get();
        
        if (fluidType.equals(Fluids.EMPTY)) {
            throw new IllegalArgumentException("Fluid is empty");
        }
        
        if (patch == null) {
            return new FluidStackTemplate(fluidType, toInt(amount));
        } else {
            return new FluidStackTemplate(fluidType, toInt(amount), patch);
        }
    }
    
    @Override
    public Holder<Fluid> fluid(FluidStackTemplate object) {
        return object.fluid();
    }
    
    @Override
    public long amount(FluidStackTemplate object) {
        return object.amount();
    }
    
    @Override
    public FluidStackTemplate withAmount(FluidStackTemplate object, int amount) {
        return object.withAmount(amount);
    }
    
    @Override
    public DataComponentMap components(FluidStackTemplate value) {
        return DataComponentMap.EMPTY;
    }
    
    @Override
    public DataComponentPatch patch(FluidStackTemplate value) {
        return value.components();
    }
    
    @Override
    public FluidStack apply(FluidStackTemplate value, DataComponentPatch patch) {
        return value.apply(patch);
    }
    
    @Override
    public FluidStack apply(FluidStackTemplate value, int amount, DataComponentPatch patch) {
        return value.apply(amount, patch);
    }
    
    @Override
    @Nullable
    public <D> D get(FluidStackTemplate value, DataComponentType<D> type) {
        return value.get(type);
    }
    
    @Override
    @Nullable
    public <D> D get(FluidStackTemplate value, Supplier<DataComponentType<D>> type) {
        return value.get(type);
    }
    
    @Override
    public FluidStackTemplate copy(FluidStackTemplate value) {
        return new FluidStackTemplate(value.fluid(), value.amount(), value.components());
    }
    
    @Override
    public int hashCode(FluidStackTemplate value) {
        int code = 1;
        code = 31 * code + value.fluid().hashCode();
        code = 31 * code + value.amount();
        code = 31 * code + value.components().hashCode();
        return code;
    }
    
    @Override
    public Codec<dev.architectury.fluid.FluidStackTemplate> codec() {
        return FluidStackTemplate.CODEC.xmap(FluidStackTemplateHooksForge::fromForge, FluidStackTemplateHooksForge::toForge);
    }
    
    @Override
    public StreamCodec<RegistryFriendlyByteBuf, dev.architectury.fluid.FluidStackTemplate> streamCodec() {
        return FluidStackTemplate.STREAM_CODEC.map(FluidStackTemplateHooksForge::fromForge, FluidStackTemplateHooksForge::toForge);
    }
}
