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

package dev.architectury.platform.forge;

import dev.architectury.platform.Mod;
import dev.architectury.utils.Env;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.loading.moddiscovery.ModFileInfo;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.IModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PlatformImpl {
    private static final Map<String, Mod> mods = new ConcurrentHashMap<>();
    
    public static Path getGameFolder() {
        return FMLPaths.GAMEDIR.get();
    }
    
    public static Path getConfigFolder() {
        return FMLPaths.CONFIGDIR.get();
    }
    
    public static Path getModsFolder() {
        return FMLPaths.MODSDIR.get();
    }
    
    public static Env getEnvironment() {
        return Env.fromPlatform(getEnv());
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
        return ModList.get().getMods().stream().map(IModInfo::getModId).collect(Collectors.toList());
    }
    
    public static boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
    
    private static class ModImpl implements Mod {
        private final ModContainer container;
        private final IModInfo info;
        
        public ModImpl(String id) {
            this.container = ModList.get().getModContainerById(id).orElseThrow();
            this.info = ModList.get().getMods().stream()
                    .filter(modInfo -> Objects.equals(modInfo.getModId(), id))
                    .findAny()
                    .orElseThrow();
        }
        
        @Override
        @NotNull
        public String getModId() {
            return info.getModId();
        }
        
        @Override
        @NotNull
        public String getVersion() {
            return info.getVersion().toString();
        }
        
        @Override
        @NotNull
        public String getName() {
            return info.getDisplayName();
        }
        
        @Override
        @NotNull
        public String getDescription() {
            return info.getDescription();
        }
        
        @Override
        public Optional<String> getLogoFile(int i) {
            return this.info.getLogoFile();
        }
        
        @Override
        public List<Path> getFilePaths() {
            return List.of(getFilePath());
        }
        
        @Override
        public Path getFilePath() {
            return this.info.getOwningFile().getFile().getSecureJar().getRootPath();
        }
        
        @Override
        public Optional<Path> findResource(String... path) {
            return Optional.of(this.info.getOwningFile().getFile().findResource(path)).filter(Files::exists);
        }
        
        @Override
        public Collection<String> getAuthors() {
            Optional<String> optional = this.info.getConfig().getConfigElement("authors")
                    .map(String::valueOf);
            return optional.isPresent() ? Collections.singleton(optional.get()) : Collections.emptyList();
        }
        
        @Override
        public @Nullable Collection<String> getLicense() {
            return Collections.singleton(this.info.getOwningFile().getLicense());
        }
        
        @Override
        public Optional<String> getHomepage() {
            return this.info.getConfig().getConfigElement("displayURL")
                    .map(String::valueOf);
        }
        
        @Override
        public Optional<String> getSources() {
            return Optional.empty();
        }
        
        @Override
        public Optional<String> getIssueTracker() {
            IModFileInfo owningFile = this.info.getOwningFile();
            if (owningFile instanceof ModFileInfo info) {
                return Optional.ofNullable(info.getIssueURL())
                        .map(URL::toString);
            }
            return Optional.empty();
        }
        
        @Override
        public void registerConfigurationScreen(ConfigurationScreenProvider configurationScreenProvider) {
            container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                    new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> configurationScreenProvider.provide(screen)));
        }
    }
}
