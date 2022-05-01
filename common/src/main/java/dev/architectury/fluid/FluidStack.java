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

package dev.architectury.fluid;

import dev.architectury.hooks.fluid.FluidStackHooks;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class FluidStack {
    private static final FluidStackAdapter<Object> ADAPTER = adapt(FluidStack::getValue, FluidStack::new);
    private static final FluidStack EMPTY = create(Fluids.EMPTY, 0);
    
    private Object value;
    
    private FluidStack(Supplier<Fluid> fluid, long amount, CompoundTag tag) {
        this(ADAPTER.create(fluid, amount, tag));
    }
    
    private FluidStack(Object value) {
        this.value = Objects.requireNonNull(value);
    }
    
    private Object getValue() {
        return value;
    }
    
    @ExpectPlatform
    private static FluidStackAdapter<Object> adapt(Function<FluidStack, Object> toValue, Function<Object, FluidStack> fromValue) {
        throw new AssertionError();
    }
    
    @ApiStatus.Internal
    public interface FluidStackAdapter<T> {
        T create(Supplier<Fluid> fluid, long amount, CompoundTag tag);
        
        Supplier<Fluid> getRawFluidSupplier(T object);
        
        Fluid getFluid(T object);
        
        long getAmount(T object);
        
        void setAmount(T object, long amount);
        
        CompoundTag getTag(T value);
        
        void setTag(T value, CompoundTag tag);
        
        T copy(T value);
        
        int hashCode(T value);
    }
    
    public static FluidStack empty() {
        return EMPTY;
    }
    
    public static FluidStack create(Fluid fluid, long amount, @Nullable CompoundTag tag) {
        return create(() -> fluid, amount, tag);
    }
    
    public static FluidStack create(Fluid fluid, long amount) {
        return create(fluid, amount, null);
    }
    
    public static FluidStack create(Supplier<Fluid> fluid, long amount, @Nullable CompoundTag tag) {
        return new FluidStack(fluid, amount, tag);
    }
    
    public static FluidStack create(Supplier<Fluid> fluid, long amount) {
        return create(fluid, amount, null);
    }
    
    public static FluidStack create(FluidStack stack, long amount) {
        return create(stack.getRawFluidSupplier(), amount, stack.getTag());
    }
    
    public static long bucketAmount() {
        return FluidStackHooks.bucketAmount();
    }
    
    public Fluid getFluid() {
        return isEmpty() ? Fluids.EMPTY : getRawFluid();
    }
    
    @Nullable
    public Fluid getRawFluid() {
        return ADAPTER.getFluid(value);
    }
    
    public Supplier<Fluid> getRawFluidSupplier() {
        return ADAPTER.getRawFluidSupplier(value);
    }
    
    public boolean isEmpty() {
        return getRawFluid() == Fluids.EMPTY || ADAPTER.getAmount(value) <= 0;
    }
    
    public long getAmount() {
        return isEmpty() ? 0 : ADAPTER.getAmount(value);
    }
    
    public void setAmount(long amount) {
        ADAPTER.setAmount(value, amount);
    }
    
    public void grow(long amount) {
        setAmount(getAmount() + amount);
    }
    
    public void shrink(long amount) {
        setAmount(getAmount() - amount);
    }
    
    public boolean hasTag() {
        return getTag() != null;
    }
    
    @Nullable
    public CompoundTag getTag() {
        return ADAPTER.getTag(value);
    }
    
    public void setTag(@Nullable CompoundTag tag) {
        ADAPTER.setTag(value, tag);
    }
    
    public CompoundTag getOrCreateTag() {
        CompoundTag tag = getTag();
        if (tag == null) {
            tag = new CompoundTag();
            setTag(tag);
            return tag;
        }
        return tag;
    }
    
    @Nullable
    public CompoundTag getChildTag(String childName) {
        CompoundTag tag = getTag();
        if (tag == null)
            return null;
        return tag.getCompound(childName);
    }
    
    public CompoundTag getOrCreateChildTag(String childName) {
        CompoundTag tag = getOrCreateTag();
        var child = tag.getCompound(childName);
        if (!tag.contains(childName, Tag.TAG_COMPOUND)) {
            tag.put(childName, child);
        }
        return child;
    }
    
    public void removeChildTag(String childName) {
        CompoundTag tag = getTag();
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
        return new FluidStack(ADAPTER.copy(value));
    }
    
    @Override
    public int hashCode() {
        return ADAPTER.hashCode(value);
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FluidStack)) {
            return false;
        }
        return isFluidStackEqual((FluidStack) o);
    }
    
    public boolean isFluidStackEqual(FluidStack other) {
        return getFluid() == other.getFluid() && getAmount() == other.getAmount() && isTagEqual(other);
    }
    
    public boolean isFluidEqual(FluidStack other) {
        return getFluid() == other.getFluid();
    }
    
    public boolean isTagEqual(FluidStack other) {
        var tag = getTag();
        var otherTag = other.getTag();
        return Objects.equals(tag, otherTag);
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
    
    public FluidStack copyWithAmount(long amount) {
        if (isEmpty()) return this;
        return new FluidStack(getRawFluidSupplier(), amount, getTag());
    }
}
