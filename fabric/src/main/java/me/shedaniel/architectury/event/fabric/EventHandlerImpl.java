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
        ClientLifecycleEvents.CLIENT_STARTED.register(ClientLifecycleEvent.CLIENT_STARTED.invoker()::stateChanged);
        ClientLifecycleEvents.CLIENT_STOPPING.register(ClientLifecycleEvent.CLIENT_STOPPING.invoker()::stateChanged);
        
        ClientTickEvents.START_CLIENT_TICK.register(ClientTickEvent.CLIENT_PRE.invoker()::tick);
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvent.CLIENT_POST.invoker()::tick);
        ClientTickEvents.START_WORLD_TICK.register(ClientTickEvent.CLIENT_WORLD_PRE.invoker()::tick);
        ClientTickEvents.END_WORLD_TICK.register(ClientTickEvent.CLIENT_WORLD_POST.invoker()::tick);
        
        ItemTooltipCallback.EVENT.register((itemStack, tooltipFlag, list) -> TooltipEvent.ITEM.invoker().append(itemStack, list, tooltipFlag));
        HudRenderCallback.EVENT.register(GuiEvent.RENDER_HUD.invoker()::renderHud);
    }
    
    public static void registerCommon() {
        ServerLifecycleEvents.SERVER_STARTING.register(LifecycleEvent.SERVER_STARTING.invoker()::stateChanged);
        ServerLifecycleEvents.SERVER_STARTED.register(LifecycleEvent.SERVER_STARTED.invoker()::stateChanged);
        ServerLifecycleEvents.SERVER_STOPPING.register(LifecycleEvent.SERVER_STOPPING.invoker()::stateChanged);
        ServerLifecycleEvents.SERVER_STOPPED.register(LifecycleEvent.SERVER_STOPPED.invoker()::stateChanged);
        
        ServerTickEvents.START_SERVER_TICK.register(TickEvent.SERVER_PRE.invoker()::tick);
        ServerTickEvents.END_SERVER_TICK.register(TickEvent.SERVER_POST.invoker()::tick);
        ServerTickEvents.START_WORLD_TICK.register(TickEvent.SERVER_WORLD_PRE.invoker()::tick);
        ServerTickEvents.END_WORLD_TICK.register(TickEvent.SERVER_WORLD_POST.invoker()::tick);
        
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
