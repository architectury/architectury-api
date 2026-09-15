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

package dev.architectury.registry.menu.neoforge;

import dev.architectury.registry.menu.ExtendedMenuDataProvider;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry.ExtendedMenuDataFactory;
import dev.architectury.registry.menu.MenuRegistry.ExtendedMenuTypeFactory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class MenuRegistryImpl {
    @Deprecated(forRemoval = true)
    @SuppressWarnings("removal")
    public static void openExtendedMenu(ServerPlayer player, ExtendedMenuProvider provider) {
        player.openMenu(provider, provider::saveExtraData);
    }
    
    public static <D> void openExtendedMenu(ServerPlayer player, ExtendedMenuDataProvider<D> provider) {
        StreamCodec<? super RegistryFriendlyByteBuf, D> codec = provider.getExtraDataCodec();
        player.openMenu(provider, buf -> codec.encode(buf, provider.getExtraData(player)));
    }
    
    @Deprecated(forRemoval = true)
    @SuppressWarnings("removal")
    public static <T extends AbstractContainerMenu> MenuType<T> ofExtended(ExtendedMenuTypeFactory<T> factory) {
        return IMenuTypeExtension.create(factory::create);
    }
    
    public static <T extends AbstractContainerMenu, D> MenuType<T> ofExtended(ExtendedMenuDataFactory<T, D> factory, StreamCodec<? super RegistryFriendlyByteBuf, D> codec) {
        return IMenuTypeExtension.create((id, inventory, buf) -> factory.create(id, inventory, codec.decode(buf)));
    }
}
