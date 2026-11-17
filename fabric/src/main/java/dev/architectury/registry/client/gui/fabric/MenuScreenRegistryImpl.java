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

package dev.architectury.registry.client.gui.fabric;

import dev.architectury.registry.client.gui.MenuScreenRegistry.ScreenFactory;
import net.minecraft.client.core.registries.BuiltInClientRegistries;
import net.minecraft.client.core.registries.ClientRegistries;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class MenuScreenRegistryImpl {
    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreenFactory(MenuType<? extends H> type, ScreenFactory<H, S> factory) {
        // Menu screens are a vanilla client registry since 26.4, keyed by the menu type id.
        Registry.register(BuiltInClientRegistries.MENU_SCREEN,
                ResourceKey.create(ClientRegistries.MENU_SCREEN, BuiltInRegistries.MENU.getKey(type)),
                (MenuScreens.ScreenConstructor<H, S>) factory::create);
    }
}
