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

package dev.architectury.registry.registries;

import net.minecraft.core.Holder;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

@ApiStatus.NonExtendable
public interface RegistrySupplier<T> extends DeferredSupplier<T>, Holder<T> {
    RegistrarManager getRegistrarManager();
    
    Registrar<T> getRegistrar();
    
    /**
     * Listens to when the registry entry is registered, and calls the given action.
     * Evaluates immediately if the entry is already registered.
     *
     * @param callback the action to call when the registry entry is registered
     */
    default void listen(Consumer<T> callback) {
        getRegistrar().listen(this, callback);
    }
}
