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

import com.mojang.serialization.Codec;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static dev.architectury.utils.Amount.toInt;

@ApiStatus.Internal
public enum FluidStackImpl implements dev.architectury.fluid.FluidStack.FluidStackAdapter<FluidStack> {
    INSTANCE;
    
    static {
        dev.architectury.fluid.FluidStack.init();
    }
    
    public static Function<dev.architectury.fluid.FluidStack, Object> toValue;
    public static Function<Object, dev.architectury.fluid.FluidStack> fromValue;
    
    public static dev.architectury.fluid.FluidStack.FluidStackAdapter<Object> adapt(Function<dev.architectury.fluid.FluidStack, Object> toValue, Function<Object, dev.architectury.fluid.FluidStack> fromValue) {
        FluidStackImpl.toValue = toValue;
        FluidStackImpl.fromValue = fromValue;
        return (dev.architectury.fluid.FluidStack.FluidStackAdapter<Object>) (dev.architectury.fluid.FluidStack.FluidStackAdapter<?>) INSTANCE;
    }
    
    @Override
    public FluidStack create(Supplier<Fluid> fluid, long amount, @Nullable DataComponentPatch patch) {
        @SuppressWarnings("deprecation")
        Holder<Fluid> holder = Objects.requireNonNull(fluid).get().builtInRegistryHolder();
        if (patch == null) {
            return new FluidStack(holder, toInt(amount));
        } else {
            return new FluidStack(holder, toInt(amount), patch);
        }
    }
    
    @Override
    public Supplier<Fluid> getRawFluidSupplier(FluidStack object) {
        return () -> object.getFluidHolder().value();
    }
    
    @Override
    public Fluid getFluid(FluidStack object) {
        return object.getFluid();
    }
    
    @Override
    public long getAmount(FluidStack object) {
        return object.getAmount();
    }
    
    @Override
    public void setAmount(FluidStack object, long amount) {
        object.setAmount(toInt(amount));
    }
    
    @Override
    public DataComponentPatch getPatch(FluidStack value) {
        return value.getComponentsPatch();
    }
    
    @Override
    public PatchedDataComponentMap getComponents(FluidStack value) {
        return value.getComponents();
    }
    
    @Override
    public void applyComponents(FluidStack value, DataComponentPatch patch) {
        value.applyComponents(patch);
    }
    
    @Override
    public void applyComponents(FluidStack value, DataComponentMap patch) {
        value.applyComponents(patch);
    }
    
    @Override
    @Nullable
    public <D> D set(FluidStack value, DataComponentType<? super D> type, @Nullable D component) {
        return value.set(type, component);
    }
    
    @Override
    @Nullable
    public <D> D remove(FluidStack value, DataComponentType<? extends D> type) {
        return value.remove(type);
    }
    
    @Override
    @Nullable
    public <D> D update(FluidStack value, DataComponentType<D> type, D component, UnaryOperator<D> updater) {
        return value.update(type, component, updater);
    }
    
    @Override
    @Nullable
    public <D, U> D update(FluidStack value, DataComponentType<D> type, D component, U updateContext, BiFunction<D, U, D> updater) {
        return value.update(type, component, updateContext, updater);
    }
    
    
    @Override
    public FluidStack copy(FluidStack value) {
        return value.copy();
    }
    
    @Override
    public int hashCode(FluidStack value) {
        var code = 1;
        code = 31 * code + value.getFluid().hashCode();
        code = 31 * code + value.getAmount();
        code = 31 * code + value.getComponents().hashCode();
        return code;
    }
    
    @Override
    public Codec<dev.architectury.fluid.FluidStack> codec() {
        return FluidStack.CODEC.xmap(FluidStackHooksForge::fromForge, FluidStackHooksForge::toForge);
    }
    
    @Override
    public StreamCodec<RegistryFriendlyByteBuf, dev.architectury.fluid.FluidStack> streamCodec() {
        return FluidStack.STREAM_CODEC.map(FluidStackHooksForge::fromForge, FluidStackHooksForge::toForge);
    }
}
