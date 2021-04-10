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

package me.shedaniel.architectury.test.registry;

import me.shedaniel.architectury.hooks.EntityHooks;
import me.shedaniel.architectury.registry.BlockProperties;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import me.shedaniel.architectury.test.TestMod;
import me.shedaniel.architectury.test.registry.objects.EquippableTickingItem;
import me.shedaniel.architectury.test.tab.TestCreativeTabs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
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
    
    public static final RegistrySupplier<Item> TEST_ITEM = ITEMS.register("test_item", () ->
            new Item(new Item.Properties().tab(TestCreativeTabs.TEST_TAB)));
    public static final RegistrySupplier<Item> TEST_EQUIPPABLE = ITEMS.register("test_eqippable", () ->
            new EquippableTickingItem(new Item.Properties().tab(TestCreativeTabs.TEST_TAB)));
    
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
    
    public static void initialize() {
        BLOCKS.register();
        ITEMS.register();
    }
}
