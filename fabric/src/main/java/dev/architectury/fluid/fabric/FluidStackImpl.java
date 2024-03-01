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

package dev.architectury.fluid.fabric;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.fluid.FluidStack;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@ApiStatus.Internal
@SuppressWarnings("UnstableApiUsage")
public enum FluidStackImpl implements FluidStack.FluidStackAdapter<FluidStackImpl.Pair> {
    INSTANCE;
    
    static {
        dev.architectury.fluid.FluidStack.init();
    }
    
    public static Function<FluidStack, Object> toValue;
    public static Function<Object, FluidStack> fromValue;
    
    public static FluidStack.FluidStackAdapter<Object> adapt(Function<FluidStack, Object> toValue, Function<Object, dev.architectury.fluid.FluidStack> fromValue) {
        FluidStackImpl.toValue = toValue;
        FluidStackImpl.fromValue = fromValue;
        return (FluidStack.FluidStackAdapter<Object>) (FluidStack.FluidStackAdapter<?>) INSTANCE;
    }
    
    public static class Pair {
        public FluidVariant variant;
        public long amount;
        
        public Pair(FluidVariant variant, long amount) {
            this.variant = variant;
            this.amount = amount;
        }
    }
    
    @Override
    public FluidStackImpl.Pair create(Supplier<Fluid> fluid, long amount, @Nullable DataComponentPatch patch) {
        Fluid fluidType = Objects.requireNonNull(fluid).get();
        if (fluidType instanceof FlowingFluid flowingFluid) {
            fluidType = flowingFluid.getSource();
        }
        return new Pair(FluidVariant.of(fluidType, patch == null ? DataComponentPatch.EMPTY : patch), amount);
    }
    
    @Override
    public Supplier<Fluid> getRawFluidSupplier(FluidStackImpl.Pair object) {
        return () -> object.variant.getFluid();
    }
    
    @Override
    public Fluid getFluid(FluidStackImpl.Pair object) {
        return object.variant.getFluid();
    }
    
    @Override
    public long getAmount(FluidStackImpl.Pair object) {
        return object.amount;
    }
    
    @Override
    public void setAmount(FluidStackImpl.Pair object, long amount) {
        object.amount = amount;
    }
    
    public DataComponentPatch getPatch(FluidStackImpl.Pair value) {
        return value.variant.getComponents();
    }
    
    @Override
    public void setPatch(FluidStackImpl.Pair value, DataComponentPatch patch) {
        value.variant = FluidVariant.of(value.variant.getFluid(), patch);
    }
    
    @Override
    public FluidStackImpl.Pair copy(FluidStackImpl.Pair value) {
        return new Pair(value.variant, value.amount);
    }
    
    @Override
    public int hashCode(FluidStackImpl.Pair value) {
        var pair = (Pair) value;
        var code = 1;
        code = 31 * code + pair.variant.hashCode();
        code = 31 * code + Long.hashCode(pair.amount);
        var patch = pair.variant.getComponents();
        if (patch != null)
            code = 31 * code + patch.hashCode();
        return code;
    }
    
    @Override
    public Codec<FluidStack> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("fluid").forGetter(stack -> stack.getFluid().builtInRegistryHolder()),
                ExtraCodecs.validate(Codec.LONG, value -> {
                    return value.compareTo(0L) >= 0 && value.compareTo(Long.MAX_VALUE) <= 0
                            ? DataResult.success(value)
                            : DataResult.error(() -> "Value must be non-negative: " + value);
                }).fieldOf("amount").forGetter(FluidStack::getAmount),
                DataComponentPatch.CODEC.fieldOf("components").forGetter(FluidStack::getPatch)
        ).apply(instance, FluidStack::create));
    }
    
    @Override
    public StreamCodec<RegistryFriendlyByteBuf, FluidStack> streamCodec() {
        return StreamCodec.composite(ByteBufCodecs.holderRegistry(Registries.FLUID), stack -> stack.getFluid().builtInRegistryHolder(),
                StreamCodec.of(ByteBuf::writeLong, ByteBuf::readLong), FluidStack::getAmount,
                DataComponentPatch.STREAM_CODEC, FluidStack::getPatch,
                FluidStack::create);
    }
}
