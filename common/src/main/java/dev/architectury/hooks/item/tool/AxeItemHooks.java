package dev.architectury.hooks.item.tool;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;

import java.util.HashMap;

public final class AxeItemHooks {
    private AxeItemHooks() {
    }

    public static void addStrippingRecipe(Block input, Block result) {
        if (!input.defaultBlockState().hasProperty(RotatedPillarBlock.AXIS))
            throw new IllegalArgumentException("Input block is missing required 'AXIS' property!");
        if (!result.defaultBlockState().hasProperty(RotatedPillarBlock.AXIS))
            throw new IllegalArgumentException("Result block is missing required 'AXIS' property!");
        if (AxeItem.STRIPPABLES instanceof ImmutableMap) {
            AxeItem.STRIPPABLES = new HashMap<>(AxeItem.STRIPPABLES);
        }
        AxeItem.STRIPPABLES.put(input, result);
    }
}
