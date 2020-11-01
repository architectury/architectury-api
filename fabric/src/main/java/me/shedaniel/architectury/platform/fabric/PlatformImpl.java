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
import me.shedaniel.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlatformImpl implements Platform.Impl {
    public static final Map<String, Mod.ConfigurationScreenProvider> CONFIG_SCREENS = new HashMap<>();
    private final Map<String, Mod> mods = new HashMap<>();
    
    @Override
    public Path getGameFolder() {
        return FabricLoader.getInstance().getGameDir();
    }
    
    @Override
    public Path getConfigFolder() {
        return FabricLoader.getInstance().getConfigDir();
    }
    
    @Override
    public Path getModsFolder() {
        return getGameFolder().resolve("mods");
    }
    
    @Override
    public EnvType getEnv() {
        return FabricLoader.getInstance().getEnvironmentType();
    }
    
    @Override
    public boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }
    
    @Override
    public Mod getMod(String id) {
        return this.mods.computeIfAbsent(id, ModImpl::new);
    }
    
    @Override
    public Collection<Mod> getMods() {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            getMod(mod.getMetadata().getId());
        }
        return this.mods.values();
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
