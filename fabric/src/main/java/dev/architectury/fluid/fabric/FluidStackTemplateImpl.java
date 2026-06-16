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
import dev.architectury.fluid.FluidStackTemplate;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.Holder;
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
import java.util.function.Function;
import java.util.function.Supplier;

@ApiStatus.Internal
public enum FluidStackTemplateImpl implements FluidStackTemplate.FluidStackTemplateAdapter<FluidStackTemplateImpl.Pair, FluidStackImpl.Pair> {
    INSTANCE;
    
    static {
        FluidStackTemplate.init();
    }
    
    public static Function<FluidStackTemplate, Object> toValue;
    public static Function<Object, FluidStackTemplate> fromValue;
    
    public static FluidStackTemplate.FluidStackTemplateAdapter<Object, Object> adapt(Function<FluidStackTemplate, Object> toValue, Function<Object, FluidStackTemplate> fromValue) {
        FluidStackTemplateImpl.toValue = toValue;
        FluidStackTemplateImpl.fromValue = fromValue;
        return (FluidStackTemplate.FluidStackTemplateAdapter<Object, Object>) (FluidStackTemplate.FluidStackTemplateAdapter<?, ?>) INSTANCE;
    }
    
    public static class Pair {
        public final Fluid fluid;
        public final PatchedDataComponentMap components;
        public final long amount;
        
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
    public Pair of(Supplier<Fluid> fluid, long amount, @Nullable DataComponentPatch patch) {
        Fluid fluidType = Objects.requireNonNull(fluid).get();
        if (fluidType instanceof FlowingFluid flowingFluid) {
            fluidType = flowingFluid.getSource();
        }
        return new Pair(fluidType, patch, amount);
    }
    
    @Override
    public Holder<Fluid> fluid(Pair object) {
        return object.fluid.builtInRegistryHolder();
    }
    
    @Override
    public long amount(Pair object) {
        return object.amount;
    }
    
    @Override
    public Pair withAmount(Pair object, int amount) {
        return new Pair(object.fluid, object.components, amount);
    }
    
    @Override
    public DataComponentMap components(Pair value) {
        return value.components;
    }
    
    @Override
    public DataComponentPatch patch(Pair value) {
        return value.components.asPatch();
    }
    
    @Override
    public FluidStackImpl.Pair apply(Pair value, DataComponentPatch patch) {
        FluidStackImpl.Pair newPair =  new FluidStackImpl.Pair(value.fluid, value.components, value.amount);
        newPair.components.applyPatch(patch);
        return newPair;
    }
    
    @Override
    public FluidStackImpl.Pair apply(Pair value, int amount, DataComponentPatch patch) {
        FluidStackImpl.Pair newPair =  new FluidStackImpl.Pair(value.fluid, value.components, amount);
        newPair.components.applyPatch(patch);
        return newPair;
    }
    
    @Override
    public @org.jspecify.annotations.Nullable <D> D get(Pair value, DataComponentType<D> type) {
        return value.components.get(type);
    }
    
    @Override
    public @org.jspecify.annotations.Nullable <D> D get(Pair value, Supplier<DataComponentType<D>> type) {
        return value.components.get(type.get());
    }
    
    @Override
    public Pair copy(FluidStackTemplateImpl.Pair value) {
        return new Pair(value.fluid, value.components.copy(), value.amount);
    }
    
    @Override
    public int hashCode(FluidStackTemplateImpl.Pair value) {
        int code = 1;
        code = 31 * code + value.fluid.hashCode();
        code = 31 * code + Long.hashCode(value.amount);
        code = 31 * code + value.components.hashCode();
        return code;
    }
    
    @Override
    public Codec<FluidStackTemplate> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("fluid").forGetter(FluidStackTemplate::fluid),
                Codec.LONG.validate(value -> value.compareTo(0L) >= 0 && value.compareTo(Long.MAX_VALUE) <= 0
                        ? DataResult.success(value)
                        : DataResult.error(() -> "Value must be non-negative: " + value)).fieldOf("amount").forGetter(FluidStackTemplate::amount),
                DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(FluidStackTemplate::getPatch)
        ).apply(instance, FluidStackTemplate::of));
    }
    
    @Override
    public StreamCodec<RegistryFriendlyByteBuf, FluidStackTemplate> streamCodec() {
        return StreamCodec.composite(ByteBufCodecs.holderRegistry(Registries.FLUID), FluidStackTemplate::fluid,
                StreamCodec.of(ByteBuf::writeLong, ByteBuf::readLong), FluidStackTemplate::amount,
                DataComponentPatch.STREAM_CODEC, FluidStackTemplate::getPatch,
                FluidStackTemplate::of);
    }
}
