/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022 architectury
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package dev.architectury.test.item;

import dev.architectury.hooks.item.tool.AxeItemHooks;
import dev.architectury.hooks.item.tool.HoeItemHooks;
import dev.architectury.hooks.item.tool.ShovelItemHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

public final class TestBlockInteractions {
    private TestBlockInteractions() {
    }

    public static void init() {
        AxeItemHooks.addStrippable(Blocks.QUARTZ_PILLAR, Blocks.OAK_LOG);
        ShovelItemHooks.addFlattenable(Blocks.IRON_ORE, Blocks.DIAMOND_BLOCK.defaultBlockState());
        HoeItemHooks.addTillable(Blocks.COAL_BLOCK, ctx -> {
            if (!ctx.getLevel().isNight()) {
                if (!ctx.getLevel().isClientSide) {
                    Player player = ctx.getPlayer();
                    if (player != null)
                        player.sendSystemMessage(Component.literal("These dark arts can only be done at night!"));
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
                    player.sendSystemMessage(Component.literal("Thou has successfully committed the dark arts of alchemy!!"));
            }
        });
    }
}
