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

package me.shedaniel.architectury.event.events;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface PlayerEvent {
    Event<PlayerJoin> PLAYER_JOIN = EventFactory.createLoop(PlayerJoin.class);
    Event<PlayerQuit> PLAYER_QUIT = EventFactory.createLoop(PlayerQuit.class);
    Event<PlayerRespawn> PLAYER_RESPAWN = EventFactory.createLoop(PlayerRespawn.class);
    @Environment(EnvType.CLIENT) Event<ClientPlayerJoin> CLIENT_PLAYER_JOIN = EventFactory.createLoop(ClientPlayerJoin.class);
    @Environment(EnvType.CLIENT) Event<ClientPlayerQuit> CLIENT_PLAYER_QUIT = EventFactory.createLoop(ClientPlayerQuit.class);
    @Environment(EnvType.CLIENT) Event<ClientPlayerRespawn> CLIENT_PLAYER_RESPAWN = EventFactory.createLoop(ClientPlayerRespawn.class);
    Event<PlayerAdvancement> PLAYER_ADVANCEMENT = EventFactory.createLoop(PlayerAdvancement.class);
    Event<PlayerClone> PLAYER_CLONE = EventFactory.createLoop(PlayerClone.class);
    Event<SmeltItem> SMELT_ITEM = EventFactory.createLoop(SmeltItem.class);
    Event<PickupItemPredicate> PICKUP_ITEM_PRE = EventFactory.createInteractionResult(PickupItemPredicate.class);
    Event<PickupItem> PICKUP_ITEM_POST = EventFactory.createLoop(PickupItem.class);
    
    interface PlayerJoin {
        void join(ServerPlayer player);
    }
    
    interface PlayerQuit {
        void quit(ServerPlayer player);
    }
    
    interface PlayerRespawn {
        void respawn(ServerPlayer newPlayer, boolean conqueredEnd);
    }
    
    interface PlayerClone {
        void clone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean wonGame);
    }
    
    interface PlayerAdvancement {
        void award(ServerPlayer player, Advancement advancement);
    }
    
    interface SmeltItem {
        void smelt(Player player, ItemStack smelted);
    }
    
    interface PickupItemPredicate {
        InteractionResult canPickup(Player player, ItemEntity entity, ItemStack smelted);
    }
    
    interface PickupItem {
        void pickup(Player player, ItemEntity entity, ItemStack smelted);
    }
    
    @Environment(EnvType.CLIENT)
    interface ClientPlayerJoin {
        void join(LocalPlayer player);
    }
    
    @Environment(EnvType.CLIENT)
    interface ClientPlayerQuit {
        void quit(LocalPlayer player);
    }
    
    @Environment(EnvType.CLIENT)
    interface ClientPlayerRespawn {
        void respawn(LocalPlayer oldPlayer, LocalPlayer newPlayer);
    }
}
