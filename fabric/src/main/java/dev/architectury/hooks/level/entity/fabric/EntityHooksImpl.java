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

package dev.architectury.hooks.level.entity.fabric;

import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import org.jetbrains.annotations.Nullable;

public class EntityHooksImpl {
    public static String getEncodeId(Entity entity) {
        return entity.getEncodeId();
    }
    
    @Nullable
    public static Entity fromCollision(CollisionContext ctx) {
        return ctx instanceof EntityCollisionContext ? ((EntityCollisionContext) ctx).getEntity() : null;
    }
    
    public static EntityInLevelCallback wrapEntityInLevelCallback(Entity entity, EntityInLevelCallback callback) {
        if (callback == EntityInLevelCallback.NULL) return callback;
        if (callback == null) return callback;
        return new EntityInLevelCallback() {
            private long lastSectionKey = SectionPos.asLong(entity.blockPosition());
            
            @Override
            public void onMove() {
                callback.onMove();
                var currentSectionKey = SectionPos.asLong(entity.blockPosition());
                if (currentSectionKey != lastSectionKey) {
                    EntityEvent.ENTER_SECTION.invoker().enterSection(entity, SectionPos.x(lastSectionKey), SectionPos.y(lastSectionKey),
                            SectionPos.z(lastSectionKey), SectionPos.x(currentSectionKey), SectionPos.y(currentSectionKey),
                            SectionPos.z(currentSectionKey));
                    lastSectionKey = currentSectionKey;
                }
            }
            
            @Override
            public void onRemove(Entity.RemovalReason removalReason) {
                callback.onRemove(removalReason);
            }
        };
    }
}
