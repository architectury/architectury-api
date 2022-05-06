package dev.architectury.hooks.item.tool.fabric;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class HoeItemHooksImpl {
    public static void addTillable(Block input, Predicate<UseOnContext> predicate, Consumer<UseOnContext> action, Function<UseOnContext, BlockState> newState) {
        if (HoeItem.TILLABLES instanceof ImmutableMap) {
            HoeItem.TILLABLES = new HashMap<>(HoeItem.TILLABLES);
        }
        HoeItem.TILLABLES.put(input, new Pair<>(predicate, useOnContext -> {
            action.accept(useOnContext);
            BlockState state = newState.apply(useOnContext);
            useOnContext.getLevel().setBlock(useOnContext.getClickedPos(), state, 11);
        }));
    }
}
