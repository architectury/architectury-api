package dev.architectury.hooks.item.tool;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;

public final class AxeItemHooks {
    private AxeItemHooks() {
    }

    public static void addStrippingRecipe(Block src, Block dst) {
        if (AxeItem.STRIPPABLES instanceof ImmutableMap) {
            AxeItem.STRIPPABLES = new HashMap<>(AxeItem.STRIPPABLES);
        }
        AxeItem.STRIPPABLES.put(src, dst);
    }
}
