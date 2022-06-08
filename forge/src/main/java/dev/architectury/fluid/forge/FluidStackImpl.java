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

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;
import java.util.function.Supplier;

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
    public FluidStack create(Supplier<Fluid> fluid, long amount, CompoundTag tag) {
        return new FluidStack(fluid.get(), toInt(amount), tag);
    }
    
    @Override
    public Supplier<Fluid> getRawFluidSupplier(FluidStack object) {
        return ForgeRegistries.FLUIDS.getDelegateOrThrow(object.getRawFluid());
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
    public CompoundTag getTag(FluidStack value) {
        return value.getTag();
    }
    
    @Override
    public void setTag(FluidStack value, CompoundTag tag) {
        value.setTag(tag);
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
        var tag = value.getTag();
        if (tag != null)
            code = 31 * code + tag.hashCode();
        return code;
    }
}
