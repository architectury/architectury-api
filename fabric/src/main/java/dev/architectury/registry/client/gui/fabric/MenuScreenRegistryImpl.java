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
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MenuScreenRegistryImpl {
    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreenFactory(MenuType<? extends H> type, ScreenFactory<H, S> factory) {
        try {
            Class<?> constructorClass = Class.forName("net.minecraft.client.gui.screens.MenuScreens$ScreenConstructor");
            Object constructor = Proxy.newProxyInstance(
                    MenuScreenRegistryImpl.class.getClassLoader(),
                    new Class[]{constructorClass},
                    (proxy, method, args) -> {
                        if ("create".equals(method.getName())) {
                            return factory.create((H) args[0], (net.minecraft.world.entity.player.Inventory) args[1], (net.minecraft.network.chat.Component) args[2]);
                        }
                        throw new UnsupportedOperationException(method.getName());
                    }
            );
            Method register = MenuScreens.class.getDeclaredMethod("register", MenuType.class, constructorClass);
            register.setAccessible(true);
            register.invoke(null, type, constructor);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Failed to register menu screen factory for " + type, exception);
        }
    }
}
