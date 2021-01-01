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

package me.shedaniel.architectury.hooks.fabric;

import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.utils.Env;
import me.shedaniel.architectury.utils.Fraction;
import me.shedaniel.architectury.utils.NbtType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.Objects;

public class FluidStackHooksImpl {
    public static Component getName(FluidStack stack) {
        if (Platform.getEnvironment() == Env.CLIENT) {
            return getNameClient(stack);
        }
        
        return new TranslatableComponent(getTranslationKey(stack));
    }
    
    @Environment(EnvType.CLIENT)
    private static Component getNameClient(FluidStack stack) {
        return stack.getFluid().defaultFluidState().createLegacyBlock().getBlock().getName();
    }
    
    public static String getTranslationKey(FluidStack stack) {
        ResourceLocation id = Registry.FLUID.getKey(stack.getFluid());
        return "block." + id.getNamespace() + "." + id.getPath();
    }
    
    public static FluidStack read(FriendlyByteBuf buf) {
        Fluid fluid = Objects.requireNonNull(Registry.FLUID.get(buf.readResourceLocation()));
        Fraction amount = Fraction.of(buf.readVarLong(), buf.readVarLong());
        CompoundTag tag = buf.readNbt();
        if (fluid == Fluids.EMPTY) return FluidStack.empty();
        return FluidStack.create(fluid, amount, tag);
    }
    
    public static void write(FluidStack stack, FriendlyByteBuf buf) {
        buf.writeResourceLocation(Registry.FLUID.getKey(stack.getFluid()));
        buf.writeVarLong(stack.getAmount().getNumerator());
        buf.writeVarLong(stack.getAmount().getDenominator());
        buf.writeNbt(stack.getTag());
    }
    
    public static FluidStack read(CompoundTag tag) {
        if (tag == null || !tag.contains("id", NbtType.STRING)) {
            return FluidStack.empty();
        }
        
        Fluid fluid = Registry.FLUID.get(new ResourceLocation(tag.getString("id")));
        if (fluid == null || fluid == Fluids.EMPTY) {
            return FluidStack.empty();
        }
        long numerator = tag.getLong("numerator");
        long denominator = tag.getLong("denominator");
        FluidStack stack = FluidStack.create(fluid, Fraction.of(numerator, denominator));
        
        if (tag.contains("tag", NbtType.COMPOUND)) {
            stack.setTag(tag.getCompound("tag"));
        }
        return stack;
    }
    
    public static CompoundTag write(FluidStack stack, CompoundTag tag) {
        tag.putString("id", Registry.FLUID.getKey(stack.getFluid()).toString());
        tag.putLong("numerator", stack.getAmount().getNumerator());
        tag.putLong("denominator", stack.getAmount().getDenominator());
        if (stack.hasTag()) {
            tag.put("tag", stack.getTag());
        }
        return tag;
    }
    
    public static Fraction bucketAmount() {
        return Fraction.ofWhole(1);
    }
}
