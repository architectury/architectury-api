/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.shedaniel.architectury.platform;

import me.shedaniel.architectury.Architectury;
import me.shedaniel.architectury.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.minecraft.SharedConstants;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

public final class Platform {
    private Platform() {}
    
    /**
     * @return the current mod loader, either "fabric" or "forge"
     */
    @NotNull
    public static String getModLoader() {
        return Architectury.getModLoader();
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
