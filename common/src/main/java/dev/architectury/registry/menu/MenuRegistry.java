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

package dev.architectury.registry.menu;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.gui.screens.Screen;
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
 * @see dev.architectury.registry.client.gui.MenuScreenRegistry
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
