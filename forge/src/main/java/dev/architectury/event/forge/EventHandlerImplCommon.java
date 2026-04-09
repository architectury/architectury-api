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

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.*;
import dev.architectury.utils.value.IntValue;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.util.Result;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
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
import net.minecraftforge.eventbus.api.listener.Priority;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class EventHandlerImplCommon {
    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(net.minecraftforge.event.TickEvent.ServerTickEvent.Pre event) {
        TickEvent.SERVER_PRE.invoker().tick(ServerLifecycleHooks.getCurrentServer());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(net.minecraftforge.event.TickEvent.ServerTickEvent.Post event) {
        TickEvent.SERVER_POST.invoker().tick(ServerLifecycleHooks.getCurrentServer());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(net.minecraftforge.event.TickEvent.LevelTickEvent.Pre event) {
        if (event.side() == LogicalSide.SERVER) {
            TickEvent.SERVER_LEVEL_PRE.invoker().tick((ServerLevel) event.level());
        }
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(net.minecraftforge.event.TickEvent.LevelTickEvent.Post event) {
        if (event.side() == LogicalSide.SERVER) {
            TickEvent.SERVER_LEVEL_POST.invoker().tick((ServerLevel) event.level());
        }
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(ServerStartingEvent event) {
        LifecycleEvent.SERVER_STARTING.invoker().stateChanged(event.getServer());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(ServerStartedEvent event) {
        LifecycleEvent.SERVER_STARTED.invoker().stateChanged(event.getServer());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(ServerStoppingEvent event) {
        LifecycleEvent.SERVER_STOPPING.invoker().stateChanged(event.getServer());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(ServerStoppedEvent event) {
        LifecycleEvent.SERVER_STOPPED.invoker().stateChanged(event.getServer());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(RegisterCommandsEvent event) {
        CommandRegistrationEvent.EVENT.invoker().register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(PlayerLoggedInEvent event) {
        PlayerEvent.PLAYER_JOIN.invoker().join((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(PlayerLoggedOutEvent event) {
        PlayerEvent.PLAYER_QUIT.invoker().quit((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(PlayerRespawnEvent event) {
        PlayerEvent.PLAYER_RESPAWN.invoker().respawn((ServerPlayer) event.getEntity(), event.isEndConquered(), event.getEntity().getRemovalReason());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean event(CommandEvent event) {
        CommandPerformEvent performEvent = new CommandPerformEvent(event.getParseResults(), event.getException());
        boolean cancelled = CommandPerformEvent.EVENT.invoker().act(performEvent).isFalse();
        event.setParseResults(performEvent.getResults());
        event.setException(performEvent.getThrowable());
        return cancelled;
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(net.minecraftforge.event.TickEvent.PlayerTickEvent.Pre event) {
        TickEvent.PLAYER_PRE.invoker().tick(event.player());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(net.minecraftforge.event.TickEvent.PlayerTickEvent.Post event) {
        TickEvent.PLAYER_POST.invoker().tick(event.player());
    }

    @SubscribeEvent(priority = Priority.HIGH)
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

    @SubscribeEvent(priority = Priority.LOWEST)
    public static boolean eventAfter(ServerChatEvent event) {
        EventResult process = ChatEvent.RECEIVED.invoker().received(event.getPlayer(), event.getMessage());
        return process.isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventWorldEvent(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel) {
            LifecycleEvent.SERVER_LEVEL_LOAD.invoker().act((ServerLevel) event.getLevel());
        }
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventWorldEvent(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel) {
            LifecycleEvent.SERVER_LEVEL_UNLOAD.invoker().act((ServerLevel) event.getLevel());
        }
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventWorldEvent(LevelEvent.Save event) {
        if (event.getLevel() instanceof ServerLevel) {
            LifecycleEvent.SERVER_LEVEL_SAVE.invoker().act((ServerLevel) event.getLevel());
        }
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean event(LivingDeathEvent event) {
        return EntityEvent.LIVING_DEATH.invoker().die(event.getEntity(), event.getSource()).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(AdvancementEvent.AdvancementEarnEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            PlayerEvent.PLAYER_ADVANCEMENT.invoker().award((ServerPlayer) event.getEntity(), event.getAdvancement());
        }
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventPlayerEvent(Clone event) {
        if (event.getOriginal() instanceof ServerPlayer && event.getEntity() instanceof ServerPlayer) {
            PlayerEvent.PLAYER_CLONE.invoker().clone((ServerPlayer) event.getOriginal(), (ServerPlayer) event.getEntity(), !event.isWasDeath());
        }
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventExplosionEvent(Start event) {
        return ExplosionEvent.PRE.invoker().explode(event.getLevel(), event.getExplosion()).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventExplosionEvent(Detonate event) {
        ExplosionEvent.DETONATE.invoker().explode(event.getLevel(), event.getExplosion(), event.getAffectedEntities());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean event(LivingAttackEvent event) {
        return EntityEvent.LIVING_HURT.invoker().hurt(event.getEntity(), event.getSource(), event.getAmount()).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean event(EntityJoinLevelEvent event) {
        return EntityEvent.ADD.invoker().add(event.getEntity(), event.getLevel()).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean event(FarmlandTrampleEvent event) {
        return event.getLevel() instanceof Level level
                && InteractionEvent.FARMLAND_TRAMPLE.invoker().trample(level, event.getPos(), event.getState(), event.getFallDistance(), event.getEntity()) != InteractionResult.PASS;
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventLivingSpawnEvent(MobSpawnEvent.FinalizeSpawn event) {
        EventResult result = EntityEvent.LIVING_CHECK_SPAWN.invoker().canSpawn(event.getEntity(), event.getLevel(), event.getX(), event.getY(), event.getZ(), event.getSpawnReason(), event.getSpawner());
        if (result.interruptsFurtherEvaluation() && !result.isEmpty()) {
            event.setSpawnCancelled(result.value());
        }
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean event(AnimalTameEvent event) {
        EventResult result = EntityEvent.ANIMAL_TAME.invoker().tame(event.getAnimal(), event.getTamer());
        return result.isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(ItemCraftedEvent event) {
        PlayerEvent.CRAFT_ITEM.invoker().craft(event.getEntity(), event.getCrafting(), event.getContainer());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(ItemSmeltedEvent event) {
        PlayerEvent.SMELT_ITEM.invoker().smelt(event.getEntity(), event.getSmelting());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean event(EntityItemPickupEvent event) {
        var result = PlayerEvent.PICKUP_ITEM_PRE.invoker().canPickup(event.getEntity(), event.getItem(), event.getItem().getItem());
        if (result.isFalse()) {
            return true;
        }
        if (result.isTrue()) {
            event.setResult(Result.ALLOW);
        }
        return false;
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(ItemPickupEvent event) {
        PlayerEvent.PICKUP_ITEM_POST.invoker().pickup(event.getEntity(), event.getOriginalEntity(), event.getStack());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(ItemTossEvent event) {
        PlayerEvent.DROP_ITEM.invoker().drop(event.getPlayer(), event.getEntity());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventPlayerContainerEvent(PlayerContainerEvent.Open event) {
        PlayerEvent.OPEN_MENU.invoker().open(event.getEntity(), event.getContainer());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventPlayerContainerEvent(PlayerContainerEvent.Close event) {
        PlayerEvent.CLOSE_MENU.invoker().close(event.getEntity(), event.getContainer());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventPlayerInteractEvent(PlayerInteractEvent.RightClickItem event) {
        InteractionResult result = InteractionEvent.RIGHT_CLICK_ITEM.invoker().click(event.getEntity(), event.getHand());
        if (result == InteractionResult.PASS) {
            return false;
        }
        event.setCancellationResult(result);
        return true;
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventPlayerInteractEvent(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult result = InteractionEvent.RIGHT_CLICK_BLOCK.invoker().click(event.getEntity(), event.getHand(), event.getPos(), event.getFace());
        if (result == InteractionResult.PASS) {
            return false;
        }
        event.setCancellationResult(result);
        event.setUseBlock(Result.DENY);
        event.setUseItem(Result.DENY);
        return true;
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventPlayerInteractEvent(PlayerInteractEvent.EntityInteractSpecific event) {
        EventResult result = InteractionEvent.INTERACT_ENTITY.invoker().interact(event.getEntity(), event.getTarget(), event.getHand());
        if (!result.isPresent()) {
            return false;
        }
        event.setCancellationResult(result.asMinecraft());
        return true;
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventPlayerInteractEvent(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START) {
            return false;
        }
        InteractionResult result = InteractionEvent.LEFT_CLICK_BLOCK.invoker().click(event.getEntity(), event.getHand(), event.getPos(), event.getFace());
        if (result == InteractionResult.PASS) {
            return false;
        }
        event.setUseBlock(result.consumesAction() ? Result.ALLOW : Result.DENY);
        event.setUseItem(result.consumesAction() ? Result.ALLOW : Result.DENY);
        return true;
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean event(BreakEvent event) {
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
            return result.isFalse();
        }
        return false;
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean event(EntityPlaceEvent event) {
        if (event.getLevel() instanceof Level) {
            EventResult result = BlockEvent.PLACE.invoker().placeBlock((Level) event.getLevel(), event.getPos(), event.getState(), event.getEntity());
            return result.isFalse();
        }
        return false;
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(ServerAboutToStartEvent event) {
        LifecycleEvent.SERVER_BEFORE_START.invoker().stateChanged(event.getServer());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            PlayerEvent.CHANGE_DIMENSION.invoker().change((ServerPlayer) event.getEntity(), event.getFrom(), event.getTo());
        }
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventChunkDataEvent(ChunkDataEvent.Save event) {
        if (event.getLevel() instanceof ServerLevel) {
            ChunkEvent.SAVE_DATA.invoker().save(event.getChunk(), (ServerLevel) event.getLevel(), event.getData());
        }
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventChunkDataEvent(ChunkDataEvent.Load event) {
        LevelAccessor level = event.getChunk().getWorldForge();
        ChunkEvent.LOAD_DATA.invoker().load(event.getChunk(), level instanceof ServerLevel ? (ServerLevel) level : null, event.getData());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(LootTableLoadEvent event) {
        LootEvent.MODIFY_LOOT_TABLE.invoker().modifyLootTable(ResourceKey.create(Registries.LOOT_TABLE, event.getName()), new LootTableModificationContextImpl(event.getTable()), true);
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean event(AttackEntityEvent event) {
        EventResult result = PlayerEvent.ATTACK_ENTITY.invoker().attack(event.getEntity(), event.getEntity().level(), event.getTarget(), event.getEntity().getUsedItemHand(), null);
        return result.isFalse();
    }

    public interface LevelEventAttachment {
        LevelAccessor architectury$getAttachedLevel();

        void architectury$attachLevel(LevelAccessor level);
    }

    public static class ModBasedEventHandler {
        @SubscribeEvent(priority = Priority.HIGH)
        public static void event(FMLCommonSetupEvent event) {
            LifecycleEvent.SETUP.invoker().run();
        }
    }
}
