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

package dev.architectury.networking.transformers;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@ApiStatus.Experimental
public interface PacketTransformer {
    void inbound(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, NetworkManager.PacketContext context, TransformationSink sink);
    
    void outbound(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, TransformationSink sink);
    
    @FunctionalInterface
    interface TransformationSink {
        void accept(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf);
    }
    
    static PacketTransformer none() {
        return new PacketTransformer() {
            @Override
            public void inbound(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, NetworkManager.PacketContext context, TransformationSink sink) {
                sink.accept(side, id, buf);
            }
            
            @Override
            public void outbound(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, TransformationSink sink) {
                sink.accept(side, id, buf);
            }
        };
    }
    
    static PacketTransformer concat(Iterable<? extends PacketTransformer> transformers) {
        if (transformers instanceof Collection && ((Collection<? extends PacketTransformer>) transformers).isEmpty()) {
            return PacketTransformer.none();
        } else if (transformers instanceof Collection && ((Collection<? extends PacketTransformer>) transformers).size() == 1) {
            return transformers.iterator().next();
        }
        return new PacketTransformer() {
            @Override
            public void inbound(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, NetworkManager.PacketContext context, TransformationSink sink) {
                traverse(side, id, buf, context, sink, true, 0);
            }
            
            @Override
            public void outbound(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, TransformationSink sink) {
                traverse(side, id, buf, null, sink, false, 0);
            }
            
            private void traverse(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, @Nullable NetworkManager.PacketContext context, TransformationSink outerSink, boolean inbound, int index) {
                if (transformers instanceof List) {
                    if (((List<? extends PacketTransformer>) transformers).size() > index) {
                        PacketTransformer transformer = ((List<? extends PacketTransformer>) transformers).get(index);
                        TransformationSink sink = (side1, id1, buf1) -> {
                            traverse(side1, id1, buf1, context, outerSink, inbound, index + 1);
                        };
                        if (inbound) {
                            transformer.inbound(side, id, buf, context, sink);
                        } else {
                            transformer.outbound(side, id, buf, sink);
                        }
                    } else {
                        outerSink.accept(side, id, buf);
                    }
                } else {
                    Iterator<? extends PacketTransformer> iterator = transformers.iterator();
                    for (int i = 0; i < index; i++) {
                        iterator.next();
                    }
                    PacketTransformer transformer = iterator.hasNext() ? iterator.next() : PacketTransformer.none();
                    TransformationSink sink = (side1, id1, buf1) -> {
                        if (iterator.hasNext()) {
                            traverse(side1, id1, buf1, context, outerSink, inbound, index + 1);
                        } else {
                            outerSink.accept(side1, id1, buf1);
                        }
                    };
                    if (inbound) {
                        transformer.inbound(side, id, buf, context, sink);
                    } else {
                        transformer.outbound(side, id, buf, sink);
                    }
                }
            }
        };
    }
}
