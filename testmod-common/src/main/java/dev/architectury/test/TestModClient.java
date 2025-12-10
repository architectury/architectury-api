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
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.client.gui.ClientTooltipComponentRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.test.networking.TestModNet;
import dev.architectury.test.registry.TestRegistries;
import dev.architectury.test.registry.client.TestKeybinds;
import dev.architectury.test.registry.objects.ItemWithTooltip;
import dev.architectury.test.registry.objects.MyClientTooltipComponent;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static dev.architectury.test.TestMod.SINK;

public class TestModClient {
    public static void initializeClient() {
        ClientLifecycleEvent.CLIENT_STARTED.register((client) -> SINK.accept("Client started!"));
        ClientLifecycleEvent.CLIENT_STOPPING.register((client) -> SINK.accept("Client stopping!"));
        TestKeybinds.initialize();
        TestModNet.initializeClient();
        EntityRendererRegistry.register(TestRegistries.TEST_ENTITY, CowRenderer::new);
        EntityRendererRegistry.register(TestRegistries.TEST_ENTITY_2, CowRenderer::new);
        ClientTooltipComponentRegistry.register(ItemWithTooltip.MyTooltipComponent.class, MyClientTooltipComponent::new);
        ClientCommandRegistrationEvent.EVENT.register((dispatcher, access) -> {
            dispatcher.register(ClientCommandRegistrationEvent.literal("cool_client")
                    .then(ClientCommandRegistrationEvent.argument("string", StringArgumentType.string())
                            .executes(context -> {
                                String string = StringArgumentType.getString(context, "string");
                                SINK.accept("Cool client command for " + string);
                                return 0;
                            })));
        });
        ClientGuiEvent.RENDER_CONTAINER_BACKGROUND.register(((screen, graphics, mouseX, mouseY, delta) -> {
            graphics.renderItem(new ItemStack(Items.DIAMOND), mouseX, mouseY);
        }));
    }
}
