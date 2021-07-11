package me.shedaniel.architectury.mixin;

import me.shedaniel.architectury.registry.trade.OfferMixingContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;


@Mixin(AbstractVillager.class)
public abstract class VillagerOfferCreationMixin extends Entity {
    public VillagerOfferCreationMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    
    @Unique
    private ThreadLocal<OfferMixingContext> offerContext = new ThreadLocal<>();
    
    @Redirect(
            method = "addOffersFromItemListings(Lnet/minecraft/world/item/trading/MerchantOffers;[Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;I)V",
            at = @At(value = "INVOKE", target = "Ljava/util/Set;iterator()Ljava/util/Iterator;")
    )
    public Iterator<Integer> overrideIterator(Set<Integer> set, MerchantOffers offers, VillagerTrades.ItemListing[] itemListings, int maxOffers) {
        OfferMixingContext context = new OfferMixingContext(maxOffers, itemListings, random);
        offerContext.set(context);
        return context.getIterator();
    }

    @ModifyVariable(
            method = "addOffersFromItemListings(Lnet/minecraft/world/item/trading/MerchantOffers;[Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;I)V",
            at = @At(value = "INVOKE_ASSIGN"),
            ordinal = 0
    )
    public MerchantOffer handleOffer(MerchantOffer offer) {
        offerContext.get().checkAndHandleMaxOfferReached();
        
        return offer;
    }
}
