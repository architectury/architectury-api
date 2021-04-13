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

package me.shedaniel.architectury.test;

import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.test.debug.ConsoleMessageSink;
import me.shedaniel.architectury.test.debug.MessageSink;
import me.shedaniel.architectury.test.debug.client.ClientOverlayMessageSink;
import me.shedaniel.architectury.test.events.DebugEvents;
import me.shedaniel.architectury.test.gamerule.TestGameRules;
import me.shedaniel.architectury.test.registry.TestRegistries;
import me.shedaniel.architectury.test.registry.client.TestKeybinds;
import me.shedaniel.architectury.test.tags.TestTags;
import me.shedaniel.architectury.utils.Env;
import me.shedaniel.architectury.utils.EnvExecutor;

public class TestMod {
    public static final MessageSink SINK = EnvExecutor.getEnvSpecific(() -> ClientOverlayMessageSink::new, () -> ConsoleMessageSink::new);
    public static final String MOD_ID = "architectury-test";
    
    public static void initialize() {
        DebugEvents.initialize();
        TestRegistries.initialize();
        TestGameRules.init();
        TestTags.initialize();
        if (Platform.getEnvironment() == Env.CLIENT)
            TestKeybinds.initialize();
    }
}
