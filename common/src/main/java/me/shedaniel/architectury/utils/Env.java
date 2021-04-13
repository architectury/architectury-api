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

package me.shedaniel.architectury.utils;

import net.fabricmc.api.EnvType;

public enum Env {
    CLIENT,
    SERVER;
    
    /**
     * Converts platform-specific environment enum to platform-agnostic environment enum.
     *
     * @param type the platform-specific environment enum, could be {@link net.fabricmc.api.EnvType} or {@link net.minecraftforge.api.distmarker.Dist}
     * @return the platform-agnostic environment enum
     */
    public static Env fromPlatform(Object type) {
        return type == EnvType.CLIENT ? CLIENT : type == EnvType.SERVER ? SERVER : null;
    }
    
    /**
     * Converts platform-agnostic environment enum to platform-specific environment enum.
     *
     * @return the platform-specific environment enum, could be {@link net.fabricmc.api.EnvType} or {@link net.minecraftforge.api.distmarker.Dist}
     */
    public EnvType toPlatform() {
        return this == CLIENT ? EnvType.CLIENT : EnvType.SERVER;
    }
}
