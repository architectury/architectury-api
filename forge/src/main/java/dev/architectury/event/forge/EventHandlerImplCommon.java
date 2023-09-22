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
import dev.architectury.event.EventPriority;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.*;
import dev.architectury.utils.value.IntValue;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.player.PlayerEvent.*;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import net.minecraftforge.event.level.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.event.level.BlockEvent.FarmlandTrampleEvent;
import net.minecraftforge.event.level.ChunkDataEvent;
import net.minecraftforge.event.level.ExplosionEvent.Detonate;
import net.minecraftforge.event.level.ExplosionEvent.Start;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.lang.ref.WeakReference;
import java.util.function.BiConsumer;

public class EventHandlerImplCommon {
    public static WeakReference<LootDataManager> lootDataManagerRef = null;
    
    public static void init() {
        for (EventPriority priority : EventPriority.VALUES) {
            registerWithPriority(priority, ServerTickEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, LevelTickEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, ServerStartingEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, ServerStartedEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, ServerStoppingEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, ServerStoppedEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, RegisterCommandsEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, PlayerLoggedInEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, PlayerLoggedOutEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, PlayerRespawnEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, CommandEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, PlayerTickEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, LevelEvent.Load.class, EventHandlerImplCommon::eventWorldEvent);
            registerWithPriority(priority, LevelEvent.Unload.class, EventHandlerImplCommon::eventWorldEvent);
            registerWithPriority(priority, LevelEvent.Save.class, EventHandlerImplCommon::eventWorldEvent);
            registerWithPriority(priority, LivingDeathEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, AdvancementEvent.AdvancementEarnEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, Clone.class, EventHandlerImplCommon::eventPlayerEvent);
            registerWithPriority(priority, Start.class, EventHandlerImplCommon::eventExplosionEvent);
            registerWithPriority(priority, Detonate.class, EventHandlerImplCommon::eventExplosionEvent);
            registerWithPriority(priority, LivingAttackEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, EntityJoinLevelEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, FarmlandTrampleEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, FillBucketEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, MobSpawnEvent.FinalizeSpawn.class, EventHandlerImplCommon::eventLivingSpawnEvent);
            registerWithPriority(priority, AnimalTameEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, ItemCraftedEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, ItemSmeltedEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, EntityItemPickupEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, ItemPickupEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, ItemTossEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, PlayerContainerEvent.Open.class, EventHandlerImplCommon::eventPlayerContainerEvent);
            registerWithPriority(priority, PlayerContainerEvent.Close.class, EventHandlerImplCommon::eventPlayerContainerEvent);
            registerWithPriority(priority, PlayerInteractEvent.RightClickItem.class, EventHandlerImplCommon::eventPlayerInteractEvent);
            registerWithPriority(priority, PlayerInteractEvent.RightClickBlock.class, EventHandlerImplCommon::eventPlayerInteractEvent);
            registerWithPriority(priority, PlayerInteractEvent.EntityInteract.class, EventHandlerImplCommon::eventPlayerInteractEvent);
            registerWithPriority(priority, PlayerInteractEvent.LeftClickBlock.class, EventHandlerImplCommon::eventPlayerInteractEvent);
            registerWithPriority(priority, BreakEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, EntityPlaceEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, ServerAboutToStartEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, PlayerChangedDimensionEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, ChunkDataEvent.Save.class, EventHandlerImplCommon::eventChunkDataEvent);
            registerWithPriority(priority, ChunkDataEvent.Load.class, EventHandlerImplCommon::eventChunkDataEvent);
            registerWithPriority(priority, LootTableLoadEvent.class, EventHandlerImplCommon::event);
            registerWithPriority(priority, AttackEntityEvent.class, EventHandlerImplCommon::event);
        }
        registerWithPriority(EventPriority.HIGHEST, ServerChatEvent.class, (serverChatEvent, priority) -> event(serverChatEvent));
        registerWithPriority(EventPriority.LOWEST, ServerChatEvent.class, (serverChatEvent, priority) -> eventAfter(serverChatEvent));
    }
    
