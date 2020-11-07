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

import me.shedaniel.architectury.event.EventHandler;
import me.shedaniel.architectury.event.events.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent.*;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.*;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.event.world.ExplosionEvent.Start;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.List;

public class EventHandlerImpl implements EventHandler.Impl {
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
            if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
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
        
        @SubscribeEvent
        public static void event(GuiScreenEvent.InitGuiEvent.Pre event) {
            if (GuiEvent.INIT_PRE.invoker().init(event.getGui(), event.getWidgetList(), (List<IGuiEventListener>) event.getGui().children()) == ActionResultType.FAIL) {
                event.setCanceled(true);
            }
        }
        
        @SubscribeEvent
        public static void event(GuiScreenEvent.InitGuiEvent.Post event) {
            GuiEvent.INIT_POST.invoker().init(event.getGui(), event.getWidgetList(), (List<IGuiEventListener>) event.getGui().children());
        }
        
        @SubscribeEvent
        public static void event(RenderGameOverlayEvent.Text event) {
            GuiEvent.DEBUG_TEXT_LEFT.invoker().gatherText(event.getLeft());
            GuiEvent.DEBUG_TEXT_RIGHT.invoker().gatherText(event.getRight());
        }
        
        @SubscribeEvent
        public static void event(ClientChatEvent event) {
            ActionResult<String> process = ChatEvent.CLIENT.invoker().process(event.getMessage());
            if (process.getObject() != null)
                event.setMessage(process.getObject());
            if (process.getResult() == ActionResultType.FAIL)
                event.setCanceled(true);
        }
        
        @SubscribeEvent
        public static void event(ClientChatReceivedEvent event) {
            ActionResult<ITextComponent> process = ChatEvent.CLIENT_RECEIVED.invoker().process(event.getType(), event.getMessage(), event.getSenderUUID());
            if (process.getObject() != null)
                event.setMessage(process.getObject());
            if (process.getResult() == ActionResultType.FAIL)
                event.setCanceled(true);
        }
        
        @SubscribeEvent
        public static void event(WorldEvent.Save event) {
            if (event.getWorld() instanceof ClientWorld) {
                ClientWorld world = (ClientWorld) event.getWorld();
                LifecycleEvent.CLIENT_WORLD_LOAD.invoker().act(world);
            }
        }
        
        @SubscribeEvent
        public static void event(GuiScreenEvent.DrawScreenEvent.Pre event) {
            if (GuiEvent.RENDER_PRE.invoker().render(event.getGui(), event.getMatrixStack(), event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks()) == ActionResultType.FAIL) {
                event.setCanceled(true);
            }
        }
        
