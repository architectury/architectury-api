/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    private Fraction amount;
    @Nullable
    private CompoundTag tag;
    private Supplier<Fluid> fluid;
    
    private FluidStack(Supplier<Fluid> fluid, Fraction amount, CompoundTag tag) {
        this.fluid = Objects.requireNonNull(fluid);
        this.amount = Objects.requireNonNull(amount);
        this.tag = tag == null ? null : tag.copy();
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
