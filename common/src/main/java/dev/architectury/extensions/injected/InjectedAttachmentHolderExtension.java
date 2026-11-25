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

package dev.architectury.extensions.injected;

import dev.architectury.attachment.AttachmentType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.function.UnaryOperator;

/**
 * Something that {@link AttachmentType data attachments} can be attached to.
 * Injected into {@link net.minecraft.world.entity.Entity}, {@link net.minecraft.world.level.block.entity.BlockEntity},
 * {@link net.minecraft.world.level.chunk.ChunkAccess} and {@link net.minecraft.world.level.Level}.
 * <p>
 * Every method here is a shortcut for the matching method of {@link AttachmentType}.
 */
public interface InjectedAttachmentHolderExtension {
    /**
     * @see AttachmentType#get
     */
    @Nullable
    default <T> T arch$getAttached(AttachmentType<T> type) {
        return type.get(this);
    }

    /**
     * @see AttachmentType#getOrCreate
     */
    default <T> T arch$getAttachedOrCreate(AttachmentType<T> type) {
        return type.getOrCreate(this);
    }

    /**
     * @see AttachmentType#getOrElse
     */
    @Contract("_, !null -> !null")
    default <T> T arch$getAttachedOrElse(AttachmentType<T> type, @Nullable T fallback) {
        return type.getOrElse(this, fallback);
    }

    /**
     * @see AttachmentType#has
     */
    default boolean arch$hasAttached(AttachmentType<?> type) {
        return type.has(this);
    }

    /**
     * @see AttachmentType#set
     */
    @Nullable
    default <T> T arch$setAttached(AttachmentType<T> type, @Nullable T value) {
        return type.set(this, value);
    }

    /**
     * @see AttachmentType#remove
     */
    @Nullable
    default <T> T arch$removeAttached(AttachmentType<T> type) {
        return type.remove(this);
    }

    /**
     * @see AttachmentType#modify
     */
    @Nullable
    default <T> T arch$modifyAttached(AttachmentType<T> type, UnaryOperator<T> operator) {
        return type.modify(this, operator);
    }
}
