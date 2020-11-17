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

import me.shedaniel.architectury.event.events.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.event.world.ExplosionEvent.Start;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class EventHandlerImplCommon {
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
    
    @SubscribeEvent
    public static void event(PlayerInteractEvent.RightClickItem event) {
        ActionResult<ItemStack> result = InteractionEvent.RIGHT_CLICK_ITEM.invoker().click(event.getPlayer(), event.getHand());
        if (result.getResult() != ActionResultType.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result.getResult());
        }
    }
    
    @SubscribeEvent
    public static void event(PlayerInteractEvent.RightClickBlock event) {
        ActionResultType result = InteractionEvent.RIGHT_CLICK_BLOCK.invoker().click(event.getPlayer(), event.getHand(), event.getPos(), event.getFace());
        if (result != ActionResultType.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result);
            event.setUseBlock(Event.Result.DENY);
            event.setUseItem(Event.Result.DENY);
        }
    }
    
    @SubscribeEvent
    public static void event(PlayerInteractEvent.EntityInteract event) {
        ActionResultType result = InteractionEvent.INTERACT_ENTITY.invoker().interact(event.getPlayer(), event.getTarget(), event.getHand());
        if (result != ActionResultType.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }
    
    @SubscribeEvent
    public static void event(PlayerInteractEvent.LeftClickBlock event) {
        ActionResultType result = InteractionEvent.LEFT_CLICK_BLOCK.invoker().click(event.getPlayer(), event.getHand(), event.getPos(), event.getFace());
        if (result != ActionResultType.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result);
            event.setUseBlock(Event.Result.DENY);
            event.setUseItem(Event.Result.DENY);
        }
    }
    
    @SubscribeEvent
    public static void event(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity && event.getWorld() instanceof World) {
            ActionResultType result = PlayerEvent.BREAK_BLOCK.invoker().breakBlock((World) event.getWorld(), event.getPos(), event.getState(), (ServerPlayerEntity) event.getPlayer());
            if (result != ActionResultType.PASS) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public static void event(BlockEvent.EntityPlaceEvent event) {
        if (event.getWorld() instanceof World) {
            ActionResultType result = EntityEvent.PLACE_BLOCK.invoker().placeBlock((World) event.getWorld(), event.getPos(), event.getState(), event.getEntity());
            if (result != ActionResultType.PASS) {
                event.setCanceled(true);
            }
        }
    }
    
    public static class ModBasedEventHandler {
        
    }
}
