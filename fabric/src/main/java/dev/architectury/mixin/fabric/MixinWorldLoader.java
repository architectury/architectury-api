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
import net.minecraft.server.WorldLoader;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Mixin(WorldLoader.class)
public class MixinWorldLoader {
    
    @Redirect(method = "load", at = @At(value = "FIELD", target = "Lnet/minecraft/resources/RegistryDataLoader;WORLDGEN_REGISTRIES:Ljava/util/List;", opcode = Opcodes.GETSTATIC))
    private static List<RegistryDataLoader.RegistryData<?>> load() {
        List<RegistryDataLoader.RegistryData<?>> data = new ArrayList<>(RegistryDataLoader.WORLDGEN_REGISTRIES);
        data.addAll(DynamicRegistryHooksImpl.getDataRegistries());
        return data;
    }
}
