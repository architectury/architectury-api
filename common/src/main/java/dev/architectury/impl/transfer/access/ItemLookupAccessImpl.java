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

package dev.architectury.impl.transfer.access;

import dev.architectury.transfer.access.ItemLookup;
import dev.architectury.transfer.access.ItemLookupAccess;
import dev.architectury.transfer.access.ItemLookupRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ItemLookupAccessImpl<T, C> implements ItemLookupAccess<T, C> {
    private final List<ItemLookup<T, C>> lookups = new ArrayList<>();
    private final List<ItemLookupRegistration<T, C>> registrationHandlers = new ArrayList<>();
    
    @Override
    public void addQueryHandler(ItemLookup<T, C> handler) {
        this.lookups.add(handler);
    }
    
    @Override
    public void addRegistrationHandler(ItemLookupRegistration<T, C> registration) {
        this.registrationHandlers.add(registration);
    }
    
    @Override
    @Nullable
    public T get(ItemStack stack, C context) {
        for (ItemLookup<T, C> handler : lookups) {
            T result = handler.get(stack, context);
            if (result != null) {
                return result;
            }
        }
        
        return null;
    }
    
    @Override
    public boolean register(ResourceLocation id, ItemAccessProvider<Function<C, T>> provider) {
        for (ItemLookupRegistration<T, C> handler : registrationHandlers) {
            if (handler.register(id, provider)) {
                return true;
            }
        }
        
        return false;
    }
}
