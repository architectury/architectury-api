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

import com.mojang.serialization.Codec;
import dev.architectury.hooks.fluid.FluidStackHooks;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.*;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class FluidStack implements DataComponentHolder {
    private static final FluidStackAdapter<Object> ADAPTER = adapt(FluidStack::getValue, FluidStack::new);
    private static final FluidStack EMPTY = new FluidStack(() -> Fluids.EMPTY, 0, DataComponentPatch.EMPTY);
    public static final Codec<FluidStack> CODEC = ADAPTER.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidStack> STREAM_CODEC = ADAPTER.streamCodec();
    
    private final Object value;
    
    private FluidStack(Supplier<Fluid> fluid, long amount, DataComponentPatch patch) {
        this(ADAPTER.create(fluid, amount, patch));
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
        T create(Supplier<Fluid> fluid, long amount, @Nullable DataComponentPatch patch);
        
        Supplier<Fluid> getRawFluidSupplier(T object);
        
        Fluid getFluid(T object);
        
        long getAmount(T object);
        
        void setAmount(T object, long amount);
        
        DataComponentPatch getPatch(T value);
        
        PatchedDataComponentMap getComponents(T value);
        
        void applyComponents(T value, DataComponentPatch patch);
        
        void applyComponents(T value, DataComponentMap patch);
        
        @Nullable <D> D set(T value, DataComponentType<? super D> type, @Nullable D component);
        
        @Nullable <D> D remove(T value, DataComponentType<? extends D> type);
        
        @Nullable <D> D update(T value, DataComponentType<D> type, D component, UnaryOperator<D> updater);
        
        @Nullable <D, U> D update(T value, DataComponentType<D> type, D component, U updateContext, BiFunction<D, U, D> updater);
        
        T copy(T value);
        
        int hashCode(T value);
        
        Codec<FluidStack> codec();
        
        StreamCodec<RegistryFriendlyByteBuf, FluidStack> streamCodec();
    }
    
    public static FluidStack empty() {
        return EMPTY;
    }
    
    public static FluidStack create(Fluid fluid, long amount, DataComponentPatch patch) {
        if (fluid == Fluids.EMPTY || amount <= 0) return empty();
        return create(() -> fluid, amount, patch);
    }
    
    public static FluidStack create(Fluid fluid, long amount) {
        return create(fluid, amount, DataComponentPatch.EMPTY);
    }
    
    public static FluidStack create(Supplier<Fluid> fluid, long amount, DataComponentPatch patch) {
        if (amount <= 0) return empty();
        return new FluidStack(fluid, amount, patch);
    }
    
    public static FluidStack create(Supplier<Fluid> fluid, long amount) {
        return create(fluid, amount, DataComponentPatch.EMPTY);
    }
    
    public static FluidStack create(Holder<Fluid> fluid, long amount, DataComponentPatch patch) {
        return create(fluid.value(), amount, patch);
    }
    
    public static FluidStack create(Holder<Fluid> fluid, long amount) {
        return create(fluid.value(), amount, DataComponentPatch.EMPTY);
    }
    
    public static FluidStack create(FluidStack stack, long amount) {
        return create(stack.getRawFluidSupplier(), amount, stack.getPatch());
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
    
    public DataComponentPatch getPatch() {
        return ADAPTER.getPatch(value);
    }
    
    @Override
    public PatchedDataComponentMap getComponents() {
        return ADAPTER.getComponents(value);
    }
    
    public void applyComponents(DataComponentPatch patch) {
        ADAPTER.applyComponents(value, patch);
    }
    
    public void applyComponents(DataComponentMap patch) {
        ADAPTER.applyComponents(value, patch);
    }
    
    @Nullable
    public <T> T set(DataComponentType<? super T> type, @Nullable T component) {
        return ADAPTER.set(value, type, component);
    }
    
    @Nullable
    public <T> T remove(DataComponentType<? extends T> type) {
        return ADAPTER.remove(value, type);
    }
    
    @Nullable
    public <T> T update(DataComponentType<T> type, T component, UnaryOperator<T> updater) {
        return ADAPTER.update(value, type, component, updater);
    }
    
    @Nullable
    public <T, U> T update(DataComponentType<T> type, T component, U updateContext, BiFunction<T, U, T> updater) {
        return ADAPTER.update(value, type, component, updateContext, updater);
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
        return getFluid() == other.getFluid() && getAmount() == other.getAmount() && isComponentEqual(other);
    }
    
    public boolean isFluidEqual(FluidStack other) {
        return getFluid() == other.getFluid();
    }
    
    public boolean isComponentEqual(FluidStack other) {
        var patch = getPatch();
        var otherPatch = other.getPatch();
        return Objects.equals(patch, otherPatch);
    }
    
    public static FluidStack read(RegistryFriendlyByteBuf buf) {
        return FluidStackHooks.read(buf);
    }
    
    public static Optional<FluidStack> read(HolderLookup.Provider provider, Tag tag) {
        return FluidStackHooks.read(provider, tag);
    }
    
    public void write(RegistryFriendlyByteBuf buf) {
        FluidStackHooks.write(this, buf);
    }
    
    public Tag write(HolderLookup.Provider provider, Tag tag) {
        return FluidStackHooks.write(provider, this, tag);
    }
    
    public FluidStack copyWithAmount(long amount) {
        if (isEmpty()) return this;
        return new FluidStack(getRawFluidSupplier(), amount, getPatch());
    }
    
    @ApiStatus.Internal
    public static void init() {
        // classloading my beloved 😍
        // please don't use this by the way
    }
}
