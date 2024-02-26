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

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
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
import dev.architectury.test.registry.client.TestKeybinds;
import dev.architectury.test.tags.TestTags;
import dev.architectury.test.trade.TestTrades;
import dev.architectury.test.worldgen.TestWorldGeneration;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.CowRenderer;

public class TestMod {
    public static final MessageSink SINK = EnvExecutor.getEnvSpecific(() -> ClientOverlayMessageSink::new, () -> ConsoleMessageSink::new);
    public static final String MOD_ID = "architectury_test";
    
    public static void initialize() {
        DebugEvents.initialize();
        TestRegistries.initialize();
        TestGameRules.init();
        TestTags.initialize();
        TestTrades.init();
        TestParticles.initialize();
        TestModNet.initialize();
        TestBlockInteractions.init();
        TestLoot.init();
        TestWorldGeneration.initialize();
        EnvExecutor.runInEnv(Env.CLIENT, () -> TestMod.Client::initializeClient);
    }
    
    @Environment(EnvType.CLIENT)
    public static class Client {
        @Environment(EnvType.CLIENT)
        public static void initializeClient() {
            ClientLifecycleEvent.CLIENT_STARTED.register((client) -> SINK.accept("Client started!"));
            ClientLifecycleEvent.CLIENT_STOPPING.register((client) -> SINK.accept("Client stopping!"));
            TestKeybinds.initialize();
            TestModNet.initializeClient();
            EntityRendererRegistry.register(TestRegistries.TEST_ENTITY, CowRenderer::new);
            EntityRendererRegistry.register(TestRegistries.TEST_ENTITY_2, CowRenderer::new);
            ClientCommandRegistrationEvent.EVENT.register(dispatcher -> {
                dispatcher.register(ClientCommandRegistrationEvent.literal("cool_client")
                        .then(ClientCommandRegistrationEvent.argument("string", StringArgumentType.string())
                                .executes(context -> {
                                    String string = StringArgumentType.getString(context, "string");
                                    SINK.accept("Cool client command for " + string);
                                    return 0;
                                })));
            });
        }
    }
}
