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

package me.shedaniel.architectury.registry.forge;

import me.shedaniel.architectury.registry.MenuRegistry.ExtendedMenuTypeFactory;
import me.shedaniel.architectury.registry.MenuRegistry.ScreenFactory;
import me.shedaniel.architectury.registry.MenuRegistry.SimpleMenuTypeFactory;
import me.shedaniel.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.network.NetworkHooks;

public class MenuRegistryImpl {
    public static void openMenu(ServerPlayer player, ExtendedMenuProvider provider) {
        NetworkHooks.openGui(player, provider, provider::saveExtraData);
    }
    
    public static <T extends AbstractContainerMenu> MenuType<T> registerMenuType(SimpleMenuTypeFactory<T> factory) {
        return new MenuType<>(factory::create);
    }
    
    public static <T extends AbstractContainerMenu> MenuType<T> registerExtendedMenuType(ExtendedMenuTypeFactory<T> factory) {
        return IForgeContainerType.create(factory::create);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreenFactory(MenuType<? extends H> type, ScreenFactory<H, S> factory) {
        MenuScreens.register(type, factory::create);
    }
}
