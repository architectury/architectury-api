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

package dev.architectury.plugin.fabric;

import net.fabricmc.loader.api.*;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ArchitecturyMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        // noop
    }
    
    @Override
    public String getRefMapperConfig() {
        return null;
    }
    
    private boolean isLoader013() {
        ModContainer fabricLoader = FabricLoader.getInstance().getModContainer("fabricloader")
                .orElseThrow(() -> new IllegalStateException("Where is fabricloader?"));
        Version version = fabricLoader.getMetadata().getVersion();
        if (version instanceof SemanticVersion) {
            try {
                return version.compareTo(SemanticVersion.parse("0.13-")) >= 0;
            } catch (VersionParsingException e) {
                throw new IllegalStateException("Failed to parse version", e);
            }
        }
        System.err.println("FabricLoader is not a SemanticVersion, cannot determine if it is >= 0.13");
        return true;
    }
    
    private boolean isMinecraft1182() {
        ModContainer minecraft = FabricLoader.getInstance().getModContainer("minecraft")
                .orElseThrow(() -> new IllegalStateException("Where is minecraft?"));
        Version version = minecraft.getMetadata().getVersion();
        if (version instanceof SemanticVersion) {
            try {
                return version.compareTo(SemanticVersion.parse("1.18.2-")) >= 0;
            } catch (VersionParsingException e) {
                throw new IllegalStateException("Failed to parse version", e);
            }
        }
        System.err.println("Minecraft is not a SemanticVersion, cannot determine if it is >= 1.18.2");
        return true;
    }
    
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if ("dev.architectury.mixin.fabric.client.MixinEffectInstance".equals(mixinClassName)) {
            return !FabricLoader.getInstance().isModLoaded("satin");
        } else if ("dev.architectury.mixin.fabric.client.MixinGameRenderer".equals(mixinClassName)) {
            return !isLoader013();
        } else if ("dev.architectury.mixin.fabric.client.MixinGameRenderer013".equals(mixinClassName)) {
            return isLoader013();
        } else if ("dev.architectury.mixin.fabric.client.MixinMinecraft118".equals(mixinClassName)) {
            return !isMinecraft1182();
        } else if ("dev.architectury.mixin.fabric.client.MixinMinecraft1182".equals(mixinClassName)) {
            return isMinecraft1182();
        }
        return true;
    }
    
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        // noop
    }
    
    @Override
    public List<String> getMixins() {
        return null;
    }
    
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // noop
    }
    
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // noop
    }
}
