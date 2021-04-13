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

package me.shedaniel.architectury.test.registry.client;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.architectury.event.events.client.ClientTickEvent;
import me.shedaniel.architectury.registry.KeyBindings;
import me.shedaniel.architectury.test.TestMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.language.I18n;
import org.lwjgl.glfw.GLFW;

public class TestKeybinds {
    @Environment(EnvType.CLIENT)
    public static void initialize() {
        KeyMapping mapping = new KeyMapping("key.architectury-test.test", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, "category.architectury-test");
        KeyBindings.registerKeyBinding(mapping);
        ClientTickEvent.CLIENT_POST.register(instance -> {
            while (mapping.consumeClick()) {
                TestMod.SINK.accept("Key \"%s\" pressed!", I18n.get("key.architectury-test.test"));
            }
        });
    }
}
