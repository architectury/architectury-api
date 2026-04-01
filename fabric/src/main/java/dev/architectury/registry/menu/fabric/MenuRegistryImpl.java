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

package dev.architectury.registry.menu.fabric;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry.ExtendedMenuTypeFactory;
import dev.architectury.registry.menu.MenuRegistry.SimpleMenuTypeFactory;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.fabricmc.fabric.api.networking.v1.FriendlyByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class MenuRegistryImpl {
    public static void openExtendedMenu(ServerPlayer player, ExtendedMenuProvider provider) {
        player.openMenu(new net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider<byte[]>() {
            @Override
            public byte[] getScreenOpeningData(ServerPlayer player) {
                FriendlyByteBuf buf = FriendlyByteBufs.create();
                provider.saveExtraData(buf);
                byte[] bytes = ByteBufUtil.getBytes(buf);
                buf.release();
                return bytes;
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
    
    public static <T extends AbstractContainerMenu> MenuType<T> of(SimpleMenuTypeFactory<T> factory) {
        return new MenuType<>(factory::create, FeatureFlags.VANILLA_SET);
    }
    
    public static <T extends AbstractContainerMenu> MenuType<T> ofExtended(ExtendedMenuTypeFactory<T> factory) {
        return new ExtendedMenuType<>((syncId, inventory, data) -> {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
            T menu = factory.create(syncId, inventory, buf);
            buf.release();
            return menu;
        }, ByteBufCodecs.BYTE_ARRAY.mapStream(Function.identity()));
    }
}
