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

package dev.architectury.fluid.forge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;
import java.util.function.Supplier;

@ApiStatus.Internal
public enum FluidStackImpl implements dev.architectury.fluid.FluidStack.FluidStackAdapter {
    INSTANCE;
    
    public static Function<dev.architectury.fluid.FluidStack, Object> toValue;
    public static Function<Object, dev.architectury.fluid.FluidStack> fromValue;
    
    public static dev.architectury.fluid.FluidStack.FluidStackAdapter adapt(Function<dev.architectury.fluid.FluidStack, Object> toValue, Function<Object, dev.architectury.fluid.FluidStack> fromValue) {
        FluidStackImpl.toValue = toValue;
        FluidStackImpl.fromValue = fromValue;
        return INSTANCE;
    }
    
    @Override
    public Object create(Supplier<Fluid> fluid, long amount, CompoundTag tag) {
        return new FluidStack(fluid.get(), (int) amount, tag);
    }
    
    @Override
    public void check(Object object) {
        if (!(object instanceof FluidStack)) {
            throw new IllegalArgumentException("Expected FluidStack, got " + object.getClass().getName());
        }
    }
    
    @Override
    public Supplier<Fluid> getRawFluidSupplier(Object object) {
        return ((FluidStack) object).getRawFluid().delegate;
    }
    
    @Override
    public Fluid getFluid(Object object) {
        return ((FluidStack) object).getFluid();
    }
    
    @Override
    public long getAmount(Object object) {
        return ((FluidStack) object).getAmount();
    }
    
    @Override
    public void setAmount(Object object, long amount) {
        ((FluidStack) object).setAmount((int) amount);
    }
    
    @Override
    public CompoundTag getTag(Object value) {
        return ((FluidStack) value).getTag();
    }
    
    @Override
    public void setTag(Object value, CompoundTag tag) {
        ((FluidStack) value).setTag(tag);
    }
    
    @Override
    public Object copy(Object value) {
        return ((FluidStack) value).copy();
    }
    
    @Override
    public int hashCode(Object value) {
        var stack = (FluidStack) value;
        var code = 1;
        code = 31 * code + stack.getFluid().hashCode();
        code = 31 * code + stack.getAmount();
        var tag = stack.getTag();
        if (tag != null)
            code = 31 * code + tag.hashCode();
        return code;
    }
}
