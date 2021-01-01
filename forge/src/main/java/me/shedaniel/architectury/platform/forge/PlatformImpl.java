/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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

package me.shedaniel.architectury.platform.forge;

import me.shedaniel.architectury.platform.Mod;
import me.shedaniel.architectury.utils.Env;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
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
        return ModList.get().getMods().stream().map(ModInfo::getModId).collect(Collectors.toList());
    }
    
    public static boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
    
    private static class ModImpl implements Mod {
        private final ModContainer container;
        private final ModInfo info;
        
        public ModImpl(String id) {
            this.container = ModList.get().getModContainerById(id).get();
            this.info = ModList.get().getMods().stream()
                    .filter(modInfo -> Objects.equals(modInfo.getModId(), id))
                    .findAny()
                    .get();
        }
        
        @Override
        @Nonnull
        public String getModId() {
            return info.getModId();
        }
        
        @Override
        @Nonnull
        public String getVersion() {
            return info.getVersion().toString();
        }
        
        @Override
        @Nonnull
        public String getName() {
            return info.getDisplayName();
        }
        
        @Override
        @Nonnull
        public String getDescription() {
            return info.getDescription();
        }
        
        @Override
        public @NotNull Optional<String> getLogoFile(int i) {
            return this.info.getLogoFile();
        }
        
        @Override
        public @NotNull Path getFilePath() {
            return this.info.getOwningFile().getFile().getFilePath();
        }
        
        @Override
        public @NotNull Collection<String> getAuthors() {
            Optional<String> optional = this.info.getConfigElement("authors")
                    .map(String::valueOf);
            return optional.isPresent() ? Collections.singleton(optional.get()) : Collections.emptyList();
        }
        
        @Override
        public @Nullable Collection<String> getLicense() {
            return Collections.singleton(this.info.getOwningFile().getLicense());
        }
        
        @Override
        public @NotNull Optional<String> getHomepage() {
            return this.info.getConfigElement("displayURL")
                    .map(String::valueOf);
        }
        
        @Override
        public @NotNull Optional<String> getSources() {
            return Optional.empty();
        }
        
        @Override
        public @NotNull Optional<String> getIssueTracker() {
            return Optional.ofNullable(this.info.getOwningFile().getIssueURL())
                    .map(URL::toString);
        }
        
        @Override
        public void registerConfigurationScreen(ConfigurationScreenProvider configurationScreenProvider) {
            container.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (minecraft, screen) -> configurationScreenProvider.provide(screen));
        }
    }
}