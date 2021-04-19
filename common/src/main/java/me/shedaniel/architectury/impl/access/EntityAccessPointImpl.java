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
import me.shedaniel.architectury.core.access.specific.EntityAccess;
import me.shedaniel.architectury.core.access.specific.EntityAccessPoint;
import net.minecraft.world.entity.Entity;

public class EntityAccessPointImpl<T, SELF extends EntityAccessPointImpl<T, SELF>> implements EntityAccessPoint<T, SELF> {
    private final AccessPoint<EntityAccess<T>, ?> parent;
    
    public EntityAccessPointImpl() {
        this.parent = AccessPoint.create(ts -> entity -> {
            return processIterable(ts, entity);
        });
    }
    
    private T processIterable(Iterable<EntityAccess<T>> iterable, Entity entity) {
        for (EntityAccess<T> accessor : iterable) {
            T t = accessor.getByEntity(entity);
            if (t != null) {
                return t;
            }
        }
        
        return null;
    }
    
    @Override
    public AccessPoint<EntityAccess<T>, ?> getParent() {
        return parent;
    }
    
    @Override
    public T getByEntity(Entity entity) {
        return get().getByEntity(entity);
    }
}
