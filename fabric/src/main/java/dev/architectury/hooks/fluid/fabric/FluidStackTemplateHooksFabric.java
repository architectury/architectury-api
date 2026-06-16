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

package dev.architectury.hooks.fluid.fabric;

import dev.architectury.fluid.FluidStackTemplate;
import dev.architectury.fluid.fabric.FluidStackTemplateImpl;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.minecraft.core.component.PatchedDataComponentMap;

public final class FluidStackTemplateHooksFabric {
    private FluidStackTemplateHooksFabric() {
    }
    
    public static FluidStackTemplate fromFabric(StorageView<FluidVariant> storageView) {
        return fromFabric(storageView.getResource(), storageView.getAmount());
    }

    public static FluidStackTemplate fromFabric(FluidVariant variant, long amount) {
        return FluidStackTemplateImpl.fromValue.apply(new FluidStackTemplateImpl.Pair(variant.getFluid(), new PatchedDataComponentMap(variant.getComponents()), amount));
    }

    public static FluidVariant toFabric(FluidStackTemplate stack) {
        return ((FluidStackTemplateImpl.Pair) FluidStackTemplateImpl.toValue.apply(stack)).toVariant();
    }
}