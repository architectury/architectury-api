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

package me.shedaniel.architectury.registry.registries;

import me.shedaniel.architectury.core.RegistryEntry;
import me.shedaniel.architectury.registry.Registry;
import org.jetbrains.annotations.NotNull;

public interface RegistryBuilder<T extends RegistryEntry<T>> {
    @NotNull
    Registry<T> build();
    
    @NotNull
    RegistryBuilder<T> option(@NotNull RegistryOption option);
    
    @NotNull
    default RegistryBuilder<T> saveToDisc() {
        return option(StandardRegistryOption.SAVE_TO_DISC);
    }
    
    @NotNull
    default RegistryBuilder<T> syncToClients() {
        return option(StandardRegistryOption.SYNC_TO_CLIENTS);
    }
}