package me.shedaniel.architectury.test.item;

import me.shedaniel.architectury.hooks.tool.AxeItemHooks;
import me.shedaniel.architectury.hooks.tool.HoeItemHooks;
import me.shedaniel.architectury.hooks.tool.ShovelItemHooks;
import net.minecraft.world.level.block.Blocks;

public final class TestBlockInteractions {
    private TestBlockInteractions() {
    }
    
    public static void init() {
        AxeItemHooks.addStrippable(Blocks.QUARTZ_PILLAR, Blocks.OAK_LOG);
        ShovelItemHooks.addFlattenable(Blocks.IRON_ORE, Blocks.DIAMOND_BLOCK.defaultBlockState());
        HoeItemHooks.addTillable(Blocks.COAL_BLOCK, Blocks.DIAMOND_BLOCK.defaultBlockState());
    }
}
