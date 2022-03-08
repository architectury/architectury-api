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

package dev.architectury.core.item;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ArchitecturySpawnEggItem extends SpawnEggItem {
    private static final Logger LOGGER = LogManager.getLogger(ArchitecturySpawnEggItem.class);
    
    private final RegistrySupplier<? extends EntityType<? extends Mob>> entityType;
    
    protected static DispenseItemBehavior createDispenseItemBehavior() {
        return new DefaultDispenseItemBehavior() {
            @Override
            public ItemStack execute(BlockSource source, ItemStack stack) {
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
                
                try {
                    entityType.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
                } catch (Exception var6) {
                    LOGGER.error("Error while dispensing spawn egg from dispenser at {}", source.getPos(), var6);
                    return ItemStack.EMPTY;
                }
                
                stack.shrink(1);
                source.getLevel().gameEvent(GameEvent.ENTITY_PLACE, source.getPos());
                return stack;
            }
        };
    }
    
    public ArchitecturySpawnEggItem(RegistrySupplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Properties properties) {
        this(entityType, backgroundColor, highlightColor, properties, createDispenseItemBehavior());
    }
    
    public ArchitecturySpawnEggItem(RegistrySupplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Properties properties,
                                    @Nullable DispenseItemBehavior dispenseItemBehavior) {
        super(null, backgroundColor, highlightColor, properties);
        this.entityType = Objects.requireNonNull(entityType, "entityType");
        SpawnEggItem.BY_ID.remove(null);
        entityType.listen(type -> {
            SpawnEggItem.BY_ID.put(type, this);
            this.defaultType = entityType.get();
        });
    }
    
    @Override
    public EntityType<?> getType(@Nullable CompoundTag compoundTag) {
        EntityType<?> type = super.getType(compoundTag);
        return type == null ? entityType.get() : type;
    }
}
