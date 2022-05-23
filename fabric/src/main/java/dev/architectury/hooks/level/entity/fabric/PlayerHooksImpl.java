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

package dev.architectury.hooks.level.entity.fabric;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PlayerHooksImpl {
    public static boolean isFake(Player player) {
        var result = FakePlayers.EVENT.invoker().isFakePlayer(player);
        if (result.isPresent()) {
            return result.isTrue();
        }
        // If no result has been returned, assume that player classes extending ServerPlayer
        // (apart from ServerPlayer itself) are fake players, as a "reasonable default"
        return player instanceof ServerPlayer && player.getClass() != ServerPlayer.class;
    }
}