        @SubscribeEvent
        public static void event(GuiScreenEvent.DrawScreenEvent.Post event) {
            GuiEvent.RENDER_POST.invoker().render(event.getGui(), event.getMatrixStack(), event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks());
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
            CommandRegistrationEvent.EVENT.invoker().register(event.getDispatcher(), event.getEnvironment());
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
        
        @SubscribeEvent
        public static void event(CommandEvent event) {
            CommandPerformEvent performEvent = new CommandPerformEvent(event.getParseResults(), event.getException());
            if (CommandPerformEvent.EVENT.invoker().act(performEvent) == ActionResultType.FAIL) {
                event.setCanceled(true);
            }
            event.setParseResults(performEvent.getResults());
            event.setException(performEvent.getThrowable());
        }
        
        @SubscribeEvent
        public static void event(PlayerTickEvent event) {
            if (event.phase == Phase.START) {
                TickEvent.PLAYER_PRE.invoker().tick(event.player);
            } else if (event.phase == Phase.END) {
                TickEvent.PLAYER_POST.invoker().tick(event.player);
            }
        }
        
        @SubscribeEvent
        public static void event(ServerChatEvent event) {
            ActionResult<ITextComponent> process = ChatEvent.SERVER.invoker().process(event.getPlayer(), event.getMessage(), event.getComponent());
            if (process.getObject() != null)
                event.setComponent(process.getObject());
            if (process.getResult() == ActionResultType.FAIL)
                event.setCanceled(true);
        }
        
        @SubscribeEvent
        public static void event(WorldEvent.Load event) {
            if (event.getWorld() instanceof ServerWorld) {
                ServerWorld world = (ServerWorld) event.getWorld();
                LifecycleEvent.SERVER_WORLD_LOAD.invoker().act(world);
            }
        }
        
        @SubscribeEvent
        public static void event(WorldEvent.Unload event) {
            if (event.getWorld() instanceof ServerWorld) {
                ServerWorld world = (ServerWorld) event.getWorld();
                LifecycleEvent.SERVER_WORLD_UNLOAD.invoker().act(world);
            }
        }
        
        @SubscribeEvent
        public static void event(WorldEvent.Save event) {
            if (event.getWorld() instanceof ServerWorld) {
                ServerWorld world = (ServerWorld) event.getWorld();
                LifecycleEvent.SERVER_WORLD_SAVE.invoker().act(world);
            }
        }
        
        @SubscribeEvent
        public static void event(LivingDeathEvent event) {
            if (EntityEvent.LIVING_DEATH.invoker().die(event.getEntityLiving(), event.getSource()) == ActionResultType.FAIL) {
                event.setCanceled(true);
            }
        }
        
        @SubscribeEvent
        public static void event(AdvancementEvent event) {
            if (event.getPlayer() instanceof ServerPlayerEntity) {
                PlayerEvent.PLAYER_ADVANCEMENT.invoker().award((ServerPlayerEntity) event.getPlayer(), event.getAdvancement());
            }
        }
        
        @SubscribeEvent
        public static void event(Clone event) {
            if (event.getOriginal() instanceof ServerPlayerEntity && event.getPlayer() instanceof ServerPlayerEntity) {
                PlayerEvent.PLAYER_CLONE.invoker().clone((ServerPlayerEntity) event.getOriginal(), (ServerPlayerEntity) event.getPlayer(), !event.isWasDeath());
            }
        }
        
        @SubscribeEvent
        public static void event(Start event) {
            if (ExplosionEvent.PRE.invoker().explode(event.getWorld(), event.getExplosion()) == ActionResultType.FAIL) {
                event.setCanceled(true);
            }
        }
        
        @SubscribeEvent
        public static void event(Detonate event) {
            ExplosionEvent.DETONATE.invoker().explode(event.getWorld(), event.getExplosion(), event.getAffectedEntities());
        }
        
        @SubscribeEvent
        public static void event(LivingAttackEvent event) {
            if (EntityEvent.LIVING_ATTACK.invoker().attack(event.getEntityLiving(), event.getSource(), event.getAmount()) == ActionResultType.FAIL) {
                event.setCanceled(true);
            }
        }
        
        @SubscribeEvent
        public static void event(EntityJoinWorldEvent event) {
            if (EntityEvent.ADD.invoker().add(event.getEntity(), event.getWorld()) == ActionResultType.FAIL) {
                event.setCanceled(true);
            }
        }
        
        @SubscribeEvent
        public static void event(ItemCraftedEvent event) {
            PlayerEvent.CRAFT_ITEM.invoker().craft(event.getPlayer(), event.getCrafting(), event.getInventory());
        }
        
        @SubscribeEvent
        public static void event(ItemSmeltedEvent event) {
            PlayerEvent.SMELT_ITEM.invoker().smelt(event.getPlayer(), event.getSmelting());
        }
        
        @SubscribeEvent
        public static void event(EntityItemPickupEvent event) {
            PlayerEvent.PICKUP_ITEM_PRE.invoker().canPickup(event.getPlayer(), event.getItem(), event.getItem().getItem());
        }
        
        @SubscribeEvent
        public static void event(ItemPickupEvent event) {
            PlayerEvent.PICKUP_ITEM_POST.invoker().pickup(event.getPlayer(), event.getOriginalEntity(), event.getStack());
        }
        
        @SubscribeEvent
        public static void event(ItemTossEvent event) {
            PlayerEvent.DROP_ITEM.invoker().drop(event.getPlayer(), event.getEntityItem());
        }
    
        @SubscribeEvent
        public static void event(PlayerContainerEvent.Open event) {
            PlayerEvent.OPEN_MENU.invoker().open(event.getPlayer(), event.getContainer());
        }
    
        @SubscribeEvent
        public static void event(PlayerContainerEvent.Close event) {
            PlayerEvent.CLOSE_MENU.invoker().close(event.getPlayer(), event.getContainer());
        }
    }
    
    @OnlyIn(Dist.DEDICATED_SERVER)
    public static class Server {
        
    }
}
