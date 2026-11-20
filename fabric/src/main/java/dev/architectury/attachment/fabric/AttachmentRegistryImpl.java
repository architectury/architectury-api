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

package dev.architectury.attachment.fabric;

import dev.architectury.attachment.AttachmentSyncPredicate;
import dev.architectury.attachment.AttachmentType;
import dev.architectury.extensions.injected.InjectedAttachmentHolderExtension;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.minecraft.resources.Identifier;

public class AttachmentRegistryImpl {
    public static <T> AttachmentType<T> register(Identifier id, AttachmentType.Builder<T> builder) {
        net.fabricmc.fabric.api.attachment.v1.AttachmentType<T> type = AttachmentRegistry.create(id, fabric -> {
            if (builder.getInitializer() != null) {
                fabric.initializer(builder.getInitializer());
            }
            if (builder.getCodec() != null) {
                fabric.persistent(builder.getCodec());
            }
            if (builder.isCopyOnDeath()) {
                fabric.copyOnDeath();
            }
            if (builder.getSyncCodec() != null) {
                AttachmentSyncPredicate predicate = builder.getSyncPredicate();
                fabric.syncWith(builder.getSyncCodec(), (target, player) -> predicate.test((InjectedAttachmentHolderExtension) target, player));
            }
        });
        return new FabricAttachmentType<>(type);
    }
}
