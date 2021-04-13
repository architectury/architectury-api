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

package me.shedaniel.architectury.registry;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Function;

public abstract class BlockProperties extends BlockBehaviour.Properties implements BlockPropertiesExtension {
    public BlockProperties(Material material, Function<BlockState, MaterialColor> function) {
        super(material, function);
    }
    
    public static BlockProperties of(Material material) {
        return of(material, material.getColor());
    }
    
    public static BlockProperties of(Material material, DyeColor color) {
        return of(material, color.getMaterialColor());
    }
    
    @ExpectPlatform
    public static BlockProperties of(Material material, MaterialColor color) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static BlockProperties of(Material material, Function<BlockState, MaterialColor> color) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static BlockProperties copy(BlockBehaviour block) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static BlockProperties copy(BlockBehaviour.Properties properties) {
        throw new AssertionError();
    }
}
