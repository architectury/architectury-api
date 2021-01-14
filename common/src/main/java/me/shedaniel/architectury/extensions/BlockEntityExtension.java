/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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

package me.shedaniel.architectury.extensions;

import me.shedaniel.architectury.hooks.BlockEntityHooks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Extensions to {@link net.minecraft.world.level.block.entity.BlockEntity}, implement this on to your class.
 */
public interface BlockEntityExtension {
    /**
     * Handles data sent by {@link BlockEntityExtension#saveClientData(CompoundTag)} on the server.
     */
    @Environment(EnvType.CLIENT)
    void loadClientData(@NotNull BlockState pos, @NotNull CompoundTag tag);
    
    /**
     * Writes data to sync to the client.
     */
    @NotNull
    CompoundTag saveClientData(@NotNull CompoundTag tag);
    
    /**
     * Sync data to the clients by {@link BlockEntityExtension#saveClientData(CompoundTag)} and {@link BlockEntityExtension#loadClientData(BlockState, CompoundTag)}.
     */
    @ApiStatus.NonExtendable
    default void syncData() {
        BlockEntityHooks.syncData((BlockEntity) this);
    }
}
