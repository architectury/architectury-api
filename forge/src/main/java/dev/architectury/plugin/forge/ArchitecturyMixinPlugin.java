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

package dev.architectury.plugin.forge;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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
        
    }
    
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if ("dev.architectury.mixin.forge.MixinIForgeBlock".equals(mixinClassName)) {
            for (MethodNode method : targetClass.methods) {
                if ("getFlammability".equals(method.name) && method.desc.endsWith(")I")) {
                    AbstractInsnNode last = method.instructions.getLast();
                    // iterate backwards to find the last IRETURN
                    while (last != null && !(last instanceof InsnNode && last.getOpcode() == Opcodes.IRETURN)) {
                        last = last.getPrevious();
                    }
                    
                    if (last != null) {
                        // insert a call to BlockFlammabilityRegistryImpl.handleFlammabilityHook
                        method.instructions.insertBefore(last, new VarInsnNode(Opcodes.ALOAD, 1)); // BlockState
                        method.instructions.insertBefore(last, new VarInsnNode(Opcodes.ALOAD, 2)); // BlockGetter
                        method.instructions.insertBefore(last, new VarInsnNode(Opcodes.ALOAD, 3)); // BlockPos
                        method.instructions.insertBefore(last, new VarInsnNode(Opcodes.ALOAD, 4)); // Direction
                        Type type = Type.getMethodType(method.desc);
                        Type[] types = type.getArgumentTypes();
                        String newDesc = Type.getMethodDescriptor(type.getReturnType(), Stream.concat(Stream.of(Type.INT_TYPE), Stream.of(types)).toArray(Type[]::new));
                        method.instructions.insertBefore(last, new MethodInsnNode(Opcodes.INVOKESTATIC, "dev/architectury/mixin/forge/BlockFlammabilityRegistryImpl", "handleBurnOddsHook", newDesc, false));
                    }
                } else if ("getFireSpreadSpeed".equals(method.name) && method.desc.endsWith(")I")) {
                    AbstractInsnNode last = method.instructions.getLast();
                    // iterate backwards to find the last IRETURN
                    while (last != null && !(last instanceof InsnNode && last.getOpcode() == Opcodes.IRETURN)) {
                        last = last.getPrevious();
                    }
    
                    if (last != null) {
                        // insert a call to BlockFlammabilityRegistryImpl.handleFlammabilityHook
                        method.instructions.insertBefore(last, new VarInsnNode(Opcodes.ALOAD, 1)); // BlockState
                        method.instructions.insertBefore(last, new VarInsnNode(Opcodes.ALOAD, 2)); // BlockGetter
                        method.instructions.insertBefore(last, new VarInsnNode(Opcodes.ALOAD, 3)); // BlockPos
                        method.instructions.insertBefore(last, new VarInsnNode(Opcodes.ALOAD, 4)); // Direction
                        Type type = Type.getMethodType(method.desc);
                        Type[] types = type.getArgumentTypes();
                        String newDesc = Type.getMethodDescriptor(type.getReturnType(), Stream.concat(Stream.of(Type.INT_TYPE), Stream.of(types)).toArray(Type[]::new));
                        method.instructions.insertBefore(last, new MethodInsnNode(Opcodes.INVOKESTATIC, "dev/architectury/mixin/forge/BlockFlammabilityRegistryImpl", "handleSpreadOddsHook", newDesc, false));
                    }
                }
            }
        }
    }
}
