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

package dev.architectury.test.attachment;

import com.mojang.serialization.Codec;
import dev.architectury.attachment.AttachmentRegistry;
import dev.architectury.attachment.AttachmentSyncPredicate;
import dev.architectury.attachment.AttachmentType;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.test.TestMod;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;

public class TestAttachments {
    public static final AttachmentType<Integer> JOIN_COUNT = AttachmentRegistry.register(
            Identifier.fromNamespaceAndPath(TestMod.MOD_ID, "join_count"),
            builder -> builder
                    .initializer(() -> 0)
                    .persistent(Codec.INT)
                    .copyOnDeath()
                    .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.targetOnly())
    );

    public static final AttachmentType<Integer> PLACED_BLOCKS = AttachmentRegistry.registerDefaulted(
            Identifier.fromNamespaceAndPath(TestMod.MOD_ID, "placed_blocks"),
            () -> 0
    );

    public static void initialize() {
        PlayerEvent.PLAYER_JOIN.register(player -> {
            Integer joins = JOIN_COUNT.modify(player, count -> count == null ? 1 : count + 1);
            TestMod.SINK.accept(player.getScoreboardName() + " has joined " + player.arch$getAttached(JOIN_COUNT) + " time(s) (previously " + joins + ")");
        });
        BlockEvent.PLACE.register((level, pos, state, placer) -> {
            if (level instanceof ServerLevel) {
                LevelChunk chunk = level.getChunkAt(pos);
                int placed = chunk.arch$getAttachedOrCreate(PLACED_BLOCKS) + 1;
                chunk.arch$setAttached(PLACED_BLOCKS, placed);
                TestMod.SINK.accept(placed + " block(s) placed in chunk " + chunk.getPos() + " since it was loaded");
            }
            return EventResult.pass();
        });
    }
}
