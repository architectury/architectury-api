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

package dev.architectury.test.registry.objects;

import dev.architectury.extensions.ItemExtension;
import dev.architectury.test.TestMod;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EquippableTickingItem extends Item implements ItemExtension {
    public EquippableTickingItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public void tickArmor(ItemStack stack, Player player) {
        TestMod.SINK.accept("Ticking " + new TranslatableComponent(stack.getDescriptionId()).getString());
    }
    
    @Nullable
    @Override
    public EquipmentSlot getCustomEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }
}
