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

import com.mojang.serialization.Codec;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static dev.architectury.utils.Amount.toInt;

@ApiStatus.Internal
public enum FluidStackImpl implements dev.architectury.fluid.FluidStack.FluidStackAdapter<FluidStack> {
    INSTANCE;
    private static final String ARCHITECTURY_COMPONENTS_TAG = "ArchitecturyDataComponents";

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
    public FluidStack create(Supplier<Fluid> fluid, long amount, @Nullable DataComponentPatch patch) {
        FluidStack stack = new FluidStack(fluid.get(), toInt(amount));
        writePatch(stack, patch);
        return stack;
    }

    @Override
    public Supplier<Fluid> getRawFluidSupplier(FluidStack object) {
        return object::getFluid;
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
    public DataComponentPatch getPatch(FluidStack value) {
        CompoundTag tag = value.getTag();
        if (tag == null || !tag.contains(ARCHITECTURY_COMPONENTS_TAG)) {
            return DataComponentPatch.EMPTY;
        }

        return tag.read(ARCHITECTURY_COMPONENTS_TAG, DataComponentPatch.CODEC).orElse(DataComponentPatch.EMPTY);
    }

    @Override
    public PatchedDataComponentMap getComponents(FluidStack value) {
        return PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, getPatch(value));
    }

    @Override
    public void applyComponents(FluidStack value, DataComponentPatch patch) {
        PatchedDataComponentMap components = getComponents(value);
        components.applyPatch(patch);
        writePatch(value, components.asPatch());
    }

    @Override
    public void applyComponents(FluidStack value, DataComponentMap components) {
        PatchedDataComponentMap patchedComponents = getComponents(value);
        patchedComponents.setAll(components);
        writePatch(value, patchedComponents.asPatch());
    }

    @Override
    @Nullable
    public <D> D set(FluidStack value, DataComponentType<D> type, @Nullable D component) {
        if (component == null) {
            return remove(value, type);
        }

        PatchedDataComponentMap components = getComponents(value);
        D oldComponent = components.set(type, component);
        writePatch(value, components.asPatch());
        return oldComponent;
    }

    @Override
    @Nullable
    public <D> D remove(FluidStack value, DataComponentType<? extends D> type) {
        PatchedDataComponentMap components = getComponents(value);
        D oldComponent = components.remove(type);
        writePatch(value, components.asPatch());
        return oldComponent;
    }

    @Override
    @Nullable
    public <D> D update(FluidStack value, DataComponentType<D> type, D component, UnaryOperator<D> updater) {
        return set(value, type, updater.apply(component));
    }

    @Override
    @Nullable
    public <D, U> D update(FluidStack value, DataComponentType<D> type, D component, U updateContext, BiFunction<D, U, D> updater) {
        return set(value, type, updater.apply(component, updateContext));
    }

    private static void writePatch(FluidStack value, @Nullable DataComponentPatch patch) {
        CompoundTag tag = value.getTag();
        if (tag != null) {
            tag.remove(ARCHITECTURY_COMPONENTS_TAG);
        }

        if (patch == null || patch.isEmpty()) {
            if (tag != null && tag.isEmpty()) {
                value.setTag(null);
            }
            return;
        }

        tag = value.getOrCreateTag();
        tag.store(ARCHITECTURY_COMPONENTS_TAG, DataComponentPatch.CODEC, patch);
    }

    @Override
    public FluidStack copy(FluidStack value) {
        return value.copy();
    }

    @Override
    public int hashCode(FluidStack value) {
        int code = 1;
        code = 31 * code + value.getFluid().hashCode();
        code = 31 * code + value.getAmount();
        if (value.getTag() != null) {
            code = 31 * code + value.getTag().hashCode();
        }
        return code;
    }

    @Override
    public Codec<dev.architectury.fluid.FluidStack> codec() {
        return FluidStack.CODEC.xmap(FluidStackHooksForge::fromForge, FluidStackHooksForge::toForge);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, dev.architectury.fluid.FluidStack> streamCodec() {
        return ByteBufCodecs.fromCodecWithRegistries(FluidStack.CODEC, NbtAccounter::unlimitedHeap)
                .map(FluidStackHooksForge::fromForge, FluidStackHooksForge::toForge);
    }
}
