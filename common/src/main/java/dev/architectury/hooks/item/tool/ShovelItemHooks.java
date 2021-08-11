package dev.architectury.hooks.item.tool;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public final class ShovelItemHooks {
    private ShovelItemHooks() {
    }

    public static void addFlatteningRecipe(Block src, BlockState dst) {
        if (ShovelItem.FLATTENABLES instanceof ImmutableMap) {
            ShovelItem.FLATTENABLES = new HashMap<>(ShovelItem.FLATTENABLES);
        }
        ShovelItem.FLATTENABLES.put(src, dst);
    }
}
