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

import dev.architectury.registry.registries.options.OnAddCallback;
import dev.architectury.registry.registries.options.OnCreateCallback;
import dev.architectury.registry.registries.options.RegistrarOption;
import dev.architectury.registry.registries.options.StandardRegistrarOptions;

/**
 * A builder to create a new {@link Registrar}.
 *
 * @param <T> The type of registry object
 * @author ebo2022
 * @since
 */
public interface RegistrarBuilder<T> {
    
    Registrar<T> build();
    
    RegistrarBuilder<T> option(RegistrarOption option);
    
    default RegistrarBuilder<T> saveToDisc() {
        return this.option(StandardRegistrarOptions.SAVE_TO_DISC);
    }
    
    default RegistrarBuilder<T> syncToClients() {
        return this.option(StandardRegistrarOptions.SYNC_TO_CLIENTS);
    }
    
    default RegistrarBuilder<T> onAdd(OnAddCallback<T> callback) {
        return this.option(callback);
    }
    
    default RegistrarBuilder<T> onCreate(OnCreateCallback<T> callback) {
        return this.option(callback);
    }
}
