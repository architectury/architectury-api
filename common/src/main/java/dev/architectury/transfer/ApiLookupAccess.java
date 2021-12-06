/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

package dev.architectury.transfer;

/**
 * The base interface for all lookup accesses.
 *
 * @param <T> the type of the API
 * @param <H> the type of the query handler
 * @param <R> the type of the registration handler
 */
public interface ApiLookupAccess<T, H, R> {
    /**
     * Adds a handler for querying the api, this will
     * only be called by architectury mods.
     * <p>
     * For adding interop with platform APIs, use the
     * register methods for linking and attaching to fabric's
     * api lookup or forge's capability system.
     *
     * @param handler the query handler to add
     */
    void addQueryHandler(H handler);
    
    /**
     * Adds a lookup registration.
     *
     * @param registration the registration handler to add
     */
    void addRegistrationHandler(R registration);
}
