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

package me.shedaniel.architectury.test.entity;

import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;

public class TestEntity extends AbstractMinecart {
    public static final EntityType<TestEntity> TYPE = EntityType.Builder.of(TestEntity::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8).build("test_entity");
    
    public TestEntity(EntityType<? extends AbstractMinecart> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkManager.createAddEntityPacket(this);
    }
}
