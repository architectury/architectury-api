package me.shedaniel.architectury.mixin;

import me.shedaniel.architectury.registry.trade.MerchantOfferAccess;
import me.shedaniel.architectury.registry.trade.TradeRegistry;
import me.shedaniel.architectury.registry.trade.VillagerOfferContext;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Villager.class)
public abstract class VillagerMixin extends VillagerOfferCreationMixin {
    
    public VillagerMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    
    @Shadow
    public abstract VillagerData getVillagerData();
    
    @Override
    public MerchantOffer handleOfferImpl(MerchantOffer offer) {
        VillagerData vd = getVillagerData();
    
        VillagerOfferContext context = new VillagerOfferContext(vd.getProfession(), vd.getLevel(), vd.getType(), offer);
        
        boolean removeResult = TradeRegistry.invokeVillagerOfferRemoving(context);
        if(removeResult) {
            return null;
        }
        
        TradeRegistry.invokeVillagerOfferModify(context);
        return offer;
    }
}
