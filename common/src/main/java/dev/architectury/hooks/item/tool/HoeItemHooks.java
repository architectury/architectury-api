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

    public static void addTillingRecipe(Block src, Predicate<UseOnContext> predicate, Consumer<UseOnContext> action) {
        if (HoeItem.TILLABLES instanceof ImmutableMap) {
            HoeItem.TILLABLES = new HashMap<>(HoeItem.TILLABLES);
        }
        HoeItem.TILLABLES.put(src, new Pair<>(predicate, action));
    }
}
