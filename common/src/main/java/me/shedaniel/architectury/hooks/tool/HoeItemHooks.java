package me.shedaniel.architectury.hooks.tool;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public final class HoeItemHooks {
    private HoeItemHooks() {
    }
    
    /**
     * Adds a new tilling (interact with hoe) interaction to the game.<p>
     *
     * <b>Notes:</b>
     * <ul>
     *     <li>Blocks can only be tilled if they have no block above them.</li>
     * </ul>
     *
     * @param input input block
     * @param result resulting state
     */
    public static void addTillable(Block input, BlockState result) {
        if (HoeItem.TILLABLES instanceof ImmutableMap) {
            HoeItem.TILLABLES = new HashMap<>(HoeItem.TILLABLES);
        }
        HoeItem.TILLABLES.put(input, result);
    }
}