    public static void event(ServerTickEvent event, EventPriority priority) {
        if (event.phase == Phase.START)
            TickEvent.SERVER_PRE.invoker(priority).tick(ServerLifecycleHooks.getCurrentServer());
        else if (event.phase == Phase.END)
            TickEvent.SERVER_POST.invoker(priority).tick(ServerLifecycleHooks.getCurrentServer());
    }
    
    public static void event(LevelTickEvent event, EventPriority priority) {
        if (event.side == LogicalSide.SERVER) {
            if (event.phase == Phase.START)
                TickEvent.SERVER_LEVEL_PRE.invoker(priority).tick((ServerLevel) event.level);
            else if (event.phase == Phase.END)
                TickEvent.SERVER_LEVEL_POST.invoker(priority).tick((ServerLevel) event.level);
        }
    }
    
    public static void event(ServerStartingEvent event, EventPriority priority) {
        LifecycleEvent.SERVER_STARTING.invoker(priority).stateChanged(event.getServer());
    }
    
    public static void event(ServerStartedEvent event, EventPriority priority) {
        LifecycleEvent.SERVER_STARTED.invoker(priority).stateChanged(event.getServer());
    }
    
    public static void event(ServerStoppingEvent event, EventPriority priority) {
        LifecycleEvent.SERVER_STOPPING.invoker(priority).stateChanged(event.getServer());
    }
    
    public static void event(ServerStoppedEvent event, EventPriority priority) {
        LifecycleEvent.SERVER_STOPPED.invoker(priority).stateChanged(event.getServer());
    }
    
    public static void event(RegisterCommandsEvent event, EventPriority priority) {
        CommandRegistrationEvent.EVENT.invoker(priority).register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }
    
    public static void event(PlayerLoggedInEvent event, EventPriority priority) {
        PlayerEvent.PLAYER_JOIN.invoker(priority).join((ServerPlayer) event.getEntity());
    }
    
    public static void event(PlayerLoggedOutEvent event, EventPriority priority) {
        PlayerEvent.PLAYER_QUIT.invoker(priority).quit((ServerPlayer) event.getEntity());
    }
    
    public static void event(PlayerRespawnEvent event, EventPriority priority) {
        PlayerEvent.PLAYER_RESPAWN.invoker(priority).respawn((ServerPlayer) event.getEntity(), event.isEndConquered());
    }
    
    public static void event(CommandEvent event, EventPriority priority) {
        CommandPerformEvent performEvent = new CommandPerformEvent(event.getParseResults(), event.getException());
        if (CommandPerformEvent.EVENT.invoker(priority).act(performEvent).isFalse()) {
            event.setCanceled(true);
        }
        event.setParseResults(performEvent.getResults());
        event.setException(performEvent.getThrowable());
    }
    
    public static void event(PlayerTickEvent event, EventPriority priority) {
        if (event.phase == Phase.START) {
            TickEvent.PLAYER_PRE.invoker(priority).tick(event.player);
        } else if (event.phase == Phase.END) {
            TickEvent.PLAYER_POST.invoker(priority).tick(event.player);
        }
    }
    
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
    
    public static void eventAfter(ServerChatEvent event) {
        EventResult process = ChatEvent.RECEIVED.invoker().received(event.getPlayer(), event.getMessage());
        if (process.isFalse())
            event.setCanceled(true);
    }
    
