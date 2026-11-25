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

package dev.architectury.test.gamerule;

import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.commands.arguments.BooleanArgument;
import net.minecraft.commands.arguments.IntegerArgument;
import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.DeferredSupplier;
import dev.architectury.test.TestMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestGameRules {
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static final DeferredRegister<GameRule<?>> GAME_RULE = DeferredRegister.create(TestMod.MOD_ID, Registries.GAME_RULE);
    
    public static final DeferredSupplier<GameRule<Boolean>> SIMPLE_BOOL = GAME_RULE.register("simple_bool", () -> new GameRule<>(
            GameRuleCategory.MISC,
            GameRuleType.BOOL,
            BooleanArgument.INSTANCE,
            GameRuleTypeVisitor::visitBoolean,
            Codec.BOOL,
            gameRuleValue -> gameRuleValue ? 1 : 0,
            false,
            FeatureFlagSet.of()
    ));
    
    public static final DeferredSupplier<GameRule<Integer>> SIMPLE_INT = GAME_RULE.register(
            "simple_int",
            () -> new GameRule<>(
                    GameRuleCategory.MISC,
                    GameRuleType.INT,
                    new IntegerArgument(MinMaxBounds.Bounds.between(0, 5)),
                    GameRuleTypeVisitor::visitInteger,
                    Codec.intRange(0, 5),
                    gameRuleValue -> gameRuleValue,
                    3,
                    FeatureFlagSet.of()
            )
    );

    public static void init() {
    }
}
