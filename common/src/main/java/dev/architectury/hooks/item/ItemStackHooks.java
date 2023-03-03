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

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

public final class ItemStackHooks {
    private ItemStackHooks() {
    }
    
    public static ItemStack copyWithCount(ItemStack stack, int count) {
        var copy = stack.copy();
        copy.setCount(count);
        return copy;
    }
    
    public static void giveItem(ServerPlayer player, ItemStack stack) {
        var bl = player.getInventory().add(stack);
        if (bl && stack.isEmpty()) {
            stack.setCount(1);
            var entity = player.drop(stack, false);
            if (entity != null) {
                entity.makeFakeItem();
            }
            
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.inventoryMenu.broadcastChanges();
        } else {
            var entity = player.drop(stack, false);
            if (entity != null) {
                entity.setNoPickUpDelay();
                entity.setTarget(player.getUUID());
            }
        }
    }
    
    /**
     * Returns whether the given item stack has a remaining item after crafting.
     * This method is stack-aware only on Forge.
     *
     * @param stack the item stack
     * @return whether the given item stack has a remaining item after crafting
     */
    @ExpectPlatform
    public static boolean hasCraftingRemainingItem(ItemStack stack) {
        throw new AssertionError();
    }
    
    /**
     * Returns the remaining item for a given item stack after crafting.
     * This method is stack-aware only on Forge.
     *
     * @param stack the item stack
     * @return the remaining item for a given item stack after crafting
     */
    @ExpectPlatform
    public static ItemStack getCraftingRemainingItem(ItemStack stack) {
        throw new AssertionError();
    }
}
