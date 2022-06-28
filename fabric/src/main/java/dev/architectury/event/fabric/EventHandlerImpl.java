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

package dev.architectury.event.fabric;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.TickEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;

public class EventHandlerImpl {
    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(instance -> ClientLifecycleEvent.CLIENT_STARTED.invoker().stateChanged(instance));
        ClientLifecycleEvents.CLIENT_STOPPING.register(instance -> ClientLifecycleEvent.CLIENT_STOPPING.invoker().stateChanged(instance));
        
        ClientTickEvents.START_CLIENT_TICK.register(instance -> ClientTickEvent.CLIENT_PRE.invoker().tick(instance));
        ClientTickEvents.END_CLIENT_TICK.register(instance -> ClientTickEvent.CLIENT_POST.invoker().tick(instance));
        ClientTickEvents.START_WORLD_TICK.register(instance -> ClientTickEvent.CLIENT_LEVEL_PRE.invoker().tick(instance));
        ClientTickEvents.END_WORLD_TICK.register(instance -> ClientTickEvent.CLIENT_LEVEL_POST.invoker().tick(instance));
        
        ItemTooltipCallback.EVENT.register((itemStack, tooltipFlag, list) -> ClientTooltipEvent.ITEM.invoker().append(itemStack, list, tooltipFlag));
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> ClientGuiEvent.RENDER_HUD.invoker().renderHud(matrices, tickDelta));
    }
    
    public static void registerCommon() {
        ServerLifecycleEvents.SERVER_STARTING.register(instance -> LifecycleEvent.SERVER_BEFORE_START.invoker().stateChanged(instance));
        ServerLifecycleEvents.SERVER_STARTED.register(instance -> LifecycleEvent.SERVER_STARTED.invoker().stateChanged(instance));
        ServerLifecycleEvents.SERVER_STOPPING.register(instance -> LifecycleEvent.SERVER_STOPPING.invoker().stateChanged(instance));
        ServerLifecycleEvents.SERVER_STOPPED.register(instance -> LifecycleEvent.SERVER_STOPPED.invoker().stateChanged(instance));
        
        ServerTickEvents.START_SERVER_TICK.register(instance -> TickEvent.SERVER_PRE.invoker().tick(instance));
        ServerTickEvents.END_SERVER_TICK.register(instance -> TickEvent.SERVER_POST.invoker().tick(instance));
        ServerTickEvents.START_WORLD_TICK.register(instance -> TickEvent.SERVER_LEVEL_PRE.invoker().tick(instance));
        ServerTickEvents.END_WORLD_TICK.register(instance -> TickEvent.SERVER_LEVEL_POST.invoker().tick(instance));
        
        ServerWorldEvents.LOAD.register((server, world) -> LifecycleEvent.SERVER_LEVEL_LOAD.invoker().act(world));
        ServerWorldEvents.UNLOAD.register((server, world) -> LifecycleEvent.SERVER_LEVEL_UNLOAD.invoker().act(world));
        
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, selection) -> CommandRegistrationEvent.EVENT.invoker().register(dispatcher, selection));
        
        UseItemCallback.EVENT.register((player, world, hand) -> InteractionEvent.RIGHT_CLICK_ITEM.invoker().click(player, hand).asMinecraft());
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> InteractionEvent.RIGHT_CLICK_BLOCK.invoker().click(player, hand, hitResult.getBlockPos(), hitResult.getDirection()).asMinecraft());
        AttackBlockCallback.EVENT.register((player, world, hand, pos, face) -> InteractionEvent.LEFT_CLICK_BLOCK.invoker().click(player, hand, pos, face).asMinecraft());
        
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> LootEvent.MODIFY_LOOT_TABLE.invoker().modifyLootTable(lootManager, id, new LootTableModificationContextImpl(tableBuilder)));
    }
    
    @Environment(EnvType.SERVER)
    public static void registerServer() {
        
    }
}
