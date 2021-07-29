/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

package dev.architectury.plugin.forge;

/*
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ArchitecturyMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        
    }
    
    @Override
    public String getRefMapperConfig() {
        return null;
    }
    
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }
    
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        
    }
    
    @Override
    public List<String> getMixins() {
        return null;
    }
    
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // Inject our own sugar
        switch (mixinClassName) {
            case "me.shedaniel.architectury.mixin.forge.MixinRegistryEntry":
                targetClass.superName = "net/minecraftforge/registries/ForgeRegistryEntry";
                for (MethodNode method : targetClass.methods) {
                    if (Objects.equals(method.name, "<init>")) {
                        for (AbstractInsnNode insnNode : method.instructions) {
                            if (insnNode.getOpcode() == Opcodes.INVOKESPECIAL && insnNode instanceof MethodInsnNode) {
                                MethodInsnNode node = (MethodInsnNode) insnNode;
                                if (Objects.equals(node.name, "<init>") && Objects.equals(node.owner, "java/lang/Object")) {
                                    node.owner = "net/minecraftforge/registries/ForgeRegistryEntry";
                                    break;
                                }
                            }
                        }
                    }
                }
                targetClass.signature = "<T::Lnet/minecraftforge/registries/IForgeRegistryEntry<TT;>;>Lnet/minecraftforge/registries/ForgeRegistryEntry<TT;>;";
                break;
        }
    }
    
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        
    }
}
*/
