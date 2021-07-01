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

package me.shedaniel.architectury.test.registry;

import me.shedaniel.architectury.hooks.EntityHooks;
import me.shedaniel.architectury.hooks.FoodPropertiesHooks;
import me.shedaniel.architectury.registry.BlockProperties;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import me.shedaniel.architectury.test.TestMod;
import me.shedaniel.architectury.test.entity.TestEntity;
import me.shedaniel.architectury.test.registry.objects.EquippableTickingItem;
import me.shedaniel.architectury.test.tab.TestCreativeTabs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static me.shedaniel.architectury.test.TestMod.SINK;

public class TestRegistries {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(TestMod.MOD_ID, Registry.ITEM_REGISTRY);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(TestMod.MOD_ID, Registry.BLOCK_REGISTRY);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(TestMod.MOD_ID, Registry.ENTITY_TYPE_REGISTRY);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(TestMod.MOD_ID, Registry.MOB_EFFECT_REGISTRY);
    
    public static final RegistrySupplier<MobEffect> TEST_EFFECT = MOB_EFFECTS.register("test_effect", () ->
            new MobEffect(MobEffectCategory.NEUTRAL, 0x123456) { });

    public static final RegistrySupplier<Item> TEST_ITEM = ITEMS.register("test_item", () ->
            new Item(new Item.Properties().tab(TestCreativeTabs.TEST_TAB)));
    public static final RegistrySupplier<Item> TEST_EQUIPPABLE = ITEMS.register("test_eqippable", () ->
            new EquippableTickingItem(new Item.Properties().tab(TestCreativeTabs.TEST_TAB)));
    public static final RegistrySupplier<Item> TEST_EDIBLE = ITEMS.register("test_edible", () -> {
        FoodProperties.Builder fpBuilder = new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).meat();
        FoodPropertiesHooks.effect(fpBuilder, () -> new MobEffectInstance(TEST_EFFECT.get(), 100), 1);
        return new Item(new Item.Properties().tab(TestCreativeTabs.TEST_TAB).food(fpBuilder.build()));
    });
    
    public static final RegistrySupplier<Block> TEST_BLOCK = BLOCKS.register("test_block", () ->
            new Block(BlockProperties.copy(Blocks.STONE)));
    public static final RegistrySupplier<Block> COLLISION_BLOCK = BLOCKS.register("collision_block", () ->
            new Block(BlockProperties.copy(Blocks.STONE)) {
                @Override
                public VoxelShape getCollisionShape(BlockState state, BlockGetter bg, BlockPos pos, CollisionContext ctx) {
                    SINK.accept(EntityHooks.fromCollision(ctx) + " is colliding with " + state);
                    return super.getCollisionShape(state, bg, pos, ctx);
                }
            });
    
    public static final RegistrySupplier<Item> TEST_BLOCK_ITEM = ITEMS.register("test_block", () ->
            new BlockItem(TEST_BLOCK.get(), new Item.Properties().tab(TestCreativeTabs.TEST_TAB)));
    public static final RegistrySupplier<Item> COLLISION_BLOCK_ITEM = ITEMS.register("collision_block", () ->
            new BlockItem(COLLISION_BLOCK.get(), new Item.Properties().tab(TestCreativeTabs.TEST_TAB)));
    
    public static final RegistrySupplier<EntityType<TestEntity>> TEST_ENTITY = ENTITY_TYPES.register("test_entity", () -> TestEntity.TYPE);
    
    public static void initialize() {
        MOB_EFFECTS.register();
        BLOCKS.register();
        ITEMS.register();
        ENTITY_TYPES.register();
    }
}
