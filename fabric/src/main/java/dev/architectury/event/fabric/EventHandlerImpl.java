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

import dev.architectury.event.EventPriority;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.event.events.common.*;
import dev.architectury.impl.fabric.ChatComponentImpl;
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
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class EventHandlerImpl {
    public static final ResourceLocation HIGHEST = new ResourceLocation("architectury", "highest");
    public static final ResourceLocation HIGH = new ResourceLocation("architectury", "high");
    public static final ResourceLocation LOW = new ResourceLocation("architectury", "low");
    public static final ResourceLocation LOWEST = new ResourceLocation("architectury", "lowest");
    public static final ResourceLocation[] PHASES = new ResourceLocation[]{HIGHEST, HIGH, net.fabricmc.fabric.api.event.Event.DEFAULT_PHASE, LOW, LOWEST};
    
    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        registerWithPriority(ClientLifecycleEvents.CLIENT_STARTED, priority -> instance -> ClientLifecycleEvent.CLIENT_STARTED.invoker(priority).stateChanged(instance));
        registerWithPriority(ClientLifecycleEvents.CLIENT_STOPPING, priority -> instance -> ClientLifecycleEvent.CLIENT_STOPPING.invoker(priority).stateChanged(instance));
        
        registerWithPriority(ClientTickEvents.START_CLIENT_TICK, priority -> instance -> ClientTickEvent.CLIENT_PRE.invoker(priority).tick(instance));
        registerWithPriority(ClientTickEvents.END_CLIENT_TICK, priority -> instance -> ClientTickEvent.CLIENT_POST.invoker(priority).tick(instance));
        registerWithPriority(ClientTickEvents.START_WORLD_TICK, priority -> instance -> ClientTickEvent.CLIENT_LEVEL_PRE.invoker(priority).tick(instance));
        registerWithPriority(ClientTickEvents.END_WORLD_TICK, priority -> instance -> ClientTickEvent.CLIENT_LEVEL_POST.invoker(priority).tick(instance));
        
        registerWithPriority(ItemTooltipCallback.EVENT, priority -> (itemStack, tooltipFlag, list) -> ClientTooltipEvent.ITEM.invoker(priority).append(itemStack, list, tooltipFlag));
        registerWithPriority(HudRenderCallback.EVENT, priority -> (graphics, tickDelta) -> ClientGuiEvent.RENDER_HUD.invoker(priority).renderHud(graphics, tickDelta));
    }
    
    public static void registerCommon() {
        registerWithPriority(ServerLifecycleEvents.SERVER_STARTING, priority -> instance -> LifecycleEvent.SERVER_BEFORE_START.invoker(priority).stateChanged(instance));
        registerWithPriority(ServerLifecycleEvents.SERVER_STARTED, priority -> instance -> LifecycleEvent.SERVER_STARTED.invoker(priority).stateChanged(instance));
        registerWithPriority(ServerLifecycleEvents.SERVER_STOPPING, priority -> instance -> LifecycleEvent.SERVER_STOPPING.invoker(priority).stateChanged(instance));
        registerWithPriority(ServerLifecycleEvents.SERVER_STOPPED, priority -> instance -> LifecycleEvent.SERVER_STOPPED.invoker(priority).stateChanged(instance));
        
        registerWithPriority(ServerTickEvents.START_SERVER_TICK, priority -> instance -> TickEvent.SERVER_PRE.invoker(priority).tick(instance));
        registerWithPriority(ServerTickEvents.END_SERVER_TICK, priority -> instance -> TickEvent.SERVER_POST.invoker(priority).tick(instance));
        registerWithPriority(ServerTickEvents.START_WORLD_TICK, priority -> instance -> TickEvent.SERVER_LEVEL_PRE.invoker(priority).tick(instance));
        registerWithPriority(ServerTickEvents.END_WORLD_TICK, priority -> instance -> TickEvent.SERVER_LEVEL_POST.invoker(priority).tick(instance));
        
        registerWithPriority(ServerWorldEvents.LOAD, priority -> (server, world) -> LifecycleEvent.SERVER_LEVEL_LOAD.invoker(priority).act(world));
        registerWithPriority(ServerWorldEvents.UNLOAD, priority -> (server, world) -> LifecycleEvent.SERVER_LEVEL_UNLOAD.invoker(priority).act(world));
        
        registerWithPriority(CommandRegistrationCallback.EVENT, priority -> (dispatcher, registry, selection) -> CommandRegistrationEvent.EVENT.invoker(priority).register(dispatcher, registry, selection));
        
        registerWithPriority(UseItemCallback.EVENT, priority -> (player, world, hand) -> InteractionEvent.RIGHT_CLICK_ITEM.invoker(priority).click(player, hand).asMinecraft());
        registerWithPriority(UseBlockCallback.EVENT, priority -> (player, world, hand, hitResult) -> InteractionEvent.RIGHT_CLICK_BLOCK.invoker(priority).click(player, hand, hitResult.getBlockPos(), hitResult.getDirection()).asMinecraft());
        registerWithPriority(AttackBlockCallback.EVENT, priority -> (player, world, hand, pos, face) -> InteractionEvent.LEFT_CLICK_BLOCK.invoker(priority).click(player, hand, pos, face).asMinecraft());
        registerWithPriority(AttackEntityCallback.EVENT, priority -> (player, world, hand, entity, hitResult) -> PlayerEvent.ATTACK_ENTITY.invoker(priority).attack(player, world, entity, hand, hitResult).asMinecraft());
        
        registerWithPriority(LootTableEvents.MODIFY, priority -> (resourceManager, lootManager, id, tableBuilder, source) -> LootEvent.MODIFY_LOOT_TABLE.invoker(priority).modifyLootTable(lootManager, id, new LootTableModificationContextImpl(tableBuilder), source.isBuiltin()));
        
        ServerMessageDecoratorEvent.EVENT.register(ServerMessageDecoratorEvent.CONTENT_PHASE, (player, component) -> {
            ChatEvent.ChatComponent chatComponent = new ChatComponentImpl(component);
            ChatEvent.DECORATE.invoker().decorate(player, chatComponent);
            return chatComponent.get();
        });
        registerWithPriority(ServerMessageEvents.ALLOW_CHAT_MESSAGE, priority -> (message, sender, params) -> {
            return !ChatEvent.RECEIVED.invoker(priority).received(sender, message.decoratedContent()).isFalse();
        });
    }
    
    private static <T> void registerWithPriority(net.fabricmc.fabric.api.event.Event<T> fabricEvent, Function<EventPriority, T> listener) {
        fabricEvent.addPhaseOrdering(HIGH, net.fabricmc.fabric.api.event.Event.DEFAULT_PHASE);
        fabricEvent.addPhaseOrdering(HIGHEST, HIGH);
        fabricEvent.addPhaseOrdering(net.fabricmc.fabric.api.event.Event.DEFAULT_PHASE, LOW);
        fabricEvent.addPhaseOrdering(LOW, LOWEST);
        for (EventPriority priority : EventPriority.VALUES) {
            fabricEvent.register(PHASES[priority.ordinal()], listener.apply(priority));
        }
    }
    
    @Environment(EnvType.SERVER)
    public static void registerServer() {
    
    }
}
