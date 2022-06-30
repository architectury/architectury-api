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

package me.shedaniel.architectury.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Mod {
    String getModId();
    
    String getVersion();
    
    String getName();
    
    String getDescription();
    
    /**
     * Gets the logo file path of the mod
     *
     * @param preferredSize the preferred logo size, only used in fabric
     * @return the logo file path relative to the file
     */
    Optional<String> getLogoFile(int preferredSize);
    
    /**
     * Gets a list of all possible root paths for the mod.
     * This is especially relevant on Fabric, as a single mod may have multiple source sets
     * (such as client / server-specific ones), each corresponding to one root path.
     *
     * @return A list of root paths belonging to the mod
     */
    List<Path> getFilePaths();
    
    /**
     * @deprecated Use {@link #getFilePaths()} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    Path getFilePath();
    
    Collection<String> getAuthors();
    
    @Nullable
    Collection<String> getLicense();
    
    Optional<String> getHomepage();
    
    Optional<String> getSources();
    
    Optional<String> getIssueTracker();
    
    @Environment(EnvType.CLIENT)
    void registerConfigurationScreen(ConfigurationScreenProvider provider);
    
    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    interface ConfigurationScreenProvider {
        Screen provide(Screen parent);
    }
}
