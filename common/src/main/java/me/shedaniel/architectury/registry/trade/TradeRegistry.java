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

package me.shedaniel.architectury.registry.trade;
import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TradeRegistry {
    private static final List<Consumer<VillagerOfferContext>> VILLAGER_MODIFY_HANDLERS = new ArrayList<>();
    private static final List<Predicate<VillagerOfferContext>> VILLAGER_REMOVE_HANDLERS = new ArrayList<>();
    
    private TradeRegistry() {
    }
    
    /**
     * Register a trade ({@link VillagerTrades.ItemListing}) for a villager by its profession and level.
     * When the mod loader is Forge, the {@code VillagerTradesEvent} event is used.
     *
     * @param profession The Profession the villager needs to have this trade.
     * @param level      The level the villager needs. Vanilla range is 1 to 5, however mods may extend that upper limit further.
     * @param trades     The trades to add to this profession at the specified level.
     */
    public static void registerVillagerTrade(VillagerProfession profession, int level, VillagerTrades.ItemListing... trades) {
        if (level < 1) {
            throw new IllegalArgumentException("Villager Trade level has to be at least 1!");
        }
        registerVillagerTrade0(profession, level, trades);
    }
    
    @ExpectPlatform
    private static void registerVillagerTrade0(VillagerProfession profession, int level, VillagerTrades.ItemListing... trades) {
        throw new AssertionError();
    }
    
    // TODO Doc
    public static void registerVillagerOfferModify(Consumer<VillagerOfferContext> consumer) {
        Objects.requireNonNull(consumer);
        VILLAGER_MODIFY_HANDLERS.add(consumer);
    }
    
    // TODO Doc
    public static void registerVillagerOfferRemoving(Predicate<VillagerOfferContext> predicate) {
        Objects.requireNonNull(predicate);
        VILLAGER_REMOVE_HANDLERS.add(predicate);
    }
    
    /**
     * Register a trade ({@link VillagerTrades.ItemListing}) to a wandering trader by its rarity.
     * When the mod loader is Forge, the {@code WandererTradesEvent} event is used.
     *
     * @param rare   Whether this trade is "rare". Rare trades have a five times lower chance of being used.
     * @param trades The trades to add to the wandering trader.
     */
    @ExpectPlatform
    public static void registerTradeForWanderingTrader(boolean rare, VillagerTrades.ItemListing... trades) {
        throw new AssertionError();
    }
    
    public static boolean invokeVillagerOfferRemoving(VillagerOfferContext ctx) {
        return VILLAGER_REMOVE_HANDLERS.stream().anyMatch(predicate -> predicate.test(ctx));
    }
    
    public static void invokeVillagerOfferModify(VillagerOfferContext ctx) {
        VILLAGER_MODIFY_HANDLERS.forEach(consumer -> consumer.accept(ctx));
    }
}
