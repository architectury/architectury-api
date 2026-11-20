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

package dev.architectury.attachment;

import dev.architectury.extensions.injected.InjectedAttachmentHolderExtension;
import net.minecraft.server.level.ServerPlayer;

/**
 * Decides which players receive a synced attachment.
 */
@FunctionalInterface
public interface AttachmentSyncPredicate {
    /**
     * @param target the holder the attachment is attached to
     * @param player a player that could receive the value
     * @return whether the value is sent to the player
     */
    boolean test(InjectedAttachmentHolderExtension target, ServerPlayer player);

    /**
     * @return a predicate that syncs to every player tracking the holder
     */
    static AttachmentSyncPredicate all() {
        return (target, player) -> true;
    }

    /**
     * @return a predicate that syncs only to the holder itself, when the holder is a player
     */
    static AttachmentSyncPredicate targetOnly() {
        return (target, player) -> target == player;
    }

    /**
     * @return a predicate that syncs to every player tracking the holder except the holder itself
     */
    static AttachmentSyncPredicate allButTarget() {
        return (target, player) -> target != player;
    }
}
