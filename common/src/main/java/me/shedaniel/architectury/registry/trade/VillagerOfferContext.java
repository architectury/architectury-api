package me.shedaniel.architectury.registry.trade;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.trading.MerchantOffer;

public class VillagerOfferContext {
    
    private final VillagerProfession profession;
    private final int level;
    private final VillagerType type;
    private final MerchantOfferAccess offer;
    
    public VillagerOfferContext(VillagerProfession profession, int level, VillagerType type, MerchantOffer offer) {
        this.profession = profession;
        this.level = level;
        this.type = type;
        this.offer = new MerchantOfferAccess(offer);
    }
    
    public VillagerProfession getProfession() {
        return profession;
    }
    
    public int getLevel() {
        return level;
    }
    
    public VillagerType getType() {
        return type;
    }
    
    public MerchantOfferAccess getOffer() {
        return offer;
    }
}
