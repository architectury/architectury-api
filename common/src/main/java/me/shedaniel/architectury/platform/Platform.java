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
import me.shedaniel.architectury.ArchitecturyPopulator;
import me.shedaniel.architectury.Populatable;
import net.fabricmc.api.EnvType;
import net.minecraft.SharedConstants;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

public final class Platform {
    private Platform() {}
    
    @Populatable
    private static final Impl IMPL = null;
    
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
    public static Path getGameFolder() {
        return IMPL.getGameFolder();
    }
    
    @NotNull
    public static Path getConfigFolder() {
        return IMPL.getConfigFolder();
    }
    
    @NotNull
    public static EnvType getEnv() {
        return IMPL.getEnv();
    }
    
    public static boolean isModLoaded(String id) {
        return IMPL.isModLoaded(id);
    }
    
    @NotNull
    public static Mod getMod(String id) {
        return IMPL.getMod(id);
    }
    
    @NotNull
    public static Optional<Mod> getOptionalMod(String id) {
        try {
            return Optional.of(IMPL.getMod(id));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
    
    @NotNull
    public static Collection<Mod> getMods() {
        return IMPL.getMods();
    }
    
    @NotNull
    public static Collection<String> getModIds() {
        return IMPL.getModIds();
    }
    
    public static boolean isDevelopmentEnvironment() {
        return IMPL.isDevelopmentEnvironment();
    }
    
    public interface Impl {
        Path getGameFolder();
        
        Path getConfigFolder();
        
        Path getModsFolder();
        
        EnvType getEnv();
        
        boolean isModLoaded(String id);
        
        Mod getMod(String id);
        
        Collection<Mod> getMods();
        
        Collection<String> getModIds();
        
        boolean isDevelopmentEnvironment();
    }
    
    static {
        ArchitecturyPopulator.populate(Platform.class);
    }
}
