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

package dev.architectury.attachment.neoforge;

import dev.architectury.attachment.AttachmentType;
import dev.architectury.extensions.injected.InjectedAttachmentHolderExtension;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class NeoForgeAttachmentType<T> implements AttachmentType<T> {
    private final Identifier id;
    private final net.neoforged.neoforge.attachment.AttachmentType<T> type;
    @Nullable
    private final Supplier<T> initializer;
    private final boolean persistent;
    private final boolean synced;
    private final boolean copyOnDeath;

    NeoForgeAttachmentType(Identifier id, net.neoforged.neoforge.attachment.AttachmentType<T> type, @Nullable Supplier<T> initializer,
                           boolean persistent, boolean synced, boolean copyOnDeath) {
        this.id = id;
        this.type = type;
        this.initializer = initializer;
        this.persistent = persistent;
        this.synced = synced;
        this.copyOnDeath = copyOnDeath;
    }

    public net.neoforged.neoforge.attachment.AttachmentType<T> unwrap() {
        return type;
    }

    @Override
    public Identifier id() {
        return id;
    }

    @Override
    public boolean isPersistent() {
        return persistent;
    }

    @Override
    public boolean isSynced() {
        return synced;
    }

    @Override
    public boolean copyOnDeath() {
        return copyOnDeath;
    }

    @Override
    @Nullable
    public Supplier<T> initializer() {
        return initializer;
    }

    @Override
    @Nullable
    public T get(InjectedAttachmentHolderExtension holder) {
        return ((IAttachmentHolder) holder).getExistingDataOrNull(type);
    }

    @Override
    public boolean has(InjectedAttachmentHolderExtension holder) {
        return ((IAttachmentHolder) holder).hasData(type);
    }

    @Override
    @Nullable
    public T set(InjectedAttachmentHolderExtension holder, @Nullable T value) {
        IAttachmentHolder attachmentHolder = (IAttachmentHolder) holder;
        T old = value == null ? attachmentHolder.removeData(type) : attachmentHolder.setData(type, value);
        // Fabric marks these holders as changed automatically, NeoForge leaves it to the caller
        if (holder instanceof BlockEntity blockEntity) {
            blockEntity.setChanged();
        } else if (holder instanceof ChunkAccess chunk) {
            chunk.markUnsaved();
        }
        return old;
    }

    @Override
    public String toString() {
        return "AttachmentType[" + id + "]";
    }
}
