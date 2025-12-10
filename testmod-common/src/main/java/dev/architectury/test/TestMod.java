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

package dev.architectury.test;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.test.debug.ConsoleMessageSink;
import dev.architectury.test.debug.MessageSink;
import dev.architectury.test.debug.client.ClientOverlayMessageSink;
import dev.architectury.test.events.DebugEvents;
import dev.architectury.test.gamerule.TestGameRules;
import dev.architectury.test.item.TestBlockInteractions;
import dev.architectury.test.loot.TestLoot;
import dev.architectury.test.networking.TestModNet;
import dev.architectury.test.particle.TestParticles;
import dev.architectury.test.registry.TestRegistries;
import dev.architectury.test.tags.TestTags;
import dev.architectury.test.trade.TestTrades;
import dev.architectury.test.worldgen.TestWorldGeneration;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class TestMod {
    public static final MessageSink SINK = EnvExecutor.getEnvSpecific(() -> ClientOverlayMessageSink::new, () -> ConsoleMessageSink::new);
    public static final String MOD_ID = "architectury_test";
    
    public static void initialize() {
        DebugEvents.initialize();
        TestRegistries.initialize();
        TestGameRules.GAME_RULE.register();
        TestTags.initialize();
        TestTrades.init();
        TestParticles.initialize();
        TestModNet.initialize();
        TestBlockInteractions.init();
        TestLoot.init();
        TestWorldGeneration.initialize();
        EnvExecutor.runInEnv(Env.CLIENT, () -> TestModClient::initializeClient);
        CreativeTabRegistry.modifyBuiltin(BuiltInRegistries.CREATIVE_MODE_TAB.getValue(CreativeModeTabs.BUILDING_BLOCKS), (flags, output, canUseGameMasterBlocks) -> {
            ItemStack sword = Items.DIAMOND_SWORD.getDefaultInstance();
            output.acceptBefore(new ItemStack(Items.OAK_WOOD), sword);
            output.acceptAfter(Blocks.STRIPPED_OAK_LOG, Items.BEDROCK);
        });
    }
}
