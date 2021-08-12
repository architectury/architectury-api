package dev.architectury.hooks.item.tool;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public final class ShovelItemHooks {
    private ShovelItemHooks() {
    }
    
    /**
     * Adds a new flattening (interact with shovel) recipe to the game.<p>
     *
     * <b>Notes:</b>
     * <ul>
     *     <li>Blocks can only be flattened if they have no block above them.</li>
     *     <li>{@linkplain net.minecraft.world.level.block.CampfireBlock Campfires} have a special case for being extinguished by a shovel,
     *     though you <em>can</em> override that using this method due to the check order.</li>
     * </ul>
     *
     * @param input input block
     * @param result result block state
     */
    public static void addFlatteningRecipe(Block input, BlockState result) {
        if (ShovelItem.FLATTENABLES instanceof ImmutableMap) {
            ShovelItem.FLATTENABLES = new HashMap<>(ShovelItem.FLATTENABLES);
        }
        ShovelItem.FLATTENABLES.put(input, result);
    }
}
