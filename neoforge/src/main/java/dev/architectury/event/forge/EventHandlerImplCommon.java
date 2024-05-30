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

package dev.architectury.event.forge;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.*;
import dev.architectury.utils.value.IntValue;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.AnimalTameEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.*;
import net.neoforged.neoforge.event.level.BlockEvent.BreakEvent;
import net.neoforged.neoforge.event.level.BlockEvent.EntityPlaceEvent;
import net.neoforged.neoforge.event.level.BlockEvent.FarmlandTrampleEvent;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent.Detonate;
import net.neoforged.neoforge.event.level.ExplosionEvent.Start;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.*;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class EventHandlerImplCommon {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerTickEvent.Pre event) {
        TickEvent.SERVER_PRE.invoker().tick(ServerLifecycleHooks.getCurrentServer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerTickEvent.Post event) {
        TickEvent.SERVER_POST.invoker().tick(ServerLifecycleHooks.getCurrentServer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(LevelTickEvent.Pre event) {
        if (!event.getLevel().isClientSide()) {
            TickEvent.SERVER_LEVEL_PRE.invoker().tick((ServerLevel) event.getLevel());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(LevelTickEvent.Post event) {
        if (!event.getLevel().isClientSide()) {
            TickEvent.SERVER_LEVEL_POST.invoker().tick((ServerLevel) event.getLevel());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerStartingEvent event) {
        LifecycleEvent.SERVER_STARTING.invoker().stateChanged(event.getServer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerStartedEvent event) {
        LifecycleEvent.SERVER_STARTED.invoker().stateChanged(event.getServer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerStoppingEvent event) {
        LifecycleEvent.SERVER_STOPPING.invoker().stateChanged(event.getServer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerStoppedEvent event) {
        LifecycleEvent.SERVER_STOPPED.invoker().stateChanged(event.getServer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(RegisterCommandsEvent event) {
        CommandRegistrationEvent.EVENT.invoker().register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerLoggedInEvent event) {
        PlayerEvent.PLAYER_JOIN.invoker().join((ServerPlayer) event.getEntity());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerLoggedOutEvent event) {
        PlayerEvent.PLAYER_QUIT.invoker().quit((ServerPlayer) event.getEntity());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerRespawnEvent event) {
        PlayerEvent.PLAYER_RESPAWN.invoker().respawn((ServerPlayer) event.getEntity(), event.isEndConquered());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(CommandEvent event) {
        CommandPerformEvent performEvent = new CommandPerformEvent(event.getParseResults(), event.getException());
        if (CommandPerformEvent.EVENT.invoker().act(performEvent).isFalse()) {
            event.setCanceled(true);
        }
        event.setParseResults(performEvent.getResults());
        event.setException(performEvent.getThrowable());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerTickEvent.Pre event) {
        TickEvent.PLAYER_PRE.invoker().tick(event.getEntity());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerTickEvent.Post event) {
        TickEvent.PLAYER_POST.invoker().tick(event.getEntity());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerChatEvent event) {
        class ChatComponentImpl implements ChatEvent.ChatComponent {
            @Override
            public Component get() {
                return event.getMessage();
            }
            
            @Override
            public void set(Component component) {
                event.setMessage(component);
            }
        }
        ChatEvent.DECORATE.invoker().decorate(event.getPlayer(), new ChatComponentImpl());
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void eventAfter(ServerChatEvent event) {
        EventResult process = ChatEvent.RECEIVED.invoker().received(event.getPlayer(), event.getMessage());
        if (process.isFalse())
            event.setCanceled(true);
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventWorldEvent(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) event.getLevel();
            LifecycleEvent.SERVER_LEVEL_LOAD.invoker().act(world);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventWorldEvent(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) event.getLevel();
            LifecycleEvent.SERVER_LEVEL_UNLOAD.invoker().act(world);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventWorldEvent(LevelEvent.Save event) {
        if (event.getLevel() instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) event.getLevel();
            LifecycleEvent.SERVER_LEVEL_SAVE.invoker().act(world);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(LivingDeathEvent event) {
        if (EntityEvent.LIVING_DEATH.invoker().die(event.getEntity(), event.getSource()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(AdvancementEvent.AdvancementEarnEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            PlayerEvent.PLAYER_ADVANCEMENT.invoker().award((ServerPlayer) event.getEntity(), event.getAdvancement());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerEvent(Clone event) {
        if (event.getOriginal() instanceof ServerPlayer && event.getEntity() instanceof ServerPlayer) {
            PlayerEvent.PLAYER_CLONE.invoker().clone((ServerPlayer) event.getOriginal(), (ServerPlayer) event.getEntity(), !event.isWasDeath());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventExplosionEvent(Start event) {
        if (ExplosionEvent.PRE.invoker().explode(event.getLevel(), event.getExplosion()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventExplosionEvent(Detonate event) {
        ExplosionEvent.DETONATE.invoker().explode(event.getLevel(), event.getExplosion(), event.getAffectedEntities());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(LivingAttackEvent event) {
        if (EntityEvent.LIVING_HURT.invoker().hurt(event.getEntity(), event.getSource(), event.getAmount()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(EntityJoinLevelEvent event) {
        if (EntityEvent.ADD.invoker().add(event.getEntity(), event.getLevel()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(FarmlandTrampleEvent event) {
        if (event.getLevel() instanceof Level && InteractionEvent.FARMLAND_TRAMPLE.invoker().trample((Level) event.getLevel(), event.getPos(), event.getState(), event.getFallDistance(), event.getEntity()).value() != null) {
            event.setCanceled(true);
        }
    }
    
    // TODO: Hook ourselves when mixin is available
    //    @SubscribeEvent(priority = EventPriority.HIGH)
    //    public static void event(EnteringChunk event) {
    //        EntityEvent.ENTER_SECTION.invoker().enterChunk(event.getEntity(), event.getNewChunkX(), event.getNewChunkZ(), event.getOldChunkX(), event.getOldChunkZ());
    //    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventLivingSpawnEvent(FinalizeSpawnEvent event) {
        EventResult result = EntityEvent.LIVING_CHECK_SPAWN.invoker().canSpawn(event.getEntity(), event.getLevel(), event.getX(), event.getY(), event.getZ(), event.getSpawnType(), null);//TODO FIX: , event.getSpawner());
        if (result.interruptsFurtherEvaluation()) {
            if (!result.isEmpty()) {
                event.setSpawnCancelled(result.value());
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(AnimalTameEvent event) {
        EventResult result = EntityEvent.ANIMAL_TAME.invoker().tame(event.getAnimal(), event.getTamer());
        event.setCanceled(result.isFalse());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ItemCraftedEvent event) {
        PlayerEvent.CRAFT_ITEM.invoker().craft(event.getEntity(), event.getCrafting(), event.getInventory());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ItemSmeltedEvent event) {
        PlayerEvent.SMELT_ITEM.invoker().smelt(event.getEntity(), event.getSmelting());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ItemEntityPickupEvent.Pre event) {
        // note: this event is weird, cancel is equivalent to denying the pickup,
        // and setting the result to ALLOW will pick it up without adding it to the player's inventory
        var result = PlayerEvent.PICKUP_ITEM_PRE.invoker().canPickup(event.getPlayer(), event.getItemEntity(), event.getItemEntity().getItem());
        if (result.isFalse()) {
            event.setCanPickup(TriState.FALSE);
        } else if (result.isTrue()) {
            event.setCanPickup(TriState.TRUE);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ItemEntityPickupEvent.Post event) {
        PlayerEvent.PICKUP_ITEM_POST.invoker().pickup(event.getPlayer(), event.getItemEntity(), event.getCurrentStack());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ItemTossEvent event) {
        PlayerEvent.DROP_ITEM.invoker().drop(event.getPlayer(), event.getEntity());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerContainerEvent(PlayerContainerEvent.Open event) {
        PlayerEvent.OPEN_MENU.invoker().open(event.getEntity(), event.getContainer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerContainerEvent(PlayerContainerEvent.Close event) {
        PlayerEvent.CLOSE_MENU.invoker().close(event.getEntity(), event.getContainer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerInteractEvent(PlayerInteractEvent.RightClickItem event) {
        CompoundEventResult<ItemStack> result = InteractionEvent.RIGHT_CLICK_ITEM.invoker().click(event.getEntity(), event.getHand());
        if (result.isPresent()) {
            event.setCanceled(true);
            event.setCancellationResult(result.result().asMinecraft());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerInteractEvent(PlayerInteractEvent.RightClickBlock event) {
        EventResult result = InteractionEvent.RIGHT_CLICK_BLOCK.invoker().click(event.getEntity(), event.getHand(), event.getPos(), event.getFace());
        if (result.isPresent()) {
            event.setCanceled(true);
            event.setCancellationResult(result.asMinecraft());
            event.setUseBlock(TriState.FALSE);
            event.setUseItem(TriState.FALSE);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerInteractEvent(PlayerInteractEvent.EntityInteract event) {
        EventResult result = InteractionEvent.INTERACT_ENTITY.invoker().interact(event.getEntity(), event.getTarget(), event.getHand());
        if (result.isPresent()) {
            event.setCanceled(true);
            event.setCancellationResult(result.asMinecraft());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerInteractEvent(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START) return;
        EventResult result = InteractionEvent.LEFT_CLICK_BLOCK.invoker().click(event.getEntity(), event.getHand(), event.getPos(), event.getFace());
        if (result.isPresent()) {
            event.setCanceled(true);
            event.setUseBlock(result.value() ? TriState.TRUE : TriState.FALSE);
            event.setUseItem(result.value() ? TriState.TRUE : TriState.FALSE);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer && event.getLevel() instanceof Level) {
            EventResult result = BlockEvent.BREAK.invoker().breakBlock((Level) event.getLevel(), event.getPos(), event.getState(), (ServerPlayer) event.getPlayer(), new IntValue() {
                @Override
                public int getAsInt() {
                    return event.getExpToDrop();
                }
                
                @Override
                public void accept(int value) {
                    event.setExpToDrop(value);
                }
            });
            if (result.isFalse()) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(EntityPlaceEvent event) {
        if (event.getLevel() instanceof Level) {
            EventResult result = BlockEvent.PLACE.invoker().placeBlock((Level) event.getLevel(), event.getPos(), event.getState(), event.getEntity());
            if (result.isFalse()) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerAboutToStartEvent event) {
        LifecycleEvent.SERVER_BEFORE_START.invoker().stateChanged(event.getServer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            PlayerEvent.CHANGE_DIMENSION.invoker().change((ServerPlayer) event.getEntity(), event.getFrom(), event.getTo());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventChunkDataEvent(ChunkDataEvent.Save event) {
        if (event.getLevel() instanceof ServerLevel) {
            ChunkEvent.SAVE_DATA.invoker().save(event.getChunk(), (ServerLevel) event.getLevel(), event.getData());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventChunkDataEvent(ChunkDataEvent.Load event) {
        LevelAccessor level = event.getChunk().getLevel();
        if (!(level instanceof ServerLevel) && event instanceof LevelEventAttachment) {
            level = ((LevelEventAttachment) event).architectury$getAttachedLevel();
        }
        ChunkEvent.LOAD_DATA.invoker().load(event.getChunk(), level instanceof ServerLevel ? (ServerLevel) level : null, event.getData());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(LootTableLoadEvent event) {
        LootEvent.MODIFY_LOOT_TABLE.invoker().modifyLootTable(ResourceKey.create(Registries.LOOT_TABLE, event.getName()), new LootTableModificationContextImpl(event.getTable()), true);
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(AttackEntityEvent event) {
        EventResult result = PlayerEvent.ATTACK_ENTITY.invoker().attack(event.getEntity(), event.getEntity().level(), event.getTarget(), event.getEntity().getUsedItemHand(), null);
        if (result.isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public interface LevelEventAttachment {
        LevelAccessor architectury$getAttachedLevel();
        
        void architectury$attachLevel(LevelAccessor level);
    }
    
    public static class ModBasedEventHandler {
        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void event(FMLCommonSetupEvent event) {
            LifecycleEvent.SETUP.invoker().run();
        }
    }
}
