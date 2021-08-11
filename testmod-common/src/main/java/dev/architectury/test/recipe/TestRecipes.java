package dev.architectury.test.recipe;

import dev.architectury.hooks.item.tool.AxeItemHooks;
import dev.architectury.hooks.item.tool.HoeItemHooks;
import dev.architectury.hooks.item.tool.ShovelItemHooks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

public final class TestRecipes {
    private TestRecipes() {
    }

    public static void init() {
        AxeItemHooks.addStrippingRecipe(Blocks.QUARTZ_PILLAR, Blocks.OAK_LOG);
        ShovelItemHooks.addFlatteningRecipe(Blocks.IRON_ORE, Blocks.DIAMOND_BLOCK.defaultBlockState());
        HoeItemHooks.addTillingRecipe(Blocks.COAL_BLOCK, ctx -> {
            if (!ctx.getLevel().isNight()) {
                if (!ctx.getLevel().isClientSide) {
                    Player player = ctx.getPlayer();
                    if (player != null)
                        player.sendMessage(new TextComponent("These dark arts can only be done at night!"), Util.NIL_UUID);
                }
                return false;
            }
            return true;
        }, ctx -> {
            BlockPos pos = ctx.getClickedPos();
            ctx.getLevel().setBlock(pos, Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);
            if (!ctx.getLevel().isClientSide) {
                Player player = ctx.getPlayer();
                if (player != null)
                    player.sendMessage(new TextComponent("Thou has successfully committed the dark arts of alchemy!!"), Util.NIL_UUID);
            }
        });
    }
}
