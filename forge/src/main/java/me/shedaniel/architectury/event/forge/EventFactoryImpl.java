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

package me.shedaniel.architectury.event.forge;

import me.shedaniel.architectury.event.EventFactory;
import me.shedaniel.architectury.event.events.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class EventFactoryImpl implements EventFactory.Impl {
    @Override
    public void registerClient() {
        MinecraftForge.EVENT_BUS.register(Client.class);
    }
    
    @Override
    public void registerCommon() {
        MinecraftForge.EVENT_BUS.register(Common.class);
    }
    
    @Override
    public void registerServer() {
        MinecraftForge.EVENT_BUS.register(Server.class);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static class Client {
        @SubscribeEvent
        public static void event(ItemTooltipEvent event) {
            TooltipEvent.ITEM.invoker().append(event.getItemStack(), event.getToolTip(), event.getFlags());
        }
        
        @SubscribeEvent
        public static void event(ClientTickEvent event) {
            if (event.phase == Phase.START)
                TickEvent.CLIENT_PRE.invoker().tick(Minecraft.getInstance());
            else if (event.phase == Phase.END)
                TickEvent.CLIENT_POST.invoker().tick(Minecraft.getInstance());
        }
        
        @SubscribeEvent
        public static void event(RenderGameOverlayEvent.Post event) {
            GuiEvent.RENDER_HUD.invoker().renderHud(event.getMatrixStack(), event.getPartialTicks());
        }
        
        @SubscribeEvent
        public static void event(ClientPlayerNetworkEvent.LoggedInEvent event) {
            PlayerEvent.CLIENT_PLAYER_JOIN.invoker().join(event.getPlayer());
        }
        
        @SubscribeEvent
        public static void event(ClientPlayerNetworkEvent.LoggedOutEvent event) {
            PlayerEvent.CLIENT_PLAYER_QUIT.invoker().quit(event.getPlayer());
        }
        
        @SubscribeEvent
        public static void event(ClientPlayerNetworkEvent.RespawnEvent event) {
            PlayerEvent.CLIENT_PLAYER_RESPAWN.invoker().respawn(event.getOldPlayer(), event.getNewPlayer());
        }
    }
    
    public static class Common {
        @SubscribeEvent
        public static void event(ServerTickEvent event) {
            if (event.phase == Phase.START)
                TickEvent.SERVER_PRE.invoker().tick(ServerLifecycleHooks.getCurrentServer());
            else if (event.phase == Phase.END)
                TickEvent.SERVER_POST.invoker().tick(ServerLifecycleHooks.getCurrentServer());
        }
        
        @SubscribeEvent
        public static void event(WorldTickEvent event) {
            if (event.side == LogicalSide.SERVER) {
                if (event.phase == Phase.START)
                    TickEvent.SERVER_WORLD_PRE.invoker().tick((ServerWorld) event.world);
                else if (event.phase == Phase.END)
                    TickEvent.SERVER_WORLD_POST.invoker().tick((ServerWorld) event.world);
            }
        }
        
        @SubscribeEvent
        public static void event(FMLServerStartingEvent event) {
            LifecycleEvent.SERVER_STARTING.invoker().stateChanged(event.getServer());
        }
        
        @SubscribeEvent
        public static void event(FMLServerStartedEvent event) {
            LifecycleEvent.SERVER_STARTED.invoker().stateChanged(event.getServer());
        }
        
        @SubscribeEvent
        public static void event(FMLServerStoppingEvent event) {
            LifecycleEvent.SERVER_STOPPING.invoker().stateChanged(event.getServer());
        }
        
        @SubscribeEvent
        public static void event(FMLServerStoppedEvent event) {
            LifecycleEvent.SERVER_STOPPED.invoker().stateChanged(event.getServer());
        }
        
        @SubscribeEvent
        public static void event(RegisterCommandsEvent event) {
            CommandRegistrationEvent.EVENT.invoker().register(event.getDispatcher());
        }
        
        @SubscribeEvent
        public static void event(PlayerLoggedInEvent event) {
            PlayerEvent.PLAYER_JOIN.invoker().join((ServerPlayerEntity) event.getPlayer());
        }
        
        @SubscribeEvent
        public static void event(PlayerLoggedOutEvent event) {
            PlayerEvent.PLAYER_QUIT.invoker().quit((ServerPlayerEntity) event.getPlayer());
        }
        
        @SubscribeEvent
        public static void event(PlayerRespawnEvent event) {
            PlayerEvent.PLAYER_RESPAWN.invoker().respawn((ServerPlayerEntity) event.getPlayer(), event.isEndConquered());
        }
    }
    
    @OnlyIn(Dist.DEDICATED_SERVER)
    public static class Server {
        
    }
}
