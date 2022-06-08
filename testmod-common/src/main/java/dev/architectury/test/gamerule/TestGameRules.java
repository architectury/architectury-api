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

import net.minecraft.world.level.GameRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestGameRules {
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static final GameRules.Key<GameRules.BooleanValue> SIMPLE_BOOL = GameRules.register("simpleBool", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
    public static final GameRules.Key<GameRules.IntegerValue> SIMPLE_INT = GameRules.register("simpleInt", GameRules.Category.MISC, GameRules.IntegerValue.create(10));
    public static final GameRules.Key<GameRules.BooleanValue> CALLBACK_BOOL = GameRules.register("callbackBool", GameRules.Category.MISC, GameRules.BooleanValue.create(true, (server, value) -> LOGGER.info("changed to {}", value.get())));
    public static final GameRules.Key<GameRules.IntegerValue> CALLBACK_INT = GameRules.register("callbackInt", GameRules.Category.MISC, GameRules.IntegerValue.create(10, (server, value) -> LOGGER.info("changed to {}", value.get())));
    
    public static void init() {
    }
}
