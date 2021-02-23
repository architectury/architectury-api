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

package me.shedaniel.architectury.mixin.forge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.BiConsumer;

@Mixin(GameRules.class)
public interface GameRulesAccessor {
    /**
     * Spliting simple classes because mixin can't handle refmap using the same name
     */
    @Mixin(GameRules.BooleanValue.class)
    interface BooleanValue {
        @Invoker("create")
        static GameRules.Type<GameRules.BooleanValue> invokeCreateArchitectury(boolean value, BiConsumer<MinecraftServer, GameRules.BooleanValue> biConsumer) {
            throw new AssertionError();
        }
    }
    
    @Mixin(GameRules.BooleanValue.class)
    interface BooleanValueSimple {
        @Invoker("create")
        static GameRules.Type<GameRules.BooleanValue> invokeCreateArchitectury(boolean value) {
            throw new AssertionError();
        }
    }
    
    @Mixin(GameRules.IntegerValue.class)
    interface IntegerValue {
        @Invoker("create")
        static GameRules.Type<GameRules.IntegerValue> invokeCreateArchitectury(int value, BiConsumer<MinecraftServer, GameRules.IntegerValue> biConsumer) {
            throw new AssertionError();
        }
    }
    
    @Mixin(GameRules.IntegerValue.class)
    interface IntegerValueSimple {
        @Invoker("create")
        static GameRules.Type<GameRules.IntegerValue> invokeCreateArchitectury(int value) {
            throw new AssertionError();
        }
    }
}
