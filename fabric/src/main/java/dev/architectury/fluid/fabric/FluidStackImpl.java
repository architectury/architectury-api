/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

import dev.architectury.fluid.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@ApiStatus.Internal
public enum FluidStackImpl implements FluidStack.FluidStackAdapter {
    INSTANCE;
    
    public static Function<FluidStack, Object> toValue;
    public static Function<Object, FluidStack> fromValue;
    
    public static FluidStack.FluidStackAdapter adapt(Function<FluidStack, Object> toValue, Function<Object, dev.architectury.fluid.FluidStack> fromValue) {
        FluidStackImpl.toValue = toValue;
        FluidStackImpl.fromValue = fromValue;
        return INSTANCE;
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
    public Object create(Supplier<Fluid> fluid, long amount, CompoundTag tag) {
        return new Pair(FluidVariant.of(Objects.requireNonNull(fluid).get(), tag == null ? null : tag.copy()), amount);
    }
    
    @Override
    public void check(Object object) {
        if (!(object instanceof Pair)) {
            throw new IllegalArgumentException("Expected FluidStackImpl.Pair, got " + object.getClass().getName());
        }
    }
    
    @Override
    public Supplier<Fluid> getRawFluidSupplier(Object object) {
        return ((Pair) object).variant::getFluid;
    }
    
    @Override
    public Fluid getFluid(Object object) {
        return ((Pair) object).variant.getFluid();
    }
    
    @Override
    public long getAmount(Object object) {
        return ((Pair) object).amount;
    }
    
    @Override
    public void setAmount(Object object, long amount) {
        ((Pair) object).amount = amount;
    }
    
    @Override
    public CompoundTag getTag(Object value) {
        return ((Pair) value).variant.getNbt();
    }
    
    @Override
    public void setTag(Object value, CompoundTag tag) {
        ((Pair) value).variant = FluidVariant.of(((Pair) value).variant.getFluid(), tag);
    }
    
    @Override
    public Object copy(Object value) {
        return new Pair(FluidVariant.of(((Pair) value).variant.getFluid(), ((Pair) value).variant.getNbt()), ((Pair) value).amount);
    }
    
    @Override
    public int hashCode(Object value) {
        var pair = (Pair) value;
        var code = 1;
        code = 31 * code + pair.variant.getFluid().hashCode();
        code = 31 * code + Long.hashCode(pair.amount);
        var tag = pair.variant.getNbt();
        if (tag != null)
            code = 31 * code + tag.hashCode();
        return code;
    }
}
