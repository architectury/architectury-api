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

package dev.architectury.platform.fabric;

import dev.architectury.platform.Mod;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PlatformImpl {
    public static final Map<String, Mod.ConfigurationScreenProvider> CONFIG_SCREENS = new ConcurrentHashMap<>();
    private static final Map<String, Mod> mods = new ConcurrentHashMap<>();
    
    public static Path getGameFolder() {
        return FabricLoader.getInstance()
                .getGameDir()
                .toAbsolutePath()
                .normalize();
    }
    
    public static Path getConfigFolder() {
        return FabricLoader.getInstance()
                .getConfigDir()
                .toAbsolutePath()
                .normalize();
    }
    
    public static Path getModsFolder() {
        return getGameFolder().resolve("mods");
    }
    
    public static Env getEnvironment() {
        return Env.fromPlatform(getEnv());
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
        for (var mod : FabricLoader.getInstance().getAllMods()) {
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
        private final ModContainer container;
        private final ModMetadata metadata;
        
        public ModImpl(String id) {
            this.container = FabricLoader.getInstance().getModContainer(id).orElseThrow();
            this.metadata = this.container.getMetadata();
        }
        
        @Override
        public String getModId() {
            return metadata.getId();
        }
        
        @Override
        public String getVersion() {
            return metadata.getVersion().getFriendlyString();
        }
        
        @Override
        public String getName() {
            return metadata.getName();
        }
        
        @Override
        public String getDescription() {
            return metadata.getDescription();
        }
        
        @Override
        public Optional<String> getLogoFile(int preferredSize) {
            return metadata.getIconPath(preferredSize);
        }
        
        @Override
        public Path getFilePath() {
            return container.getRootPath();
        }
        
        @Override
        public Collection<String> getAuthors() {
            return metadata.getAuthors().stream()
                    .map(Person::getName)
                    .collect(Collectors.toList());
        }
        
        @Override
        public @Nullable Collection<String> getLicense() {
            return metadata.getLicense();
        }
        
        @Override
        public Optional<String> getHomepage() {
            return metadata.getContact().get("homepage");
        }
        
        @Override
        public Optional<String> getSources() {
            return metadata.getContact().get("issues");
        }
        
        @Override
        public Optional<String> getIssueTracker() {
            return metadata.getContact().get("sources");
        }
        
        @Override
        public void registerConfigurationScreen(ConfigurationScreenProvider provider) {
            if (CONFIG_SCREENS.containsKey(getModId()))
                throw new IllegalStateException("Can not register configuration screen for mod '" + getModId() + "' because it was already registered!");
            CONFIG_SCREENS.put(getModId(), provider);
        }
    }
}
