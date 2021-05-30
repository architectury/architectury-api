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

package dev.architectury.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.targets.ArchitecturyTarget;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.minecraft.SharedConstants;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

public final class Platform {
    private Platform() {
    }
    
    private static int simpleLoaderCache = -1;
    
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
        
        switch (ArchitecturyTarget.getCurrentTarget()) {
            case "fabric" -> simpleLoaderCache = 0;
            case "forge" -> simpleLoaderCache = 1;
        }
    }
    
    public static String getMinecraftVersion() {
        return SharedConstants.getCurrentVersion().getId();
    }
    
    @ExpectPlatform
    public static Path getGameFolder() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static Path getConfigFolder() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static Path getModsFolder() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static Env getEnvironment() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static EnvType getEnv() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static boolean isModLoaded(String id) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static Mod getMod(String id) {
        throw new AssertionError();
    }
    
    public static Optional<Mod> getOptionalMod(String id) {
        try {
            return Optional.of(getMod(id));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
    
    @ExpectPlatform
    public static Collection<Mod> getMods() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static Collection<String> getModIds() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static boolean isDevelopmentEnvironment() {
        throw new AssertionError();
    }
}
