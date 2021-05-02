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

package me.shedaniel.architectury.event.forge;

import me.shedaniel.architectury.event.CompoundEventResult;
import me.shedaniel.architectury.event.EventResult;
import me.shedaniel.architectury.event.events.PlayerEvent;
import me.shedaniel.architectury.event.events.*;
import me.shedaniel.architectury.utils.IntValue;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.player.PlayerEvent.*;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.event.world.BlockEvent.FarmlandTrampleEvent;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.event.world.ExplosionEvent.Start;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.server.*;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class EventHandlerImplCommon {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerTickEvent event) {
        if (event.phase == Phase.START)
            TickEvent.SERVER_PRE.invoker().tick(ServerLifecycleHooks.getCurrentServer());
        else if (event.phase == Phase.END)
            TickEvent.SERVER_POST.invoker().tick(ServerLifecycleHooks.getCurrentServer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            if (event.phase == Phase.START)
                TickEvent.SERVER_WORLD_PRE.invoker().tick((ServerLevel) event.world);
            else if (event.phase == Phase.END)
                TickEvent.SERVER_WORLD_POST.invoker().tick((ServerLevel) event.world);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(FMLServerStartingEvent event) {
        LifecycleEvent.SERVER_STARTING.invoker().stateChanged(event.getServer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(FMLServerStartedEvent event) {
        LifecycleEvent.SERVER_STARTED.invoker().stateChanged(event.getServer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(FMLServerStoppingEvent event) {
        LifecycleEvent.SERVER_STOPPING.invoker().stateChanged(event.getServer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(FMLServerStoppedEvent event) {
        LifecycleEvent.SERVER_STOPPED.invoker().stateChanged(event.getServer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(RegisterCommandsEvent event) {
        CommandRegistrationEvent.EVENT.invoker().register(event.getDispatcher(), event.getEnvironment());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerLoggedInEvent event) {
        PlayerEvent.PLAYER_JOIN.invoker().join((ServerPlayer) event.getPlayer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerLoggedOutEvent event) {
        PlayerEvent.PLAYER_QUIT.invoker().quit((ServerPlayer) event.getPlayer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerRespawnEvent event) {
        PlayerEvent.PLAYER_RESPAWN.invoker().respawn((ServerPlayer) event.getPlayer(), event.isEndConquered());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(CommandEvent event) {
        CommandPerformEvent performEvent = new CommandPerformEvent(event.getParseResults(), event.getException());
        if (CommandPerformEvent.EVENT.invoker().act(performEvent) == InteractionResult.FAIL) {
            event.setCanceled(true);
        }
        event.setParseResults(performEvent.getResults());
        event.setException(performEvent.getThrowable());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerTickEvent event) {
        if (event.phase == Phase.START) {
            TickEvent.PLAYER_PRE.invoker().tick(event.player);
        } else if (event.phase == Phase.END) {
            TickEvent.PLAYER_POST.invoker().tick(event.player);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerChatEvent event) {
        InteractionResultHolder<Component> process = ChatEvent.SERVER.invoker().process(event.getPlayer(), event.getMessage(), event.getComponent());
        if (process.getObject() != null)
            event.setComponent(process.getObject());
        if (process.getResult() == InteractionResult.FAIL)
            event.setCanceled(true);
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) event.getWorld();
            LifecycleEvent.SERVER_WORLD_LOAD.invoker().act(world);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(WorldEvent.Unload event) {
        if (event.getWorld() instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) event.getWorld();
            LifecycleEvent.SERVER_WORLD_UNLOAD.invoker().act(world);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(WorldEvent.Save event) {
        if (event.getWorld() instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) event.getWorld();
            LifecycleEvent.SERVER_WORLD_SAVE.invoker().act(world);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(LivingDeathEvent event) {
        if (EntityEvent.LIVING_DEATH.invoker().die(event.getEntityLiving(), event.getSource()) == InteractionResult.FAIL) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(AdvancementEvent event) {
        if (event.getPlayer() instanceof ServerPlayer) {
            PlayerEvent.PLAYER_ADVANCEMENT.invoker().award((ServerPlayer) event.getPlayer(), event.getAdvancement());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(Clone event) {
        if (event.getOriginal() instanceof ServerPlayer && event.getPlayer() instanceof ServerPlayer) {
            PlayerEvent.PLAYER_CLONE.invoker().clone((ServerPlayer) event.getOriginal(), (ServerPlayer) event.getPlayer(), !event.isWasDeath());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(Start event) {
        if (ExplosionEvent.PRE.invoker().explode(event.getWorld(), event.getExplosion()) == InteractionResult.FAIL) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(Detonate event) {
        ExplosionEvent.DETONATE.invoker().explode(event.getWorld(), event.getExplosion(), event.getAffectedEntities());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(LivingAttackEvent event) {
        if (EntityEvent.LIVING_ATTACK.invoker().attack(event.getEntityLiving(), event.getSource(), event.getAmount()) == InteractionResult.FAIL) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(EntityJoinWorldEvent event) {
        if (EntityEvent.ADD.invoker().add(event.getEntity(), event.getWorld()) == InteractionResult.FAIL) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(FarmlandTrampleEvent event) {
        if (InteractionEvent.FARMLAND_TRAMPLE.invoker().trample((Level) event.getWorld(), event.getPos(), event.getState(), event.getFallDistance(), event.getEntity()).value() != null) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(FillBucketEvent event) {
        ItemStack oldItem = event.getEmptyBucket();
        CompoundEventResult<ItemStack> result = PlayerEvent.FILL_BUCKET.invoker().fill(event.getPlayer(), event.getWorld(), oldItem, event.getTarget());
        if (result.interruptsFurtherEvaluation()) {
            event.setCanceled(true);
            event.setFilledBucket(result.object());
            
            if (result.value() != null) {
                event.setResult(result.value() ? Event.Result.ALLOW : Event.Result.DENY);
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(EnteringChunk event) {
        EntityEvent.ENTER_CHUNK.invoker().enterChunk(event.getEntity(), event.getNewChunkX(), event.getNewChunkZ(), event.getOldChunkX(), event.getOldChunkZ());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(LivingSpawnEvent.CheckSpawn event) {
        EventResult result = EntityEvent.LIVING_CHECK_SPAWN.invoker().canSpawn(event.getEntityLiving(), event.getWorld(), event.getX(), event.getY(), event.getZ(), event.getSpawnReason(), event.getSpawner());
        if (result.interruptsFurtherEvaluation()) {
            if (result.value() != null) {
                event.setResult(result.value() == Boolean.TRUE ? Event.Result.ALLOW : Event.Result.DENY);
            }
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ItemCraftedEvent event) {
        PlayerEvent.CRAFT_ITEM.invoker().craft(event.getPlayer(), event.getCrafting(), event.getInventory());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ItemSmeltedEvent event) {
        PlayerEvent.SMELT_ITEM.invoker().smelt(event.getPlayer(), event.getSmelting());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(EntityItemPickupEvent event) {
        PlayerEvent.PICKUP_ITEM_PRE.invoker().canPickup(event.getPlayer(), event.getItem(), event.getItem().getItem());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ItemPickupEvent event) {
        PlayerEvent.PICKUP_ITEM_POST.invoker().pickup(event.getPlayer(), event.getOriginalEntity(), event.getStack());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ItemTossEvent event) {
        PlayerEvent.DROP_ITEM.invoker().drop(event.getPlayer(), event.getEntityItem());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerContainerEvent.Open event) {
        PlayerEvent.OPEN_MENU.invoker().open(event.getPlayer(), event.getContainer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerContainerEvent.Close event) {
        PlayerEvent.CLOSE_MENU.invoker().close(event.getPlayer(), event.getContainer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerInteractEvent.RightClickItem event) {
        InteractionResultHolder<ItemStack> result = InteractionEvent.RIGHT_CLICK_ITEM.invoker().click(event.getPlayer(), event.getHand());
        if (result.getResult() != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result.getResult());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult result = InteractionEvent.RIGHT_CLICK_BLOCK.invoker().click(event.getPlayer(), event.getHand(), event.getPos(), event.getFace());
        if (result != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result);
            event.setUseBlock(Event.Result.DENY);
            event.setUseItem(Event.Result.DENY);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerInteractEvent.EntityInteract event) {
        InteractionResult result = InteractionEvent.INTERACT_ENTITY.invoker().interact(event.getPlayer(), event.getTarget(), event.getHand());
        if (result != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerInteractEvent.LeftClickBlock event) {
        InteractionResult result = InteractionEvent.LEFT_CLICK_BLOCK.invoker().click(event.getPlayer(), event.getHand(), event.getPos(), event.getFace());
        if (result != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result);
            event.setUseBlock(Event.Result.DENY);
            event.setUseItem(Event.Result.DENY);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer && event.getWorld() instanceof Level) {
            InteractionResult result = BlockEvent.BREAK.invoker().breakBlock((Level) event.getWorld(), event.getPos(), event.getState(), (ServerPlayer) event.getPlayer(), new IntValue() {
                @Override
                public int getAsInt() {
                    return event.getExpToDrop();
                }
                
                @Override
                public void accept(int value) {
                    event.setExpToDrop(value);
                }
            });
            if (result != InteractionResult.PASS) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(EntityPlaceEvent event) {
        if (event.getWorld() instanceof Level) {
            InteractionResult result = BlockEvent.PLACE.invoker().placeBlock((Level) event.getWorld(), event.getPos(), event.getState(), event.getEntity());
            if (result != InteractionResult.PASS) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(FMLServerAboutToStartEvent event) {
        LifecycleEvent.SERVER_BEFORE_START.invoker().stateChanged(event.getServer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerChangedDimensionEvent event) {
        if (event.getPlayer() instanceof ServerPlayer) {
            PlayerEvent.CHANGE_DIMENSION.invoker().change((ServerPlayer) event.getPlayer(), event.getFrom(), event.getTo());
        }
    }
    
    public static class ModBasedEventHandler {
        
    }
}
