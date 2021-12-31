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

package me.shedaniel.architectury.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import me.shedaniel.architectury.registry.menu.ExtendedMenuProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * A utility class to register {@link MenuType}s and {@link Screen}s for containers
 */
public final class MenuRegistry {
    private MenuRegistry() {
    }
    
    /**
     * Opens the menu.
     *
     * @param player    The player affected
     * @param provider  The {@link MenuProvider} that provides the menu
     * @param bufWriter That writer that sends extra data for {@link MenuType} created with {@link MenuRegistry#ofExtended(ExtendedMenuTypeFactory)}
     */
    public static void openExtendedMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> bufWriter) {
        openExtendedMenu(player, new ExtendedMenuProvider() {
            @Override
            public void saveExtraData(FriendlyByteBuf buf) {
                bufWriter.accept(buf);
            }
            
            @Override
            public Component getDisplayName() {
                return provider.getDisplayName();
            }
            
            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return provider.createMenu(i, inventory, player);
            }
        });
    }
    
    /**
     * Opens the menu.
     *
     * @param player   The player affected
     * @param provider The {@link ExtendedMenuProvider} that provides the menu
     */
    @ExpectPlatform
    public static void openExtendedMenu(ServerPlayer player, ExtendedMenuProvider provider) {
        throw new AssertionError();
    }
    
    /**
     * Opens the menu.
     *
     * @param player   The player affected
     * @param provider The {@link MenuProvider} that provides the menu
     */
    public static void openMenu(ServerPlayer player, MenuProvider provider) {
        player.openMenu(provider);
    }
    
    /**
     * Creates a simple {@link MenuType}.
     *
     * @param factory A functional interface to create the {@link MenuType} from an id (Integer) and inventory
     * @param <T>     The type of {@link AbstractContainerMenu} that handles the logic for the {@link MenuType}
     * @return The {@link MenuType} for your {@link AbstractContainerMenu}
     */
    @ExpectPlatform
    public static <T extends AbstractContainerMenu> MenuType<T> of(SimpleMenuTypeFactory<T> factory) {
        throw new AssertionError();
    }
    
    /**
     * Creates a extended {@link MenuType}.
     *
     * @param factory A functional interface to create the {@link MenuType} from an id (Integer), {@link Inventory}, and {@link FriendlyByteBuf}
     * @param <T>     The type of {@link AbstractContainerMenu} that handles the logic for the {@link MenuType}
     * @return The {@link MenuType} for your {@link AbstractContainerMenu}
     */
    @ExpectPlatform
    public static <T extends AbstractContainerMenu> MenuType<T> ofExtended(ExtendedMenuTypeFactory<T> factory) {
        throw new AssertionError();
    }
    
    /**
     * Registers a Screen Factory on the client to display.
     *
     * @param type    The {@link MenuType} the screen visualizes
     * @param factory A functional interface that is used to create new {@link Screen}s
     * @param <H>     The type of {@link AbstractContainerMenu} for the screen
     * @param <S>     The type for the {@link Screen}
     */
    @Environment(EnvType.CLIENT)
    @ExpectPlatform
    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreenFactory(MenuType<? extends H> type, ScreenFactory<H, S> factory) {
        throw new AssertionError();
    }
    
    /**
     * Creates new screens.
     *
     * @param <H> The type of {@link AbstractContainerMenu} for the screen
     * @param <S> The type for the {@link Screen}
     */
    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    public interface ScreenFactory<H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> {
        /**
         * Creates a new {@link S} that extends {@link Screen}
         *
         * @param containerMenu The {@link AbstractContainerMenu} that controls the game logic for the screen
         * @param inventory     The {@link Inventory} for the screen
         * @param component     The {@link Component} for the screen
         * @return A new {@link S} that extends {@link Screen}
         */
        S create(H containerMenu, Inventory inventory, Component component);
    }
    
    /**
     * Creates simple menus.
     *
     * @param <T> The {@link AbstractContainerMenu} type
     */
    @FunctionalInterface
    public interface SimpleMenuTypeFactory<T extends AbstractContainerMenu> {
        /**
         * Creates a new {@link T} that extends {@link AbstractContainerMenu}
         *
         * @param id The id for the menu
         * @return A new {@link T} that extends {@link AbstractContainerMenu}
         */
        T create(int id, Inventory inventory);
    }
    
    /**
     * Creates extended menus.
     *
     * @param <T> The {@link AbstractContainerMenu} type
     */
    @FunctionalInterface
    public interface ExtendedMenuTypeFactory<T extends AbstractContainerMenu> {
        /**
         * Creates a new {@link T} that extends {@link AbstractContainerMenu}.
         *
         * @param id        The id for the menu
         * @param inventory The {@link Inventory} for the menu
         * @param buf       The {@link FriendlyByteBuf} for the menu to provide extra data
         * @return A new {@link T} that extends {@link AbstractContainerMenu}
         */
        T create(int id, Inventory inventory, FriendlyByteBuf buf);
    }
}