    public static void eventWorldEvent(LevelEvent.Load event, EventPriority priority) {
        if (event.getLevel() instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) event.getLevel();
            LifecycleEvent.SERVER_LEVEL_LOAD.invoker(priority).act(world);
        }
    }
    
    public static void eventWorldEvent(LevelEvent.Unload event, EventPriority priority) {
        if (event.getLevel() instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) event.getLevel();
            LifecycleEvent.SERVER_LEVEL_UNLOAD.invoker(priority).act(world);
        }
    }
    
    public static void eventWorldEvent(LevelEvent.Save event, EventPriority priority) {
        if (event.getLevel() instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) event.getLevel();
            LifecycleEvent.SERVER_LEVEL_SAVE.invoker(priority).act(world);
        }
    }
    
    public static void event(LivingDeathEvent event, EventPriority priority) {
        if (EntityEvent.LIVING_DEATH.invoker(priority).die(event.getEntity(), event.getSource()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void event(AdvancementEvent.AdvancementEarnEvent event, EventPriority priority) {
        if (event.getEntity() instanceof ServerPlayer) {
            PlayerEvent.PLAYER_ADVANCEMENT.invoker(priority).award((ServerPlayer) event.getEntity(), event.getAdvancement());
        }
    }
    
    public static void eventPlayerEvent(Clone event, EventPriority priority) {
        if (event.getOriginal() instanceof ServerPlayer && event.getEntity() instanceof ServerPlayer) {
            PlayerEvent.PLAYER_CLONE.invoker(priority).clone((ServerPlayer) event.getOriginal(), (ServerPlayer) event.getEntity(), !event.isWasDeath());
        }
    }
    
    public static void eventExplosionEvent(Start event, EventPriority priority) {
        if (ExplosionEvent.PRE.invoker(priority).explode(event.getLevel(), event.getExplosion()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void eventExplosionEvent(Detonate event, EventPriority priority) {
        ExplosionEvent.DETONATE.invoker(priority).explode(event.getLevel(), event.getExplosion(), event.getAffectedEntities());
    }
    
    public static void event(LivingAttackEvent event, EventPriority priority) {
        if (EntityEvent.LIVING_HURT.invoker(priority).hurt(event.getEntity(), event.getSource(), event.getAmount()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void event(EntityJoinLevelEvent event, EventPriority priority) {
        if (EntityEvent.ADD.invoker(priority).add(event.getEntity(), event.getLevel()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void event(FarmlandTrampleEvent event, EventPriority priority) {
        if (event.getLevel() instanceof Level && InteractionEvent.FARMLAND_TRAMPLE.invoker(priority).trample((Level) event.getLevel(), event.getPos(), event.getState(), event.getFallDistance(), event.getEntity()).value() != null) {
            event.setCanceled(true);
        }
    }
    
    public static void event(FillBucketEvent event, EventPriority priority) {
        ItemStack oldItem = event.getEmptyBucket();
        CompoundEventResult<ItemStack> result = PlayerEvent.FILL_BUCKET.invoker(priority).fill(event.getEntity(), event.getLevel(), oldItem, event.getTarget());
        if (result.interruptsFurtherEvaluation()) {
            event.setCanceled(true);
            event.setFilledBucket(result.object());
            
            if (result.value() != null) {
                event.setResult(result.value() ? Event.Result.ALLOW : Event.Result.DENY);
            }
        }
    }
    
    // TODO: Hook ourselves when mixin is available
    //    @SubscribeEvent(priority = EventPriority.HIGH)
    //    public static void event(EnteringChunk event) {
    //        EntityEvent.ENTER_SECTION.invoker().enterChunk(event.getEntity(), event.getNewChunkX(), event.getNewChunkZ(), event.getOldChunkX(), event.getOldChunkZ());
    //    }
    
    public static void eventLivingSpawnEvent(MobSpawnEvent.FinalizeSpawn event, EventPriority priority) {
        EventResult result = EntityEvent.LIVING_CHECK_SPAWN.invoker(priority).canSpawn(event.getEntity(), event.getLevel(), event.getX(), event.getY(), event.getZ(), event.getSpawnType(), event.getSpawner());
        if (result.interruptsFurtherEvaluation()) {
            if (!result.isEmpty()) {
                event.setSpawnCancelled(result.value());
            }
        }
    }
    
    public static void event(AnimalTameEvent event, EventPriority priority) {
        EventResult result = EntityEvent.ANIMAL_TAME.invoker(priority).tame(event.getAnimal(), event.getTamer());
        event.setCanceled(result.isFalse());
    }
    
    public static void event(ItemCraftedEvent event, EventPriority priority) {
        PlayerEvent.CRAFT_ITEM.invoker(priority).craft(event.getEntity(), event.getCrafting(), event.getInventory());
    }
    
    public static void event(ItemSmeltedEvent event, EventPriority priority) {
        PlayerEvent.SMELT_ITEM.invoker(priority).smelt(event.getEntity(), event.getSmelting());
    }
    
    public static void event(EntityItemPickupEvent event, EventPriority priority) {
        // note: this event is weird, cancel is equivalent to denying the pickup,
        // and setting the result to ALLOW will pick it up without adding it to the player's inventory
        var result = PlayerEvent.PICKUP_ITEM_PRE.invoker(priority).canPickup(event.getEntity(), event.getItem(), event.getItem().getItem());
        if (result.isFalse()) {
            event.setCanceled(true);
        } else if (result.isTrue()) {
            event.setResult(Event.Result.ALLOW);
        }
    }
    
    public static void event(ItemPickupEvent event, EventPriority priority) {
        PlayerEvent.PICKUP_ITEM_POST.invoker(priority).pickup(event.getEntity(), event.getOriginalEntity(), event.getStack());
    }
    
    public static void event(ItemTossEvent event, EventPriority priority) {
        PlayerEvent.DROP_ITEM.invoker(priority).drop(event.getPlayer(), event.getEntity());
    }
    
    public static void eventPlayerContainerEvent(PlayerContainerEvent.Open event, EventPriority priority) {
        PlayerEvent.OPEN_MENU.invoker(priority).open(event.getEntity(), event.getContainer());
    }
    
    public static void eventPlayerContainerEvent(PlayerContainerEvent.Close event, EventPriority priority) {
        PlayerEvent.CLOSE_MENU.invoker(priority).close(event.getEntity(), event.getContainer());
    }
    
    public static void eventPlayerInteractEvent(PlayerInteractEvent.RightClickItem event, EventPriority priority) {
        CompoundEventResult<ItemStack> result = InteractionEvent.RIGHT_CLICK_ITEM.invoker(priority).click(event.getEntity(), event.getHand());
        if (result.isPresent()) {
            event.setCanceled(true);
            event.setCancellationResult(result.result().asMinecraft());
        }
    }
    
    public static void eventPlayerInteractEvent(PlayerInteractEvent.RightClickBlock event, EventPriority priority) {
        EventResult result = InteractionEvent.RIGHT_CLICK_BLOCK.invoker(priority).click(event.getEntity(), event.getHand(), event.getPos(), event.getFace());
        if (result.isPresent()) {
            event.setCanceled(true);
            event.setCancellationResult(result.asMinecraft());
            event.setUseBlock(Event.Result.DENY);
            event.setUseItem(Event.Result.DENY);
        }
    }
    
    public static void eventPlayerInteractEvent(PlayerInteractEvent.EntityInteract event, EventPriority priority) {
        EventResult result = InteractionEvent.INTERACT_ENTITY.invoker(priority).interact(event.getEntity(), event.getTarget(), event.getHand());
        if (result.isPresent()) {
            event.setCanceled(true);
            event.setCancellationResult(result.asMinecraft());
        }
    }
    
    public static void eventPlayerInteractEvent(PlayerInteractEvent.LeftClickBlock event, EventPriority priority) {
        EventResult result = InteractionEvent.LEFT_CLICK_BLOCK.invoker(priority).click(event.getEntity(), event.getHand(), event.getPos(), event.getFace());
        if (result.isPresent()) {
            event.setCanceled(true);
            event.setCancellationResult(result.asMinecraft());
            event.setUseBlock(Event.Result.DENY);
            event.setUseItem(Event.Result.DENY);
        }
    }
    
    public static void event(BreakEvent event, EventPriority priority) {
        if (event.getPlayer() instanceof ServerPlayer && event.getLevel() instanceof Level) {
            EventResult result = BlockEvent.BREAK.invoker(priority).breakBlock((Level) event.getLevel(), event.getPos(), event.getState(), (ServerPlayer) event.getPlayer(), new IntValue() {
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
    
    public static void event(EntityPlaceEvent event, EventPriority priority) {
        if (event.getLevel() instanceof Level) {
            EventResult result = BlockEvent.PLACE.invoker(priority).placeBlock((Level) event.getLevel(), event.getPos(), event.getState(), event.getEntity());
            if (result.isFalse()) {
                event.setCanceled(true);
            }
        }
    }
    
    public static void event(ServerAboutToStartEvent event, EventPriority priority) {
        LifecycleEvent.SERVER_BEFORE_START.invoker(priority).stateChanged(event.getServer());
    }
    
    public static void event(PlayerChangedDimensionEvent event, EventPriority priority) {
        if (event.getEntity() instanceof ServerPlayer) {
            PlayerEvent.CHANGE_DIMENSION.invoker(priority).change((ServerPlayer) event.getEntity(), event.getFrom(), event.getTo());
        }
    }
    
    public static void eventChunkDataEvent(ChunkDataEvent.Save event, EventPriority priority) {
        if (event.getLevel() instanceof ServerLevel) {
            ChunkEvent.SAVE_DATA.invoker(priority).save(event.getChunk(), (ServerLevel) event.getLevel(), event.getData());
        }
    }
    
    public static void eventChunkDataEvent(ChunkDataEvent.Load event, EventPriority priority) {
        LevelAccessor level = event.getChunk().getWorldForge();
        if (!(level instanceof ServerLevel) && event instanceof LevelEventAttachment) {
            level = ((LevelEventAttachment) event).architectury$getAttachedLevel();
        }
        ChunkEvent.LOAD_DATA.invoker(priority).load(event.getChunk(), level instanceof ServerLevel ? (ServerLevel) level : null, event.getData());
    }
    
    public static void event(LootTableLoadEvent event, EventPriority priority) {
        LootEvent.MODIFY_LOOT_TABLE.invoker(priority).modifyLootTable(lootDataManagerRef == null ? null : lootDataManagerRef.get(), event.getName(), new LootTableModificationContextImpl(event.getTable()), true);
    }
    
    public static void event(AttackEntityEvent event, EventPriority priority) {
        EventResult result = PlayerEvent.ATTACK_ENTITY.invoker(priority).attack(event.getEntity(), event.getEntity().level(), event.getTarget(), event.getEntity().getUsedItemHand(), null);
        if (result.isFalse()) {
            event.setCanceled(true);
        }
    }
    
    private static <T extends Event> void registerWithPriority(EventPriority priority, Class<T> type, BiConsumer<T, EventPriority> callback) {
        net.minecraftforge.eventbus.api.EventPriority forgePriority = net.minecraftforge.eventbus.api.EventPriority.valueOf(priority.name());
        MinecraftForge.EVENT_BUS.addListener(forgePriority, false, type, event -> callback.accept(event, priority));
    }
    
    public interface LevelEventAttachment {
        LevelAccessor architectury$getAttachedLevel();
        
        void architectury$attachLevel(LevelAccessor level);
    }
    
    public static class ModBasedEventHandler {
        public static void init(IEventBus bus) {
            for (EventPriority priority : EventPriority.VALUES) {
                registerWithPriority(bus, priority, FMLCommonSetupEvent.class, ModBasedEventHandler::event);
            }
        }
        
        public static void event(FMLCommonSetupEvent event, EventPriority priority) {
            LifecycleEvent.SETUP.invoker(priority).run();
        }
        
        private static <T extends Event> void registerWithPriority(IEventBus bus, EventPriority priority, Class<T> type, BiConsumer<T, EventPriority> callback) {
            net.minecraftforge.eventbus.api.EventPriority forgePriority = net.minecraftforge.eventbus.api.EventPriority.valueOf(priority.name());
            bus.addListener(forgePriority, false, type, event -> callback.accept(event, priority));
        }
    }
}
