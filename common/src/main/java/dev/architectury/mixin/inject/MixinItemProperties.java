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

package dev.architectury.mixin.inject;

import dev.architectury.extensions.injected.InjectedItemPropertiesExtension;
import dev.architectury.impl.ItemPropertiesExtensionImpl;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Item.Properties.class)
public class MixinItemProperties implements InjectedItemPropertiesExtension, ItemPropertiesExtensionImpl {
    @Unique
    private CreativeModeTab tab;
    
    @Override
    public Item.Properties arch$tab(CreativeModeTab tab) {
        this.tab = tab;
        return (Item.Properties) (Object) this;
    }
    
    @Override
    @Nullable
    public CreativeModeTab arch$getTab() {
        return tab;
    }
}
