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

package dev.architectury.registry.level.forge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

import java.util.function.BiConsumer;

public class GameRuleFactoryImpl {
    private GameRuleFactoryImpl() {
    }
    
    public static GameRules.Type<GameRules.BooleanValue> createBooleanRule(boolean defaultValue) {
        return GameRules.BooleanValue.create(defaultValue);
    }
    
    public static GameRules.Type<GameRules.BooleanValue> createBooleanRule(boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changedCallback) {
        return GameRules.BooleanValue.create(defaultValue, changedCallback);
    }
    
    public static GameRules.Type<GameRules.IntegerValue> createIntRule(int defaultValue) {
        return GameRules.IntegerValue.create(defaultValue);
    }
    
    public static GameRules.Type<GameRules.IntegerValue> createIntRule(int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changedCallback) {
        return GameRules.IntegerValue.create(defaultValue, changedCallback);
    }
}
