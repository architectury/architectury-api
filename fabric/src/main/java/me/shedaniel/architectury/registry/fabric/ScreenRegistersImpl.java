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

package me.shedaniel.architectury.registry.fabric;

import java.util.function.BiFunction;

import me.shedaniel.architectury.ExpectPlatform;
import me.shedaniel.architectury.registry.ScreenRegisters;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;

public class ScreenRegistersImpl {
    public static <T extends AbstractContainerMenu> MenuType<T> registerMenuType(ResourceLocation identifier, BiFunction<Integer, Inventory, T> menuTypeSupplier) {
        return ScreenHandlerRegistry.registerSimple(identifier, (integer, inventory) -> menuTypeSupplier.apply(integer, inventory));
    }

    public static <T extends AbstractContainerMenu> MenuType<T> registerExtendedMenuType(ResourceLocation identifier, ScreenRegisters.ExtendedMenuTypeFactory<T> menuTypeSupplier) {
        return ScreenHandlerRegistry.registerExtended(identifier, (integer, inventory, friendlyByteBuf) -> menuTypeSupplier.create(integer, inventory, friendlyByteBuf));
    }

    @Environment(EnvType.CLIENT)
    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreenFactory(MenuType<? extends H> menuType, ScreenRegisters.ScreenFactory<H, S> screenSupplier) {
        ScreenRegistry.register(menuType, (H h, Inventory i, Component t) -> screenSupplier.create(h, i, t));
    }
}
