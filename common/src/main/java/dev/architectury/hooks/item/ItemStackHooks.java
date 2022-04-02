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

package dev.architectury.hooks.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.CarriedBlocks;

public final class ItemStackHooks {
    private ItemStackHooks() {
    }
    
    public static ItemStack copyWithCount(ItemStack stack, int count) {
        var copy = stack.copy();
        copy.setCount(count);
        return copy;
    }
    
    public static void giveItem(ServerPlayer player, ItemStack stack) {
        if (player.getCarried() == LivingEntity.Carried.NONE && stack.getCount() == 1) {
            player.setCarriedBlock(CarriedBlocks.getBlockFromItemStack(stack).orElse(null));
        } else if (!stack.isEmpty()) {
            player.drop(stack, false);
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F,
                    ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }
    }
}
