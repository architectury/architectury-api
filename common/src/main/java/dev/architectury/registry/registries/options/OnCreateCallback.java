/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022, 2023 architectury
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

package dev.architectury.registry.registries.options;

import dev.architectury.registry.registries.Registrar;

import java.util.function.Consumer;

/**
 * A {@link RegistrarOption} that adds code to run when the registrar is created.
 * <p>Fabric: This runs immediately after the registry is created.
 * <p>Forge: This runs during <code>NewRegistryEvent</code> at the same time the built registrar is initialized.
 *
 * @param <T> The registry object type
 */
@FunctionalInterface
public interface OnCreateCallback<T> extends Consumer<Registrar<T>>, RegistrarOption {
}
