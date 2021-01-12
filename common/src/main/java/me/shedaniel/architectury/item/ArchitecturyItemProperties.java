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

/**
 * A wrapper for The {@link Item.Properties} class for both the fabric and forge environments. Deprecated methods in this class only work on one side, so do not necessarily expect the same functionality.
 */
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

    /**
     * Only works in fabric environment
     *
     * @param function A function to convert an item stack to a Equipment Slot
     * @return Thiss
     */
    @Deprecated
    public ArchitecturyItemProperties equipmentSlot(Function<ItemStack, EquipmentSlot> function) {
        ArchitecturyItemPropertiesPlatform.equipmentSlot(properties, function);
        return this;
    }


    /**
     * Only works in fabric environment
     *
     * @param handler The {@link CustomDamageHandler}
     * @return this
     */
    @Deprecated
    public ArchitecturyItemProperties customDamage(CustomDamageHandler handler) {
        ArchitecturyItemPropertiesPlatform.customDamage(properties, handler);
        return this;
    }

    /*
     * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *     http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    @FunctionalInterface
    public interface CustomDamageHandler {
        /**
         * Called to apply damage to the given stack.
         * This can be used to e.g. drain from a battery before actually damaging the item.
         *
         * @param amount        The amount of damage originally requested
         * @param breakCallback Callback when the stack reaches zero damage. See {@link ItemStack#hurtAndBreak(int, LivingEntity, Consumer)} (int, LivingEntity, Consumer)} and its callsites for more information.
         * @return The amount of damage to pass to vanilla's logic
         */
        int damage(ItemStack stack, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback);
    }

    // forge item properties

    /**
     * Only works in forge environment
     */
    @Deprecated
    public ArchitecturyItemProperties setNoRepair() {
        ArchitecturyItemPropertiesPlatform.setNoRepair(properties);
        return this;
    }


    /**
     * Only works in forge environment. For fabric add it to the {@code fabric:<tool_type>} tag
     *
     * @param type  The tool type
     * @param level The mining level
     * @return this
     */
    @Deprecated
    public ArchitecturyItemProperties addToolType(ToolType type, int level) {
        ArchitecturyItemPropertiesPlatform.addToolType(properties, type.forgeName, level);
        return this;
    }

    /**
     * Creates a custom item renderer. In fabric, it is the same as calling {@code BuiltinItemRendererRegistry.INSTANCE.register(ITEM, new CustomItemRenderer());}
     *
     * @param ister The Item renderer, wrapped in a suppiler and then a callable to prevent loading client code on a server
     * @return this
     */
    public ArchitecturyItemProperties setISTER(Supplier<Callable<BlockEntityWithoutLevelRenderer>> ister) {
        ArchitecturyItemPropertiesPlatform.setISTER(properties, ister);
        return this;
    }
}
