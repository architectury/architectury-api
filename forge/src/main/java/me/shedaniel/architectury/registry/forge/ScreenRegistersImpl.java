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

import java.util.function.BiFunction;

import me.shedaniel.architectury.forge.ArchitecturyForge;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.ScreenRegisters;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ScreenRegistersImpl {
    private static final DeferredRegister MENU_TYPE_REGISTRY = DeferredRegister.create(ArchitecturyForge.MOD_ID, Registry.MENU_REGISTRY);

    public static <T extends AbstractContainerMenu> MenuType<T> registerMenuType(ResourceLocation identifier, BiFunction<Integer, Inventory, T> menuTypeSupplier) {
        MenuType<T> menuType = IForgeContainerType.create((windowId, inv, data) -> menuTypeSupplier.apply(windowId, inv));
        MENU_TYPE_REGISTRY.register(identifier, () -> menuType);
        return menuType;
    }

    public static <T extends AbstractContainerMenu> MenuType<T> registerExtendedMenuType(ResourceLocation identifier, ScreenRegisters.ExtendedMenuTypeFactory<T> menuTypeSupplier) {
        MenuType<T> menuType = IForgeContainerType.create((windowId, inv, data) -> menuTypeSupplier.create(windowId, inv, data));
        MENU_TYPE_REGISTRY.register(identifier, () -> menuType);
        return menuType;
    }

    @OnlyIn(Dist.CLIENT)
    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreenFactory(MenuType<? extends H> menuType, ScreenRegisters.ScreenFactory<H, S> screenSupplier) {
        MenuScreens.register(menuType, (H t, Inventory i, Component c) -> screenSupplier.create(t, i, c));
    }
}
