/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022, 2023 architectury
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

package dev.architectury.mixin.fabric;

import dev.architectury.hooks.data.fabric.DynamicRegistryHooksImpl;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RegistryDataLoader.class)
public class MixinRegistryDataLoader {
    
    // Mirror forge's directory style for architectury registrars
    @Inject(method = "registryDirPath", at = @At("HEAD"), cancellable = true)
    private static void registryDirPath(ResourceLocation key, CallbackInfoReturnable<String> cir) {
        if (DynamicRegistryHooksImpl.shouldPrefix(key))
            cir.setReturnValue(key.getNamespace() + "/" + key.getPath());
    }
}
