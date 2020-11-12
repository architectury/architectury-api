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

package me.shedaniel.architectury.platform.forge;

import me.shedaniel.architectury.platform.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PlatformImpl {
    private static final Map<String, Mod> mods = new HashMap<>();
    
    public static Path getGameFolder() {
        return FMLPaths.GAMEDIR.get();
    }
    
    public static Path getConfigFolder() {
        return FMLPaths.CONFIGDIR.get();
    }
    
    public static Path getModsFolder() {
        return FMLPaths.MODSDIR.get();
    }
    
    public static Dist getEnv() {
        return FMLEnvironment.dist;
    }
    
    public static boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }
    
    public static Mod getMod(String id) {
        return mods.computeIfAbsent(id, ModImpl::new);
    }
    
    public static Collection<Mod> getMods() {
        for (IModInfo mod : ModList.get().getMods()) {
            getMod(mod.getModId());
        }
        return mods.values();
    }
    
    public static Collection<String> getModIds() {
        return ModList.get().getMods().stream().map(ModInfo::getModId).collect(Collectors.toList());
    }
    
    public static boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
    
    private static class ModImpl implements Mod {
        private final ModContainer container;
        private final IModInfo metadata;
        
        public ModImpl(String id) {
            this.container = ModList.get().getModContainerById(id).get();
            this.metadata = container.getModInfo();
        }
        
        @Override
        @Nonnull
        public String getModId() {
            return metadata.getModId();
        }
        
        @Override
        @Nonnull
        public String getVersion() {
            return metadata.getVersion().toString();
        }
        
        @Override
        @Nonnull
        public String getName() {
            return metadata.getDisplayName();
        }
        
        @Override
        @Nonnull
        public String getDescription() {
            return metadata.getDescription();
        }
        
        @Override
        public void registerConfigurationScreen(ConfigurationScreenProvider configurationScreenProvider) {
            container.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (minecraft, screen) -> configurationScreenProvider.provide(screen));
        }
    }
}