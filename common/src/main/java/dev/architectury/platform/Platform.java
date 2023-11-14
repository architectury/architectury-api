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

package dev.architectury.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.targets.ArchitecturyTarget;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.minecraft.SharedConstants;

import java.nio.file.Path;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

public final class Platform {
    private Platform() {
    }
    
    private static int simpleLoaderCache = -1;
    
    public static boolean isFabric() {
        updateLoaderCache();
        return simpleLoaderCache == 0;
    }
    
    @Deprecated(forRemoval = true)
    public static boolean isForge() {
        return isForgeLike();
    }
    
    public static boolean isForgeLike() {
        return isMinecraftForge() || isNeoForge();
    }
    
    public static boolean isMinecraftForge() {
        updateLoaderCache();
        return simpleLoaderCache == 1;
    }
    
    public static boolean isNeoForge() {
        updateLoaderCache();
        return simpleLoaderCache == 2;
    }
    
    private static void updateLoaderCache() {
        if (simpleLoaderCache != -1) {
            return;
        }
        
        switch (ArchitecturyTarget.getCurrentTarget()) {
            case "fabric" -> simpleLoaderCache = 0;
            case "forge" -> simpleLoaderCache = 1;
            case "neoforge" -> simpleLoaderCache = 2;
        }
    }
    
    public static String getMinecraftVersion() {
        return SharedConstants.getCurrentVersion().getId();
    }
    
    /**
     * Gets the root directory for the current instance of Minecraft.
     * <p>
     * The returned path is guaranteed to be <b>absolute</b> and <b>normalized</b>.
     */
    @ExpectPlatform
    public static Path getGameFolder() {
        throw new AssertionError();
    }
    
    /**
     * Gets the main <code>config</code> folder for the current instance of Minecraft.
     * <p>
     * The returned path is guaranteed to be <b>absolute</b> and <b>normalized</b>.
     */
    @ExpectPlatform
    public static Path getConfigFolder() {
        throw new AssertionError();
    }
    
    /**
     * Gets the <code>mods</code> folder of the current instance of Minecraft.
     * <p>
     * The returned path is guaranteed to be <b>absolute</b> and <b>normalized</b>.
     */
    @ExpectPlatform
    public static Path getModsFolder() {
        throw new AssertionError();
    }
    
    /**
     * Returns the current Environment the game is running in,
     * being one of either <code>CLIENT</code> or <code>SERVER</code>.
     * <p>
     * The class returned is a platform-agnostic wrapper around the
     * <code>EnvType</code> and <code>Dist</code> enums, respectively.
     *
     * @return The current Environment, as an instance of {@link Env}
     * @see Env
     * @see #getEnv()
     */
    @ExpectPlatform
    public static Env getEnvironment() {
        throw new AssertionError();
    }
    
    /**
     * Returns the current Environment the game is running in,
     * as a member of the {@link EnvType} enum. This is remapped
     * on Forge to be the <code>Dist</code> enum, instead.
     *
     * @return The current Environment, as an instance of {@link EnvType}
     * (or <code>Dist</code> on Forge)
     */
    @ExpectPlatform
    public static EnvType getEnv() {
        throw new AssertionError();
    }
    
    /**
     * Checks whether a mod with the given mod ID is present.
     *
     * @param id The mod ID to check.
     * @return <code>true</code> if the mod is loaded, <code>false</code> otherwise.
     */
    @ExpectPlatform
    public static boolean isModLoaded(String id) {
        throw new AssertionError();
    }
    
    /**
     * Gets a {@link Mod} container by its mod ID.
     *
     * @param id The mod ID to look for.
     * @return The mod container, if found.
     * @throws NoSuchElementException if no mod with the given ID exists
     */
    @ExpectPlatform
    public static Mod getMod(String id) {
        throw new AssertionError();
    }
    
    /**
     * Optionally gets a {@link Mod} container by its mod ID if it exists.
     *
     * @param id The mod ID to look for.
     * @return An optional representing the mod container, if found,
     * or an empty optional otherwise.
     */
    public static Optional<Mod> getOptionalMod(String id) {
        try {
            return Optional.of(getMod(id));
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Gets a collection of {@link Mod} containers for all currently-loaded mods.
     *
     * @return A collection of mod containers.
     */
    @ExpectPlatform
    public static Collection<Mod> getMods() {
        throw new AssertionError();
    }
    
    /**
     * Gets a collection of Strings representing the mod IDs of all currently-loaded mods.
     *
     * @return A collection of all loaded mod IDs.
     */
    @ExpectPlatform
    public static Collection<String> getModIds() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static boolean isDevelopmentEnvironment() {
        throw new AssertionError();
    }
}
