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

package dev.architectury.test.entity;

import com.google.common.base.Suppliers;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class TestEntity extends Cow {
    public static final Supplier<EntityType<TestEntity>> TYPE = Suppliers.memoize(() -> EntityType.Builder.of(TestEntity::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8).build("test_entity"));
    public static final Supplier<EntityType<TestEntity>> TYPE_2 = Suppliers.memoize(() -> EntityType.Builder.of(TestEntity::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8).build("test_entity_2"));
    
    public TestEntity(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        // Custom packets broken in BundlePacket
        // return NetworkManager.createAddEntityPacket(this);
        return super.getAddEntityPacket();
    }
}
