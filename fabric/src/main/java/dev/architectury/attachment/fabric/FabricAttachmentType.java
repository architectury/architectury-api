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

import dev.architectury.attachment.AttachmentType;
import dev.architectury.extensions.injected.InjectedAttachmentHolderExtension;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class FabricAttachmentType<T> implements AttachmentType<T> {
    private final net.fabricmc.fabric.api.attachment.v1.AttachmentType<T> type;

    FabricAttachmentType(net.fabricmc.fabric.api.attachment.v1.AttachmentType<T> type) {
        this.type = type;
    }

    public net.fabricmc.fabric.api.attachment.v1.AttachmentType<T> unwrap() {
        return type;
    }

    @Override
    public Identifier id() {
        return type.identifier();
    }

    @Override
    public boolean isPersistent() {
        return type.isPersistent();
    }

    @Override
    public boolean isSynced() {
        return type.isSynced();
    }

    @Override
    public boolean copyOnDeath() {
        return type.copyOnDeath();
    }

    @Override
    @Nullable
    public Supplier<T> initializer() {
        return type.initializer();
    }

    @Override
    @Nullable
    public T get(InjectedAttachmentHolderExtension holder) {
        return ((AttachmentTarget) holder).getAttached(type);
    }

    @Override
    public boolean has(InjectedAttachmentHolderExtension holder) {
        return ((AttachmentTarget) holder).hasAttached(type);
    }

    @Override
    @Nullable
    public T set(InjectedAttachmentHolderExtension holder, @Nullable T value) {
        return ((AttachmentTarget) holder).setAttached(type, value);
    }

    @Override
    public String toString() {
        return "AttachmentType[" + id() + "]";
    }
}
