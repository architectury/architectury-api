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

package me.shedaniel.architectury.platform.fabric;

import me.shedaniel.architectury.platform.Mod;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PlatformImpl {
    public static final Map<String, Mod.ConfigurationScreenProvider> CONFIG_SCREENS = new HashMap<>();
    private static final Map<String, Mod> mods = new HashMap<>();
    
    public static Path getGameFolder() {
        return FabricLoader.getInstance().getGameDir();
    }
    
    public static Path getConfigFolder() {
        return FabricLoader.getInstance().getConfigDir();
    }
    
    public static Path getModsFolder() {
        return getGameFolder().resolve("mods");
    }
    
    public static EnvType getEnv() {
        return FabricLoader.getInstance().getEnvironmentType();
    }
    
    public static boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }
    
    public static Mod getMod(String id) {
        return mods.computeIfAbsent(id, ModImpl::new);
    }
    
    public static Collection<Mod> getMods() {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            getMod(mod.getMetadata().getId());
        }
        return mods.values();
    }
    
    public static Collection<String> getModIds() {
        return FabricLoader.getInstance().getAllMods().stream().map(ModContainer::getMetadata).map(ModMetadata::getId).collect(Collectors.toList());
    }
    
    public static boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
    
    private static class ModImpl implements Mod {
        private final ModMetadata metadata;
        
        public ModImpl(String id) {
            this.metadata = FabricLoader.getInstance().getModContainer(id).get().getMetadata();
        }
        
        @Override
        public @NotNull String getModId() {
            return metadata.getId();
        }
        
        @Override
        public @NotNull String getVersion() {
            return metadata.getVersion().getFriendlyString();
        }
        
        @Override
        public @NotNull String getName() {
            return metadata.getName();
        }
        
        @Override
        public @NotNull String getDescription() {
            return metadata.getDescription();
        }
        
        @Override
        public void registerConfigurationScreen(ConfigurationScreenProvider provider) {
            if (CONFIG_SCREENS.containsKey(getModId()))
                throw new IllegalStateException("Can not register configuration screen for mod '" + getModId() + "' because it was already registered!");
            CONFIG_SCREENS.put(getModId(), provider);
        }
    }
}
