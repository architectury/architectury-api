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
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@ApiStatus.Internal
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
        public Fluid fluid;
        public PatchedDataComponentMap components;
        public long amount;
        
        public Pair(Fluid fluid, @Nullable DataComponentPatch patch, long amount) {
            this(fluid,
                    patch == null ? new PatchedDataComponentMap(DataComponentMap.EMPTY)
                            : PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, patch),
                    amount);
        }
        
        public Pair(Fluid fluid, PatchedDataComponentMap components, long amount) {
            this.fluid = fluid;
            this.components = components;
            this.amount = amount;
        }
        
        public FluidVariant toVariant() {
            return FluidVariant.of(fluid, getPatch());
        }
        
        public DataComponentPatch getPatch() {
            return amount <= 0L || this.fluid == Fluids.EMPTY ? components.asPatch() : DataComponentPatch.EMPTY;
        }
    }
    
    @Override
    public FluidStackImpl.Pair create(Supplier<Fluid> fluid, long amount, @Nullable DataComponentPatch patch) {
        Fluid fluidType = Objects.requireNonNull(fluid).get();
        if (fluidType instanceof FlowingFluid flowingFluid) {
            fluidType = flowingFluid.getSource();
        }
        return new Pair(fluidType, patch, amount);
    }
    
    @Override
    public Supplier<Fluid> getRawFluidSupplier(FluidStackImpl.Pair object) {
        return () -> object.fluid;
    }
    
    @Override
    public Fluid getFluid(FluidStackImpl.Pair object) {
        return object.fluid;
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
        return value.getPatch();
    }
    
    @Override
    public PatchedDataComponentMap getComponents(Pair value) {
        return value.components;
    }
    
    @Override
    public void applyComponents(Pair value, DataComponentPatch patch) {
        value.components.applyPatch(patch);
    }
    
    @Override
    public void applyComponents(Pair value, DataComponentMap patch) {
        value.components.setAll(patch);
    }
    
    @Override
    @Nullable
    public <D> D set(Pair value, DataComponentType<? super D> type, @Nullable D component) {
        return value.components.set(type, component);
    }
    
    @Override
    @Nullable
    public <D> D remove(Pair value, DataComponentType<? extends D> type) {
        return value.components.remove(type);
    }
    
    @Override
    @Nullable
    public <D> D update(Pair value, DataComponentType<D> type, D component, UnaryOperator<D> updater) {
        return value.components.set(type, updater.apply(getComponents(value).getOrDefault(type, component)));
    }
    
    @Override
    @Nullable
    public <D, U> D update(Pair value, DataComponentType<D> type, D component, U updateContext, BiFunction<D, U, D> updater) {
        return value.components.set(type, updater.apply(getComponents(value).getOrDefault(type, component), updateContext));
    }
    
    @Override
    public FluidStackImpl.Pair copy(FluidStackImpl.Pair value) {
        return new Pair(value.fluid, value.components.copy(), value.amount);
    }
    
    @Override
    public int hashCode(FluidStackImpl.Pair value) {
        var pair = (Pair) value;
        var code = 1;
        code = 31 * code + pair.fluid.hashCode();
        code = 31 * code + Long.hashCode(pair.amount);
        code = 31 * code + pair.components.hashCode();
        return code;
    }
    
    @Override
    public Codec<FluidStack> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("fluid").forGetter(stack -> stack.getFluid().builtInRegistryHolder()),
                Codec.LONG.validate(value -> {
                    return value.compareTo(0L) >= 0 && value.compareTo(Long.MAX_VALUE) <= 0
                            ? DataResult.success(value)
                            : DataResult.error(() -> "Value must be non-negative: " + value);
                }).fieldOf("amount").forGetter(FluidStack::getAmount),
                DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(FluidStack::getPatch)
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
