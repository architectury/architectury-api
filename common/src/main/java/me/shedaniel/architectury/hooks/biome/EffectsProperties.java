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

package me.shedaniel.architectury.hooks.biome;

import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

public interface EffectsProperties {
    int getFogColor();
    
    int getWaterColor();
    
    int getWaterFogColor();
    
    int getSkyColor();
    
    @NotNull
    OptionalInt getFoliageColorOverride();
    
    @NotNull
    OptionalInt getGrassColorOverride();
    
    @NotNull
    GrassColorModifier getGrassColorModifier();
    
    @NotNull
    Optional<AmbientParticleSettings> getAmbientParticle();
    
    @NotNull
    Optional<SoundEvent> getAmbientLoopSound();
    
    @NotNull
    Optional<AmbientMoodSettings> getAmbientMoodSound();
    
    @NotNull
    Optional<AmbientAdditionsSettings> getAmbientAdditionsSound();
    
    @NotNull
    Optional<Music> getBackgroundMusic();
    
    interface Mutable extends EffectsProperties {
        @NotNull
        EffectsProperties.Mutable setFogColor(int color);
        
        @NotNull
        EffectsProperties.Mutable setWaterColor(int color);
        
        @NotNull
        EffectsProperties.Mutable setWaterFogColor(int color);
        
        @NotNull
        EffectsProperties.Mutable setSkyColor(int color);
        
        @NotNull
        EffectsProperties.Mutable setFoliageColorOverride(@Nullable Integer colorOverride);
        
        @NotNull
        EffectsProperties.Mutable setGrassColorOverride(@Nullable Integer colorOverride);
        
        @NotNull
        EffectsProperties.Mutable setGrassColorModifier(@NotNull GrassColorModifier modifier);
        
        @NotNull
        EffectsProperties.Mutable setAmbientParticle(@Nullable AmbientParticleSettings settings);
        
        @NotNull
        EffectsProperties.Mutable setAmbientLoopSound(@Nullable SoundEvent sound);
        
        @NotNull
        EffectsProperties.Mutable setAmbientMoodSound(@Nullable AmbientMoodSettings settings);
        
        @NotNull
        EffectsProperties.Mutable setAmbientAdditionsSound(@Nullable AmbientAdditionsSettings settings);
        
        @NotNull
        EffectsProperties.Mutable setBackgroundMusic(@Nullable Music music);
    }
}