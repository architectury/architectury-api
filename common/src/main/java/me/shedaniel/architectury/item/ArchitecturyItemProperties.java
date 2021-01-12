/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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

package me.shedaniel.architectury.item;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import me.shedaniel.architectury.registry.ToolType;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class ArchitecturyItemProperties {
    private final Item.Properties properties;

    public ArchitecturyItemProperties() {
        properties = ArchitecturyItemPropertiesPlatform.getPlatformProperties();
    }

    public Item.Properties build() {
        return this.properties;
    }

    // vanilla item properties

    public ArchitecturyItemProperties food(FoodProperties foodProperties) {
        properties.food(foodProperties);
        return this;
    }

    public ArchitecturyItemProperties stacksTo(int i) {
        properties.stacksTo(i);
        return this;
    }

    public ArchitecturyItemProperties defaultDurability(int i) {
        properties.defaultDurability(i);
        return this;
    }

    public ArchitecturyItemProperties durability(int i) {
        properties.durability(i);
        return this;
    }

    public ArchitecturyItemProperties craftRemainder(Item item) {
        properties.craftRemainder(item);
        return this;
    }

    public ArchitecturyItemProperties tab(CreativeModeTab creativeModeTab) {
        properties.tab(creativeModeTab);
        return this;
    }

    public ArchitecturyItemProperties rarity(Rarity rarity) {
        properties.rarity(rarity);
        return this;
    }

    public ArchitecturyItemProperties fireResistant() {
        properties.fireResistant();
        return this;
    }

    // fabric item properties

    public ArchitecturyItemProperties equipmentSlot(Function<ItemStack, EquipmentSlot> function) {
        ArchitecturyItemPropertiesPlatform.equipmentSlot(properties, function);
        return this;
    }

    public ArchitecturyItemProperties customDamage(CustomDamageHandler handler) {
        ArchitecturyItemPropertiesPlatform.customDamage(properties, handler);
        return this;
    }

    @FunctionalInterface
    public interface CustomDamageHandler {
        /**
         * Called to apply damage to the given stack.
         * This can be used to e.g. drain from a battery before actually damaging the item.
         * @param amount The amount of damage originally requested
         * @param breakCallback Callback when the stack reaches zero damage. See {@link ItemStack#hurtAndBreak(int, LivingEntity, Consumer)} (int, LivingEntity, Consumer)} and its callsites for more information.
         * @return The amount of damage to pass to vanilla's logic
         */
        int damage(ItemStack stack, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback);
    }

    // forge item properties

    public ArchitecturyItemProperties setNoRepair() {
        ArchitecturyItemPropertiesPlatform.setNoRepair(properties);
        return this;
    }

    public ArchitecturyItemProperties addToolType(ToolType type, int level) {
        ArchitecturyItemPropertiesPlatform.addToolType(properties, type.forgeName, level);
        return this;
    }

    public ArchitecturyItemProperties setISTER(Supplier<Callable<BlockEntityWithoutLevelRenderer>> ister){
        ArchitecturyItemPropertiesPlatform.setISTER(properties, ister);
        return this;
    }
}
