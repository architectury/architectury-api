package me.shedaniel.architectury.registry.trade.interal;

import net.minecraft.world.entity.npc.VillagerProfession;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@ApiStatus.Internal
public class TradeRegistryData {
    public static final List<Consumer<VillagerTradeOfferContext>> VILLAGER_MODIFY_HANDLERS = new ArrayList<>();
    public static final List<Predicate<VillagerTradeOfferContext>> VILLAGER_REMOVE_HANDLERS = new ArrayList<>();
    public static final List<Consumer<WanderingTraderOfferContext>> WANDERING_TRADER_MODIFY_HANDLERS = new ArrayList<>();
    public static final List<Predicate<WanderingTraderOfferContext>> WANDERING_TRADER_REMOVE_HANDLERS = new ArrayList<>();
    
    public static final Map<VillagerProfession, Map<Integer, Integer>> VILLAGER_MAX_OFFER_OVERRIDES = new HashMap<>();
    public static Integer wanderingTraderMaxOfferOverride = null;
    
    /**
     * @param profession The Profession of the villager.
     * @param level      The level the villager needs. Vanilla range is 1 to 5, however mods may extend that upper limit further.
     * @return           Max offers for the villager. Returning null means no override exists
     */
    @Nullable
    public static Integer getVillagerMaxOffers(VillagerProfession profession, int level) {
        if(!VILLAGER_MAX_OFFER_OVERRIDES.containsKey(profession)){
            return null;
        }
        
        return VILLAGER_MAX_OFFER_OVERRIDES.get(profession).get(level);
    }
    
    /**
     * @return Max offers for the wandering trader. Returning null means no override exists
     */
    @Nullable
    public static Integer getWanderingTraderMaxOffers() {
        return wanderingTraderMaxOfferOverride;
    }
    
    public static boolean invokeVillagerOfferRemoving(VillagerTradeOfferContext ctx) {
        return VILLAGER_REMOVE_HANDLERS.stream().anyMatch(predicate -> predicate.test(ctx));
    }
    
    public static void invokeVillagerOfferModify(VillagerTradeOfferContext ctx) {
        VILLAGER_MODIFY_HANDLERS.forEach(consumer -> consumer.accept(ctx));
    }
    
    public static boolean invokeWanderingTraderOfferRemoving(WanderingTraderOfferContext ctx) {
        return WANDERING_TRADER_REMOVE_HANDLERS.stream().anyMatch(predicate -> predicate.test(ctx));
    }
    
    public static void invokeWanderingTraderOfferModify(WanderingTraderOfferContext ctx) {
        WANDERING_TRADER_MODIFY_HANDLERS.forEach(consumer -> consumer.accept(ctx));
    }
}
