/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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

package me.shedaniel.architectury.registry;

import java.util.function.BiFunction;

import me.shedaniel.architectury.ExpectPlatform;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * A utility class to register {@link MenuType}s and {@link Screen}s for containers
 */
public final class ScreenRegisters {
    private ScreenRegisters() {}

    /**
     * Registers a simple {@link MenuType}
     * @param resourceLocation The identifier for the {@link MenuType}
     * @param menuTypeSupplier A functional interface to create the {@link MenuType} from an id (Integer) and inventory
     * @param <T> The type of {@link AbstractContainerMenu} that handles the logic for the {@link MenuType}
     * @return The {@link MenuType} for your {@link AbstractContainerMenu}
     */
    @ExpectPlatform
    public static <T extends AbstractContainerMenu> MenuType<T> registerSimpleMenuType(ResourceLocation resourceLocation, BiFunction<Integer, Inventory, T> menuTypeSupplier) {
        throw new AssertionError();
    }

    /**
     * Registers a extended {@link MenuType}
     * @param resourceLocation The identifier for the {@link MenuType}
     * @param menuTypeSupplier A functional interface to create the {@link MenuType} from an id (Integer), {@link Inventory}, and {@link FriendlyByteBuf}
     * @param <T> The type of {@link AbstractContainerMenu} that handles the logic for the {@link MenuType}
     * @return The {@link MenuType} for your {@link AbstractContainerMenu}
     */
    @ExpectPlatform
    public static <T extends AbstractContainerMenu> MenuType<T> registerExtendedMenuType(ResourceLocation resourceLocation, ExtendedMenuTypeFactory<T> menuTypeSupplier) {
        throw new AssertionError();
    }

    /**
     * Registers a Screen Factory on the client to display
     * @param menuType The {@link MenuType} the screen visualizes
     * @param screenSupplier A functional interface that is used to create new {@link Screen}s
     * @param <H> The type of {@link AbstractContainerMenu} for the screen
     * @param <S> The type for the {@link Screen}
     */
    @Environment(EnvType.CLIENT)
    @ExpectPlatform
    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreenFactory(MenuType<? extends H> menuType, ScreenFactory<H, S> screenSupplier) {
        throw new AssertionError();
    }

    /**
     * Creates new screens
     * @param <H> The type of {@link AbstractContainerMenu} for the screen
     * @param <S> The type for the {@link Screen}
     */
    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    public interface ScreenFactory<H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> {
        /**
         * Creates a new {@link S} that extends {@link Screen}
         * @param containerMenu The {@link AbstractContainerMenu} that controls the game logic for the screen
         * @param inventory The {@link Inventory} for the screen
         * @param component The {@link Component} for the screen
         * @return A new {@link S} that extends {@link Screen}
         */
        S create(H containerMenu, Inventory inventory, Component component);
    }

    /**
     * Creates extended menus
     * @param <T> The {@link AbstractContainerMenu} type
     */
    @FunctionalInterface
    public interface ExtendedMenuTypeFactory<T extends AbstractContainerMenu> {
        /**
         * Creates a new {@link T} that extends {@link AbstractContainerMenu}
         * @param id The id for the menu
         * @param inventory The {@link Inventory} for the menu
         * @param friendlyByteBuf The {@link FriendlyByteBuf} for the menu to provide extra data
         * @return A new {@link T} that extends {@link AbstractContainerMenu}
         */
        T create(int id, Inventory inventory, FriendlyByteBuf friendlyByteBuf);
    }
}
