package dev.architectury.hooks.item.tool;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class HoeItemHooks {
    private HoeItemHooks() {
    }
    
    /**
     * Adds a new tilling (interact with hoe) interaction to the game.<p>
     *
     * Tilling uses a predicate/consumer pair system:
     * <ul>
     *     <li>First, the game tests the context using the predicate.</li>
     *     <li>Then, if the predicate returns {@code true}, the game will, <em>on the server side only</em>,
     *         invoke the action and then damage the hoe item by 1.</li>
     *     <li>Otherwise, no action will be invoked.</li>
     * </ul>
     *
     * @param input input block
     * @param predicate context predicate
     * @param action action to run
     */
    public static void addTillable(Block input, Predicate<UseOnContext> predicate, Consumer<UseOnContext> action) {
        if (HoeItem.TILLABLES instanceof ImmutableMap) {
            HoeItem.TILLABLES = new HashMap<>(HoeItem.TILLABLES);
        }
        HoeItem.TILLABLES.put(input, new Pair<>(predicate, action));
    }
}
