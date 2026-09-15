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

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.MenuType;

/**
 * A {@link MenuProvider} that sends extra data to the client when the menu is opened,
 * serialized through a {@link StreamCodec}.
 *
 * <p>The menu must have been created with
 * {@link MenuRegistry#ofExtended(MenuRegistry.ExtendedMenuDataFactory, StreamCodec)}, and the codec
 * returned by {@link #getExtraDataCodec()} must match the one given to that call.
 *
 * @param <D> the type of the extra data
 * @see MenuRegistry#openExtendedMenu(ServerPlayer, ExtendedMenuDataProvider)
 */
public interface ExtendedMenuDataProvider<D> extends MenuProvider {
    /**
     * Returns the extra data to send to the client opening this menu.
     *
     * @param player the player the menu is being opened for
     * @return the extra data, encoded with {@link #getExtraDataCodec()}
     */
    D getExtraData(ServerPlayer player);
    
    /**
     * Returns the codec used to encode {@link #getExtraData(ServerPlayer)}.
     *
     * <p>This must be the same codec passed to
     * {@link MenuRegistry#ofExtended(MenuRegistry.ExtendedMenuDataFactory, StreamCodec)} when creating
     * the {@link MenuType}, otherwise the client will fail to decode the data.
     *
     * @return the codec for the extra data
     */
    StreamCodec<? super RegistryFriendlyByteBuf, D> getExtraDataCodec();
}
