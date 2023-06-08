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

package dev.architectury.event.forge;

import dev.architectury.event.events.common.LootEvent;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;

final class LootTableModificationContextImpl implements LootEvent.LootTableModificationContext {
    private final LootTable table;
    private final List<LootPool> pools;
    
    LootTableModificationContextImpl(LootTable table) {
        this.table = table;
        
        // This field has the type changed to List<LootPool> by Forge
        // Since this is rather unsafe, we are making sure 100% we are getting it
        List<LootPool> pools = null;
        try {
            pools = ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, "f_79109_");
        } catch (ObfuscationReflectionHelper.UnableToFindFieldException ignored) {
            for (Field field : LootTable.class.getDeclaredFields()) {
                if (field.getType().equals(List.class)) {
                    // This is probably the field
                    field.setAccessible(true);
                    try {
                        pools = (List<LootPool>) field.get(table);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            
            if (pools == null) {
                throw new RuntimeException("Unable to find pools field in LootTable!");
            }
        }
        
        this.pools = pools;
    }
    
    @Override
    public void addPool(LootPool pool) {
        this.pools.add(pool);
    }
    
    @Override
    public void addPool(LootPool.Builder pool) {
        this.pools.add(pool.build());
    }
}
