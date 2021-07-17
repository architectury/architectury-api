package me.shedaniel.architectury.registry.trade;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;

public class VillagerOfferContext {
    
    private final VillagerProfession profession;
    private final int level;
    private final VillagerType type;
    
    public VillagerOfferContext(VillagerProfession profession, int level, VillagerType type) {
        this.profession = profession;
        this.level = level;
        this.type = type;
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
}
