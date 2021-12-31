/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

package me.shedaniel.architectury.mixin.forge;

import me.shedaniel.architectury.extensions.network.EntitySpawnExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntitySpawnExtension.class)
public interface MixinEntitySpawnExtension extends IEntityAdditionalSpawnData {
    @Override
    default void writeSpawnData(FriendlyByteBuf buf) {
        ((EntitySpawnExtension) this).saveAdditionalSpawnData(buf);
    }
    
    @Override
    default void readSpawnData(FriendlyByteBuf buf) {
        ((EntitySpawnExtension) this).loadAdditionalSpawnData(buf);
    }
}
