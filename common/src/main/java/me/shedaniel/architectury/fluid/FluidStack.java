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

package me.shedaniel.architectury.fluid;

import me.shedaniel.architectury.hooks.FluidStackHooks;
import me.shedaniel.architectury.utils.Fraction;
import me.shedaniel.architectury.utils.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public final class FluidStack {
    private static final FluidStack EMPTY = create(Fluids.EMPTY, Fraction.zero());
    private Fraction amount;
    @Nullable
    private CompoundTag tag;
    private Supplier<Fluid> fluid;
    
    private FluidStack(Supplier<Fluid> fluid, Fraction amount, CompoundTag tag) {
        this.fluid = Objects.requireNonNull(fluid);
        this.amount = Objects.requireNonNull(amount);
        this.tag = tag == null ? null : tag.copy();
    }
    
    public static FluidStack empty() {
        return EMPTY;
    }
    
    public static FluidStack create(Fluid fluid, Fraction amount, @Nullable CompoundTag tag) {
        return create(() -> fluid, amount, tag);
    }
    
    public static FluidStack create(Fluid fluid, Fraction amount) {
        return create(fluid, amount, null);
    }
    
    public static FluidStack create(Supplier<Fluid> fluid, Fraction amount, @Nullable CompoundTag tag) {
        return new FluidStack(fluid, amount, tag);
    }
    
    public static FluidStack create(Supplier<Fluid> fluid, Fraction amount) {
        return create(fluid, amount, null);
    }
    
    public static FluidStack create(FluidStack stack, Fraction amount) {
        return create(stack.getRawFluidSupplier(), amount, stack.getTag());
    }
    
    public static Fraction bucketAmount() {
        return FluidStackHooks.bucketAmount();
    }
    
    public final Fluid getFluid() {
        return isEmpty() ? Fluids.EMPTY : getRawFluid();
    }
    
    @Nullable
    public final Fluid getRawFluid() {
        return fluid.get();
    }
    
    public final Supplier<Fluid> getRawFluidSupplier() {
        return fluid;
    }
    
    public boolean isEmpty() {
        return getRawFluid() == Fluids.EMPTY || !amount.isGreaterThan(Fraction.zero());
    }
    
    public Fraction getAmount() {
        return isEmpty() ? Fraction.zero() : amount;
    }
    
    public void setAmount(Fraction amount) {
        this.amount = Objects.requireNonNull(amount);
    }
    
    public void grow(Fraction amount) {
        setAmount(this.amount.add(amount));
    }
    
    public void shrink(Fraction amount) {
        setAmount(this.amount.minus(amount));
    }
    
    public boolean hasTag() {
        return tag != null;
    }
    
    @Nullable
    public CompoundTag getTag() {
        return tag;
    }
    
    public void setTag(@Nullable CompoundTag tag) {
        this.tag = tag;
    }
    
    public CompoundTag getOrCreateTag() {
        if (tag == null)
            setTag(new CompoundTag());
        return tag;
    }
    
    @Nullable
    public CompoundTag getChildTag(String childName) {
        if (tag == null)
            return null;
        return tag.getCompound(childName);
    }
    
    public CompoundTag getOrCreateChildTag(String childName) {
        getOrCreateTag();
        CompoundTag child = tag.getCompound(childName);
        if (!tag.contains(childName, NbtType.COMPOUND)) {
            tag.put(childName, child);
        }
        return child;
    }
    
    public void removeChildTag(String childName) {
        if (tag != null)
            tag.remove(childName);
    }
    
    public Component getName() {
        return FluidStackHooks.getName(this);
    }
    
    public String getTranslationKey() {
        return FluidStackHooks.getTranslationKey(this);
    }
    
    public FluidStack copy() {
        return new FluidStack(fluid, amount, tag);
    }
    
    @Override
    public final int hashCode() {
        int code = 1;
        code = 31 * code + getFluid().hashCode();
        code = 31 * code + amount.hashCode();
        if (tag != null)
            code = 31 * code + tag.hashCode();
        return code;
    }
    
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof FluidStack)) {
            return false;
        }
        return isFluidStackEqual((FluidStack) o);
    }
    
    public boolean isFluidStackEqual(FluidStack other) {
        return getFluid() == other.getFluid() && getAmount().equals(other.getAmount()) && isTagEqual(other);
    }
    
    private boolean isTagEqual(FluidStack other) {
        return tag == null ? other.tag == null : other.tag != null && tag.equals(other.tag);
    }
    
    public static FluidStack read(FriendlyByteBuf buf) {
        return FluidStackHooks.read(buf);
    }
    
    public static FluidStack read(CompoundTag tag) {
        return FluidStackHooks.read(tag);
    }
    
    public void write(FriendlyByteBuf buf) {
        FluidStackHooks.write(this, buf);
    }
    
    public CompoundTag write(CompoundTag tag) {
        return FluidStackHooks.write(this, tag);
    }
}
