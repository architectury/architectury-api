package me.shedaniel.architectury.registry.trade;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.ApiStatus;

import java.util.Random;

public class WanderingTraderOfferContext extends TradeOfferContext {
    private final boolean rare;
    
    @ApiStatus.Internal
    public WanderingTraderOfferContext(MerchantOffer offer, boolean rare, Entity entity, Random random) {
        super(offer, entity, random);
        this.rare = rare;
    }
    
    public boolean isRare() {
        return rare;
    }
}
