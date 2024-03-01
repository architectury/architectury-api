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

import com.google.common.collect.Iterators;
import com.mojang.serialization.Codec;
import dev.architectury.hooks.fluid.FluidStackHooks;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.core.component.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class FluidStack implements DataComponentHolder {
    private static final FluidStackAdapter<Object> ADAPTER = adapt(FluidStack::getValue, FluidStack::new);
    private static final FluidStack EMPTY = create(Fluids.EMPTY, 0);
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
    
    @Override
    public DataComponentMap getComponents() {
        return new DataComponentMap() {
            @Nullable
            @Override
            public <T> T get(DataComponentType<? extends T> type) {
                return getPatch().get(type).orElse(null);
            }
            
            @Override
            public Set<DataComponentType<?>> keySet() {
                return new AbstractSet<>() {
                    @Override
                    public Iterator<DataComponentType<?>> iterator() {
                        return Iterators.transform(getPatch().entrySet().iterator(), Map.Entry::getKey);
                    }
                    
                    @Override
                    public int size() {
                        return getPatch().entrySet().size();
                    }
                    
                    @Override
                    public boolean contains(Object o) {
                        if (!(o instanceof DataComponentType<?> type)) return false;
                        return getPatch().get(type).isPresent();
                    }
                };
            }
        };
    }
    
    public <T> T set(DataComponentType<? super T> dataComponentType, @Nullable T object) {
        T previous = (T) get(dataComponentType);
        DataComponentPatch.Builder builder = DataComponentPatch.builder();
        for (TypedDataComponent<?> component : getComponents()) {
            if (component.type() != dataComponentType) {
                builder.set(component);
            }
        }
        if (object != null) {
            builder.set(dataComponentType, object);
        }
        setPatch(builder.build());
        return previous;
    }
    
    @Nullable
    public <T, U> T update(DataComponentType<T> dataComponentType, T object, U object2, BiFunction<T, U, T> biFunction) {
        return this.set(dataComponentType, biFunction.apply(this.getOrDefault(dataComponentType, object), object2));
    }
    
    @Nullable
    public <T> T update(DataComponentType<T> dataComponentType, T object, UnaryOperator<T> unaryOperator) {
        return this.set(dataComponentType, unaryOperator.apply(this.getOrDefault(dataComponentType, object)));
    }
    
    @Nullable
    public <T> T remove(DataComponentType<? extends T> dataComponentType) {
        return this.set(dataComponentType, null);
    }
    
    public void applyComponents(DataComponentPatch dataComponentPatch) {
        DataComponentPatch.Builder builder = DataComponentPatch.builder();
        for (TypedDataComponent<?> component : getComponents()) {
            builder.set(component);
        }
        for (Map.Entry<DataComponentType<?>, Optional<?>> entry : dataComponentPatch.entrySet()) {
            if (entry.getValue().isPresent()) {
                //noinspection rawtypes
                builder.set((DataComponentType) entry.getKey(), entry.getValue().get());
            } else {
                builder.remove(entry.getKey());
            }
        }
        setPatch(builder.build());
    }
    
    public void applyComponents(DataComponentMap dataComponentMap) {
        DataComponentPatch.Builder builder = DataComponentPatch.builder();
        for (TypedDataComponent<?> component : getComponents()) {
            builder.set(component);
        }
        for (TypedDataComponent<?> entry : dataComponentMap) {
            if (entry.value() != null) {
                //noinspection rawtypes
                builder.set((DataComponentType) entry.type(), entry.value());
            } else {
                builder.remove(entry.type());
            }
        }
        setPatch(builder.build());
    }
    
    @ApiStatus.Internal
    public interface FluidStackAdapter<T> {
        T create(Supplier<Fluid> fluid, long amount, @Nullable DataComponentPatch patch);
        
        Supplier<Fluid> getRawFluidSupplier(T object);
        
        Fluid getFluid(T object);
        
        long getAmount(T object);
        
        void setAmount(T object, long amount);
        
        DataComponentPatch getPatch(T value);
        
        void setPatch(T value, DataComponentPatch patch);
        
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
    
    public void setPatch(DataComponentPatch patch) {
        ADAPTER.setPatch(value, patch);
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
        return new FluidStack(getRawFluidSupplier(), amount, getPatch());
    }
    
    @ApiStatus.Internal
    public static void init() {
        // classloading my beloved 😍
        // please don't use this by the way
    }
}
