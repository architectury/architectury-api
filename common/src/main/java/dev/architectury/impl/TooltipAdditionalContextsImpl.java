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

package dev.architectury.impl;

import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TooltipAdditionalContextsImpl implements ClientTooltipEvent.AdditionalContexts {
    private static final ThreadLocal<TooltipAdditionalContextsImpl> INSTANCE_LOCAL = ThreadLocal.withInitial(TooltipAdditionalContextsImpl::new);
    
    public static ClientTooltipEvent.AdditionalContexts get() {
        return INSTANCE_LOCAL.get();
    }
    
    @Nullable
    private ItemStack item;
    
    @Override
    @Nullable
    public ItemStack getItem() {
        return item;
    }
    
    @Override
    public void setItem(@Nullable ItemStack item) {
        this.item = item;
    }
}
