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

package me.shedaniel.architectury.hooks.forge;

import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;

public final class FluidStackHooksForge {
    private FluidStackHooksForge() {
    }
    
    public static FluidStack fromForge(net.minecraftforge.fluids.FluidStack stack) {
        return FluidStack.create(stack.getFluid().delegate, Fraction.ofWhole(stack.getAmount()), stack.getTag());
    }
    
    public static net.minecraftforge.fluids.FluidStack toForge(FluidStack stack) {
        return new net.minecraftforge.fluids.FluidStack(stack.getRawFluid(), stack.getAmount().intValue(), stack.getTag());
    }
}
