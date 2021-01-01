/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class FluidStackHooksImpl {
    public static Component getName(FluidStack stack) {
        return stack.getFluid().getAttributes().getDisplayName(FluidStackHooksForge.toForge(stack));
    }
    
    public static String getTranslationKey(FluidStack stack) {
        return stack.getFluid().getAttributes().getTranslationKey(FluidStackHooksForge.toForge(stack));
    }
    
    public static FluidStack read(FriendlyByteBuf buf) {
        return FluidStackHooksForge.fromForge(net.minecraftforge.fluids.FluidStack.readFromPacket(buf));
    }
    
    public static void write(FluidStack stack, FriendlyByteBuf buf) {
        FluidStackHooksForge.toForge(stack).writeToPacket(buf);
    }
    
    public static FluidStack read(CompoundTag tag) {
        return FluidStackHooksForge.fromForge(net.minecraftforge.fluids.FluidStack.loadFluidStackFromNBT(tag));
    }
    
    public static CompoundTag write(FluidStack stack, CompoundTag tag) {
        return FluidStackHooksForge.toForge(stack).writeToNBT(tag);
    }
    
    public static Fraction bucketAmount() {
        return Fraction.ofWhole(1000);
    }
}
