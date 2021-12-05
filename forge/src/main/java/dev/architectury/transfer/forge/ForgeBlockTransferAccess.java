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

package dev.architectury.transfer.forge;

import dev.architectury.transfer.access.BlockTransferAccess;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface ForgeBlockTransferAccess<T, C> extends BlockTransferAccess<T, Direction> {
    Capability<C> getCapability();
    
    C from(T handler);
    
    @Override
    default void register(ResourceLocation id, BlockAccessProvider<T, Direction> provider) {
        CapabilitiesAttachListeners.add(event -> {
            if (event.getObject() instanceof BlockEntity) {
                BlockEntity blockEntity = (BlockEntity) event.getObject();
                Function<Direction, T> applicator = provider.get(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity);
                if (applicator != null) {
                    event.addCapability(id, new ICapabilityProvider() {
                        @NotNull
                        @Override
                        public <S> LazyOptional<S> getCapability(@NotNull Capability<S> capability, @Nullable Direction arg) {
                            if (capability == ForgeBlockTransferAccess.this.getCapability()) {
                                T handler = applicator.apply(arg);
                                
                                return LazyOptional.of(() -> from(handler)).cast();
                            }
                            
                            return LazyOptional.empty();
                        }
                    });
                }
            }
        });
    }
}
