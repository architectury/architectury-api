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

package me.shedaniel.architectury.hooks.fabric;

import net.minecraft.world.item.DyeColor;

public class DyeColorHooksImpl {
    public static int getColorValue(DyeColor color) {
        float[] colors = color.getTextureDiffuseColors();
        return ((int) (colors[0] * 255.0F + 0.5D) & 255) << 16 | ((int) (colors[1] * 255.0F + 0.5D) & 255) << 8 | (int) (colors[2] * 255.0F + 0.5D);
    }
}
