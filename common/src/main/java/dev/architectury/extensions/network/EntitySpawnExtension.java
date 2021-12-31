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

package dev.architectury.extensions.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

/**
 * This interface can be implemented on {@linkplain Entity entities} to attach additional spawn data to packets sent to client.
 * This is used in conjunction with {@link dev.architectury.networking.NetworkManager#createAddEntityPacket(Entity)}
 */
public interface EntitySpawnExtension {
    void saveAdditionalSpawnData(FriendlyByteBuf buf);
    
    void loadAdditionalSpawnData(FriendlyByteBuf buf);
}
