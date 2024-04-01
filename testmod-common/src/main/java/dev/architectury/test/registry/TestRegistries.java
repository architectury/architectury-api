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

package dev.architectury.test.registry;

import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes;
import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.hooks.item.food.FoodPropertiesHooks;
import dev.architectury.hooks.level.entity.EntityHooks;
import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.test.TestMod;
import dev.architectury.test.entity.TestEntity;
import dev.architectury.test.recipes.TestRecipeSerializer;
import dev.architectury.test.registry.objects.EquippableTickingItem;
import dev.architectury.test.registry.objects.ItemWithTooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import static dev.architectury.test.TestMod.SINK;

public class TestRegistries {
    public static final class TestInt {
        public final int value;
        
        public TestInt(int value) {
            this.value = value;
        }
    }
    
    public static final Registrar<TestInt> INTS = RegistrarManager.get(TestMod.MOD_ID).<TestInt>builder(new ResourceLocation(TestMod.MOD_ID, "ints"))
            .syncToClients()
            .build();
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(TestMod.MOD_ID, Registries.CREATIVE_MODE_TAB);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(TestMod.MOD_ID, Registries.ITEM);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(TestMod.MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(TestMod.MOD_ID, Registries.FLUID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(TestMod.MOD_ID, Registries.ENTITY_TYPE);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(TestMod.MOD_ID, Registries.MOB_EFFECT);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(TestMod.MOD_ID, Registries.RECIPE_SERIALIZER);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(TestMod.MOD_ID, Registries.RECIPE_TYPE);
    
    public static final RegistrySupplier<CreativeModeTab> TEST_TAB = TABS.register("test_tab", () ->
            CreativeTabRegistry.create(Component.translatable("category.architectury_test"),
                    () -> new ItemStack(TestRegistries.TEST_ITEM.get())));
    
    public static final ArchitecturyFluidAttributes TEST_FLUID_ATTRIBUTES = SimpleArchitecturyFluidAttributes.ofSupplier(() -> TestRegistries.TEST_FLUID_FLOWING, () -> TestRegistries.TEST_FLUID)
            .convertToSource(true)
            .flowingTexture(new ResourceLocation("block/water_flow"))
            .sourceTexture(new ResourceLocation("block/water_still"))
            .blockSupplier(() -> TestRegistries.TEST_FLUID_BLOCK)
            .bucketItemSupplier(() -> TestRegistries.TEST_FLUID_BUCKET)
            .color(0xFF0000);
    
    public static final RegistrySupplier<TestInt> TEST_INT = INTS.register(new ResourceLocation(TestMod.MOD_ID, "test_int"), () -> new TestInt(1));
    public static final RegistrySupplier<TestInt> TEST_INT_2 = INTS.register(new ResourceLocation(TestMod.MOD_ID, "test_int_2"), () -> new TestInt(2));
    
    public static final RegistrySupplier<MobEffect> TEST_EFFECT = MOB_EFFECTS.register("test_effect", () ->
            new MobEffect(MobEffectCategory.NEUTRAL, 0x123456) {
            });
    
    public static final RegistrySupplier<Item> TEST_ITEM = ITEMS.register("test_item", () ->
            new Item(new Item.Properties().arch$tab(TestRegistries.TEST_TAB)));
    public static final RegistrySupplier<Item> TEST_EQUIPPABLE = ITEMS.register("test_eqippable", () ->
            new EquippableTickingItem(new Item.Properties().arch$tab(TestRegistries.TEST_TAB)));
    public static final RegistrySupplier<Item> TEST_EDIBLE = ITEMS.register("test_edible", () -> {
        FoodProperties.Builder fpBuilder = new FoodProperties.Builder().nutrition(8).saturationModifier(0.8F);
        FoodPropertiesHooks.effect(fpBuilder, () -> new MobEffectInstance(TEST_EFFECT, 100), 1);
        return new Item(new Item.Properties().food(fpBuilder.build()).arch$tab(TestRegistries.TEST_TAB));
    });
    public static final RegistrySupplier<Item> TEST_SPAWN_EGG = ITEMS.register("test_spawn_egg", () ->
            new ArchitecturySpawnEggItem(TestRegistries.TEST_ENTITY, 0xFF000000, 0xFFFFFFFF,
                    new Item.Properties().arch$tab(TestRegistries.TEST_TAB)));
    public static final RegistrySupplier<Item> TEST_SPAWN_EGG_2 = ITEMS.register("test_spawn_egg_2", () ->
            new ArchitecturySpawnEggItem(TestRegistries.TEST_ENTITY_2, 0xFFFFFFFF, 0xFF000000,
                    new Item.Properties().arch$tab(TestRegistries.TEST_TAB)));
    
    public static final RegistrySupplier<Item> TEST_FLUID_BUCKET = ITEMS.register("test_fluid_bucket", () -> {
        try {
            // In example mod the forge class isn't being replaced, this is not required in mods depending on architectury
            return (Item) Class.forName(!Platform.isForge() ? "dev.architectury.core.item.ArchitecturyBucketItem" : "dev.architectury.core.item.forge.imitator.ArchitecturyBucketItem")
                    .getDeclaredConstructor(Supplier.class, Item.Properties.class)
                    .newInstance(TestRegistries.TEST_FLUID, new Item.Properties().arch$tab(TestRegistries.TEST_TAB));
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    });
    
    public static final RegistrySupplier<ItemWithTooltip> TEST_TOOLTIP = ITEMS.register("test_tooltip",
            () -> new ItemWithTooltip(new Item.Properties().arch$tab(TestRegistries.TEST_TAB)));
    
    public static final RegistrySupplier<Block> TEST_BLOCK = BLOCKS.register("test_block", () ->
            new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE)));
    public static final RegistrySupplier<Block> COLLISION_BLOCK = BLOCKS.register("collision_block", () ->
            new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE)) {
                @Override
                public VoxelShape getCollisionShape(BlockState state, BlockGetter bg, BlockPos pos, CollisionContext ctx) {
                    SINK.accept(EntityHooks.fromCollision(ctx) + " is colliding with " + state);
                    return super.getCollisionShape(state, bg, pos, ctx);
                }
            });
    
    public static final RegistrySupplier<LiquidBlock> TEST_FLUID_BLOCK = BLOCKS.register("test_fluid", () -> {
        try {
            // In example mod the forge class isn't being replaced, this is not required in mods depending on architectury
            return (LiquidBlock) Class.forName(!Platform.isForge() ? "dev.architectury.core.block.ArchitecturyLiquidBlock" : "dev.architectury.core.block.forge.imitator.ArchitecturyLiquidBlock")
                    .getDeclaredConstructor(Supplier.class, BlockBehaviour.Properties.class)
                    .newInstance(TestRegistries.TEST_FLUID, BlockBehaviour.Properties.ofLegacyCopy(Blocks.WATER).noCollission().strength(100.0F).noLootTable());
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    });
    
    public static final RegistrySupplier<Fluid> TEST_FLUID = FLUIDS.register("test_fluid", () -> {
        try {
            // In example mod the forge class isn't being replaced, this is not required in mods depending on architectury
            return (FlowingFluid) Class.forName(!Platform.isForge() ? "dev.architectury.core.fluid.ArchitecturyFlowingFluid$Source" : "dev.architectury.core.fluid.forge.imitator.ArchitecturyFlowingFluid$Source")
                    .getDeclaredConstructor(ArchitecturyFluidAttributes.class)
                    .newInstance(TestRegistries.TEST_FLUID_ATTRIBUTES);
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    });
    
    public static final RegistrySupplier<Fluid> TEST_FLUID_FLOWING = FLUIDS.register("test_fluid_flowing", () -> {
        try {
            // In example mod the forge class isn't being replaced, this is not required in mods depending on architectury
            return (FlowingFluid) Class.forName(!Platform.isForge() ? "dev.architectury.core.fluid.ArchitecturyFlowingFluid$Flowing" : "dev.architectury.core.fluid.forge.imitator.ArchitecturyFlowingFluid$Flowing")
                    .getDeclaredConstructor(ArchitecturyFluidAttributes.class)
                    .newInstance(TestRegistries.TEST_FLUID_ATTRIBUTES);
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    });
    
    public static final RegistrySupplier<Item> TEST_BLOCK_ITEM = ITEMS.register("test_block", () ->
            new BlockItem(TEST_BLOCK.get(), new Item.Properties().arch$tab(TestRegistries.TEST_TAB)));
    public static final RegistrySupplier<Item> COLLISION_BLOCK_ITEM = ITEMS.register("collision_block", () ->
            new BlockItem(COLLISION_BLOCK.get(), new Item.Properties().arch$tab(TestRegistries.TEST_TAB)));
    
    public static final RegistrySupplier<EntityType<TestEntity>> TEST_ENTITY = ENTITY_TYPES.register("test_entity", TestEntity.TYPE);
    public static final RegistrySupplier<EntityType<TestEntity>> TEST_ENTITY_2 = ENTITY_TYPES.register("test_entity_2", TestEntity.TYPE_2);
    
    public static final RegistrySupplier<RecipeSerializer<CustomRecipe>> TEST_SERIALIZER = RECIPE_SERIALIZERS.register("test_serializer", TestRecipeSerializer::new);
    
    public static final RegistrySupplier<RecipeType<CustomRecipe>> TEST_RECIPE_TYPE = RECIPE_TYPES.register("test_recipe_type", () -> new RecipeType<CustomRecipe>() {
        @Override
        public String toString() {
            return TestMod.MOD_ID + ":test_recipe_type";
        }
    });
    
    public static void initialize() {
        TABS.register();
        MOB_EFFECTS.register();
        FLUIDS.register();
        BLOCKS.register();
        ITEMS.register();
        ENTITY_TYPES.register();
        RECIPE_TYPES.register();
        RECIPE_SERIALIZERS.register();
        EntityAttributeRegistry.register(TEST_ENTITY, TestEntity::createAttributes);
        EntityAttributeRegistry.register(TEST_ENTITY_2, TestEntity::createAttributes);
        TEST_BLOCK_ITEM.listen(item -> {
            System.out.println("Registered item!");
        });
        TEST_SERIALIZER.listen(type -> {
            System.out.println("Registered recipe serializer!");
        });
        TEST_RECIPE_TYPE.listen(type -> {
            System.out.println("Registered recipe type!");
        });
    }
}
