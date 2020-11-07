/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.shedaniel.architectury.event.fabric;

import me.shedaniel.architectury.event.EventHandler;
import me.shedaniel.architectury.event.events.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.commands.Commands;

public class EventHandlerImpl implements EventHandler.Impl {
    @Override
    public void registerClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(LifecycleEvent.CLIENT_STARTED.invoker()::stateChanged);
        ClientLifecycleEvents.CLIENT_STOPPING.register(LifecycleEvent.CLIENT_STOPPING.invoker()::stateChanged);
        
        ClientTickEvents.START_CLIENT_TICK.register(TickEvent.CLIENT_PRE.invoker()::tick);
        ClientTickEvents.END_CLIENT_TICK.register(TickEvent.CLIENT_POST.invoker()::tick);
        ClientTickEvents.START_WORLD_TICK.register(TickEvent.CLIENT_WORLD_PRE.invoker()::tick);
        ClientTickEvents.END_WORLD_TICK.register(TickEvent.CLIENT_WORLD_POST.invoker()::tick);
        
        ItemTooltipCallback.EVENT.register((itemStack, tooltipFlag, list) -> TooltipEvent.ITEM.invoker().append(itemStack, list, tooltipFlag));
        HudRenderCallback.EVENT.register(GuiEvent.RENDER_HUD.invoker()::renderHud);
    }
    
    @Override
    public void registerCommon() {
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
    }
    
    @Override
    public void registerServer() {
        
    }
}
