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

package dev.architectury.hooks.level.biome;

import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

public interface EffectsProperties {
    int getFogColor();
    
    int getWaterColor();
    
    int getWaterFogColor();
    
    int getSkyColor();
    
    OptionalInt getFoliageColorOverride();
    
    OptionalInt getGrassColorOverride();
    
    GrassColorModifier getGrassColorModifier();
    
    Optional<AmbientParticleSettings> getAmbientParticle();
    
    Optional<SoundEvent> getAmbientLoopSound();
    
    Optional<AmbientMoodSettings> getAmbientMoodSound();
    
    Optional<AmbientAdditionsSettings> getAmbientAdditionsSound();
    
    Optional<Music> getBackgroundMusic();
    
    interface Mutable extends EffectsProperties {
        EffectsProperties.Mutable setFogColor(int color);
        
        EffectsProperties.Mutable setWaterColor(int color);
        
        EffectsProperties.Mutable setWaterFogColor(int color);
        
        EffectsProperties.Mutable setSkyColor(int color);
        
        EffectsProperties.Mutable setFoliageColorOverride(@Nullable Integer colorOverride);
        
        EffectsProperties.Mutable setGrassColorOverride(@Nullable Integer colorOverride);
        
        EffectsProperties.Mutable setGrassColorModifier(GrassColorModifier modifier);
        
        EffectsProperties.Mutable setAmbientParticle(@Nullable AmbientParticleSettings settings);
        
        EffectsProperties.Mutable setAmbientLoopSound(@Nullable SoundEvent sound);
        
        EffectsProperties.Mutable setAmbientMoodSound(@Nullable AmbientMoodSettings settings);
        
        EffectsProperties.Mutable setAmbientAdditionsSound(@Nullable AmbientAdditionsSettings settings);
        
        EffectsProperties.Mutable setBackgroundMusic(@Nullable Music music);
    }
}
