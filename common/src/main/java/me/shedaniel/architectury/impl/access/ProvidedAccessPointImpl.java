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

package me.shedaniel.architectury.impl.access;

import me.shedaniel.architectury.core.access.AccessPoint;
import me.shedaniel.architectury.core.access.specific.LevelAccess;
import me.shedaniel.architectury.core.access.specific.LevelAccessPoint;
import me.shedaniel.architectury.core.access.specific.ProvidedAccess;
import me.shedaniel.architectury.core.access.specific.ProvidedAccessPoint;
import net.minecraft.world.level.Level;

public class ProvidedAccessPointImpl<T, SELF extends ProvidedAccessPointImpl<T, SELF>> implements ProvidedAccessPoint<T, SELF> {
    private final AccessPoint<ProvidedAccess<T>, ?> parent;
    
    public ProvidedAccessPointImpl() {
        this.parent = AccessPoint.create(ts -> object -> {
            return processIterable(ts, object);
        });
    }
    
    private T processIterable(Iterable<ProvidedAccess<T>> iterable, Object object) {
        for (ProvidedAccess<T> accessor : iterable) {
            T t = accessor.get(object);
            if (t != null) {
                return t;
            }
        }
        
        return null;
    }
    
    @Override
    public AccessPoint<ProvidedAccess<T>, ?> getParent() {
        return parent;
    }
    
    @Override
    public T get(Object object) {
        return get().get(object);
    }
}
