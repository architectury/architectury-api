package dev.architectury.hooks.item.tool;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;

import java.util.HashMap;

public final class AxeItemHooks {
    private AxeItemHooks() {
    }
    
    /**
     * Adds a new stripping (interact with axe) interaction to the game.<p>
     *
     * Note that both the input block and the result block <em>must</em> have the
     * {@link net.minecraft.world.level.block.state.properties.BlockStateProperties#AXIS AXIS} property,
     * and that the value of this property will be copied from the input block to the result block when the recipe
     * is performed.
     *
     * @param input input block
     * @param result result block
     * @throws IllegalArgumentException if the input or result blocks do not have the
     * {@link net.minecraft.world.level.block.state.properties.BlockStateProperties#AXIS AXIS} property.
     */
    public static void addStrippable(Block input, Block result) {
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
