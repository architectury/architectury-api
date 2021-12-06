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

import dev.architectury.transfer.access.ItemLookupRegistration;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface ForgeItemLookupRegistration<T, Cap, Context> extends ItemLookupRegistration<T, Context> {
    static <T, C, Context> ForgeItemLookupRegistration<T, C, Context> create(Capability<C> capability, ItemAccessProvider<BiFunction<Direction, T, C>> transformer) {
        return new ForgeItemLookupRegistration<T, C, Context>() {
            @Override
            public Capability<C> getCapability() {
                return capability;
            }
            
            @Override
            public C from(ItemStack stack, @Nullable Direction arg, T handler) {
                return transformer.get(stack).apply(arg, handler);
            }
        };
    }
    
    Capability<Cap> getCapability();
    
    Cap from(ItemStack stack, @Nullable Direction arg, T handler);
    
    @Override
    default boolean register(ResourceLocation id, ItemAccessProvider<Function<Context, T>> provider) {
        CapabilitiesAttachListeners.add(event -> {
            if (event.getObject() instanceof ItemStack) {
                ItemStack stack = (ItemStack) event.getObject();
                Function<Context, T> applicator = provider.get(stack);
                if (applicator != null) {
                    event.addCapability(id, new ICapabilityProvider() {
                        @NotNull
                        @Override
                        public <S> LazyOptional<S> getCapability(@NotNull Capability<S> capability, @Nullable Direction arg) {
                            if (capability == ForgeItemLookupRegistration.this.getCapability()) {
                                T handler = applicator.apply(null);
                                
                                return handler == null ? LazyOptional.empty() : LazyOptional.of(() -> from(stack, arg, handler)).cast();
                            }
                            
                            return LazyOptional.empty();
                        }
                    });
                }
            }
        });
        
        return true;
    }
}
