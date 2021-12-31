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

package dev.architectury.mixin.fabric.client;

import com.mojang.blaze3d.shaders.Program;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Unique
@Mixin(value = EffectInstance.class, priority = 950)
public class MixinEffectInstance {
    @Redirect(
            method = "<init>",
            at = @At(value = "NEW",
                    target = "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
                    ordinal = 0)
    )
    private ResourceLocation mojangPls(String _0, ResourceManager rm, String str) {
        return mojangPls(new ResourceLocation(str), ".json");
    }
    
    @Redirect(
            method = "getOrCreate",
            at = @At(value = "NEW",
                    target = "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
                    ordinal = 0)
    )
    private static ResourceLocation mojangPls(String _0, ResourceManager rm, Program.Type type, String str) {
        return mojangPls(new ResourceLocation(str), type.getExtension());
    }
    
    private static ResourceLocation mojangPls(ResourceLocation rl, String ext) {
        return new ResourceLocation(rl.getNamespace(), "shaders/program/" + rl.getPath() + ext);
    }
}
