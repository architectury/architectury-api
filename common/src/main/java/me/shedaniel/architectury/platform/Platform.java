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

package me.shedaniel.architectury.platform;

import me.shedaniel.architectury.Architectury;
import me.shedaniel.architectury.annotations.ExpectPlatform;
import me.shedaniel.architectury.targets.ArchitecturyTarget;
import me.shedaniel.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.minecraft.SharedConstants;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

public final class Platform {
    private Platform() {}
    
    private static int simpleLoaderCache = -1;
    
    /**
     * @return the current mod loader, either "fabric" or "forge"
     * @deprecated does not reflect the true mod loader, "quilt" is never returned,
     *             use {@link ArchitecturyTarget#getCurrentTarget()} instead.
     */
    @NotNull
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0")
    public static String getModLoader() {
        return Architectury.getModLoader();
    }
    
    public static boolean isFabric() {
        updateLoaderCache();
        return simpleLoaderCache == 0;
    }
    
    public static boolean isForge() {
        updateLoaderCache();
        return simpleLoaderCache == 1;
    }
    
    private static void updateLoaderCache() {
        if (simpleLoaderCache != -1) {
            return;
        }
        
        switch (getModLoader()) {
            case "fabric":
                simpleLoaderCache = 0;
                return;
            case "forge":
                simpleLoaderCache = 1;
                return;
        }
    }
    
    @NotNull
    public static String getMinecraftVersion() {
        return SharedConstants.getCurrentVersion().getId();
    }
    
    @NotNull
    @ExpectPlatform
    public static Path getGameFolder() {
        throw new AssertionError();
    }
    
    @NotNull
    @ExpectPlatform
    public static Path getConfigFolder() {
        throw new AssertionError();
    }
    
    @NotNull
    @ExpectPlatform
    public static Env getEnvironment() {
        throw new AssertionError();
    }
    
    @NotNull
    @ExpectPlatform
    public static EnvType getEnv() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static boolean isModLoaded(String id) {
        throw new AssertionError();
    }
    
    @NotNull
    @ExpectPlatform
    public static Mod getMod(String id) {
        throw new AssertionError();
    }
    
    @NotNull
    public static Optional<Mod> getOptionalMod(String id) {
        try {
            return Optional.of(getMod(id));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
    
    @NotNull
    @ExpectPlatform
    public static Collection<Mod> getMods() {
        throw new AssertionError();
    }
    
    @NotNull
    @ExpectPlatform
    public static Collection<String> getModIds() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static boolean isDevelopmentEnvironment() {
        throw new AssertionError();
    }
}
