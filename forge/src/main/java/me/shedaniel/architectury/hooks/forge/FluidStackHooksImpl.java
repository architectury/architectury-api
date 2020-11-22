/*
 * This file is part of architectury.
 * Copyright (C) 2020 shedaniel
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

package me.shedaniel.architectury.hooks.forge;

import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

public class FluidStackHooksImpl {
    public static ITextComponent getName(FluidStack stack) {
        return stack.getFluid().getAttributes().getDisplayName(
                new net.minecraftforge.fluids.FluidStack(stack.getRawFluid(), stack.getAmount().intValue(), stack.getTag()));
    }
    
    public static String getTranslationKey(FluidStack stack) {
        return stack.getFluid().getAttributes().getTranslationKey(
                new net.minecraftforge.fluids.FluidStack(stack.getRawFluid(), stack.getAmount().intValue(), stack.getTag()));
    }
    
    public static FluidStack read(PacketBuffer buf) {
        net.minecraftforge.fluids.FluidStack stack = net.minecraftforge.fluids.FluidStack.readFromPacket(buf);
        return FluidStack.create(stack.getFluid().delegate, Fraction.ofWhole(stack.getAmount()), stack.getTag());
    }
    
    public static void write(FluidStack stack, PacketBuffer buf) {
        new net.minecraftforge.fluids.FluidStack(stack.getRawFluid(), stack.getAmount().intValue(), stack.getTag()).writeToPacket(buf);
    }
    
    public static FluidStack read(CompoundNBT tag) {
        net.minecraftforge.fluids.FluidStack stack = net.minecraftforge.fluids.FluidStack.loadFluidStackFromNBT(tag);
        return FluidStack.create(stack.getFluid().delegate, Fraction.ofWhole(stack.getAmount()), stack.getTag());
    }
    
    public static CompoundNBT write(FluidStack stack, CompoundNBT tag) {
        return new net.minecraftforge.fluids.FluidStack(stack.getRawFluid(), stack.getAmount().intValue(), stack.getTag()).writeToNBT(tag);
    }
    
    public static Fraction bucketAmount() {
        return Fraction.ofWhole(1000);
    }
}
