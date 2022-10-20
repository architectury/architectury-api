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

import dev.architectury.event.events.client.ClientTextureStitchEvent;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.AtlasSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashMap;
import java.util.Map;

@Mixin(AtlasSet.AtlasEntry.class)
public class MixinAtlasSet {
    @Mutable
    @Shadow
    @Final
    AtlasSet.ResourceLister resourceLister;
    
    @Inject(method = "<init>", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void preStitch(TextureAtlas textureAtlas, AtlasSet.ResourceLister resourceLister, CallbackInfo ci) {
        AtlasSet.ResourceLister tmp = this.resourceLister;
        this.resourceLister = resourceManager -> {
            Map<ResourceLocation, Resource> map = new HashMap<>();
            ClientTextureStitchEvent.PRE.invoker().stitch(textureAtlas, resourceManager, map::put);
            map.putAll(tmp.apply(resourceManager));
            return map;
        };
    }
}
