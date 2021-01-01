/*
 * This file is part of architectury.
 * Copyright (C) 2020 shedaniel
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

package me.shedaniel.architectury.event.fabric;

import me.shedaniel.architectury.event.events.*;
import me.shedaniel.architectury.event.events.client.ClientLifecycleEvent;
import me.shedaniel.architectury.event.events.client.ClientTickEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.commands.Commands;

public class EventHandlerImpl {
    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(instance -> ClientLifecycleEvent.CLIENT_STARTED.invoker().stateChanged(instance));
        ClientLifecycleEvents.CLIENT_STOPPING.register(instance -> ClientLifecycleEvent.CLIENT_STOPPING.invoker().stateChanged(instance));
        
        ClientTickEvents.START_CLIENT_TICK.register(instance -> ClientTickEvent.CLIENT_PRE.invoker().tick(instance));
        ClientTickEvents.END_CLIENT_TICK.register(instance -> ClientTickEvent.CLIENT_POST.invoker().tick(instance));
        ClientTickEvents.START_WORLD_TICK.register(instance -> ClientTickEvent.CLIENT_WORLD_PRE.invoker().tick(instance));
        ClientTickEvents.END_WORLD_TICK.register(instance -> ClientTickEvent.CLIENT_WORLD_POST.invoker().tick(instance));
        
        ItemTooltipCallback.EVENT.register((itemStack, tooltipFlag, list) -> TooltipEvent.ITEM.invoker().append(itemStack, list, tooltipFlag));
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> GuiEvent.RENDER_HUD.invoker().renderHud(matrices, tickDelta));
    }
    
    public static void registerCommon() {
        ServerLifecycleEvents.SERVER_STARTING.register(instance -> LifecycleEvent.SERVER_BEFORE_START.invoker().stateChanged(instance));
        ServerLifecycleEvents.SERVER_STARTED.register(instance -> LifecycleEvent.SERVER_STARTED.invoker().stateChanged(instance));
        ServerLifecycleEvents.SERVER_STOPPING.register(instance -> LifecycleEvent.SERVER_STOPPING.invoker().stateChanged(instance));
        ServerLifecycleEvents.SERVER_STOPPED.register(instance -> LifecycleEvent.SERVER_STOPPED.invoker().stateChanged(instance));
        
        ServerTickEvents.START_SERVER_TICK.register(instance -> TickEvent.SERVER_PRE.invoker().tick(instance));
        ServerTickEvents.END_SERVER_TICK.register(instance -> TickEvent.SERVER_POST.invoker().tick(instance));
        ServerTickEvents.START_WORLD_TICK.register(instance -> TickEvent.SERVER_WORLD_PRE.invoker().tick(instance));
        ServerTickEvents.END_WORLD_TICK.register(instance -> TickEvent.SERVER_WORLD_POST.invoker().tick(instance));
        
        ServerWorldEvents.LOAD.register((server, world) -> LifecycleEvent.SERVER_WORLD_LOAD.invoker().act(world));
        ServerWorldEvents.UNLOAD.register((server, world) -> LifecycleEvent.SERVER_WORLD_UNLOAD.invoker().act(world));
        
        CommandRegistrationCallback.EVENT.register((commandDispatcher, b) -> CommandRegistrationEvent.EVENT.invoker().register(commandDispatcher, b ? Commands.CommandSelection.DEDICATED : Commands.CommandSelection.INTEGRATED));
        
        UseItemCallback.EVENT.register((player, world, hand) -> InteractionEvent.RIGHT_CLICK_ITEM.invoker().click(player, hand));
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> InteractionEvent.RIGHT_CLICK_BLOCK.invoker().click(player, hand, hitResult.getBlockPos(), hitResult.getDirection()));
        AttackBlockCallback.EVENT.register((player, world, hand, pos, face) -> InteractionEvent.LEFT_CLICK_BLOCK.invoker().click(player, hand, pos, face));
    }
    
    @Environment(EnvType.SERVER)
    public static void registerServer() {
        
    }
}
