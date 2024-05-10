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

package dev.architectury.impl;

import com.google.common.base.Suppliers;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.transformers.PacketSink;
import dev.architectury.networking.transformers.PacketTransformer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@ApiStatus.Internal
public class NetworkAggregator {
    public static final Supplier<Adaptor> ADAPTOR = Suppliers.memoize(() -> {
        try {
            Method adaptor = NetworkManager.class.getDeclaredMethod("getAdaptor");
            adaptor.setAccessible(true);
            return (Adaptor) adaptor.invoke(null);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    });
    public static final Map<ResourceLocation, CustomPacketPayload.Type<BufCustomPacketPayload>> C2S_TYPE = new HashMap<>();
    public static final Map<ResourceLocation, CustomPacketPayload.Type<BufCustomPacketPayload>> S2C_TYPE = new HashMap<>();
    public static final Map<ResourceLocation, NetworkManager.NetworkReceiver<?>> C2S_RECEIVER = new HashMap<>();
    public static final Map<ResourceLocation, NetworkManager.NetworkReceiver<?>> S2C_RECEIVER = new HashMap<>();
    public static final Map<ResourceLocation, StreamCodec<ByteBuf, ?>> C2S_CODECS = new HashMap<>();
    public static final Map<ResourceLocation, StreamCodec<ByteBuf, ?>> S2C_CODECS = new HashMap<>();
    public static final Map<ResourceLocation, PacketTransformer> C2S_TRANSFORMERS = new HashMap<>();
    public static final Map<ResourceLocation, PacketTransformer> S2C_TRANSFORMERS = new HashMap<>();
    
    public static void registerReceiver(NetworkManager.Side side, ResourceLocation id, List<PacketTransformer> packetTransformers, NetworkManager.NetworkReceiver<RegistryFriendlyByteBuf> receiver) {
        CustomPacketPayload.Type<BufCustomPacketPayload> type = new CustomPacketPayload.Type<>(id);
        if (side == NetworkManager.Side.C2S) {
            C2S_TYPE.put(id, type);
            registerC2SReceiver(type, BufCustomPacketPayload.streamCodec(type), packetTransformers, (value, context) -> {
                RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(value.payload()), context.registryAccess());
                receiver.receive(buf, context);
                buf.release();
            });
        } else if (side == NetworkManager.Side.S2C) {
            S2C_TYPE.put(id, type);
            registerS2CReceiver(type, BufCustomPacketPayload.streamCodec(type), packetTransformers, (value, context) -> {
                RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(value.payload()), context.registryAccess());
                receiver.receive(buf, context);
                buf.release();
            });
        }
    }
    
    public static <T extends CustomPacketPayload> void registerReceiver(NetworkManager.Side side, CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, List<PacketTransformer> packetTransformers, NetworkManager.NetworkReceiver<T> receiver) {
        Objects.requireNonNull(type, "Cannot register receiver with a null type!");
        packetTransformers = Objects.requireNonNullElse(packetTransformers, List.of());
        Objects.requireNonNull(receiver, "Cannot register a null receiver!");
        if (side == NetworkManager.Side.C2S) {
            registerC2SReceiver(type, codec, packetTransformers, receiver);
        } else if (side == NetworkManager.Side.S2C) {
            registerS2CReceiver(type, codec, packetTransformers, receiver);
        }
    }
    
    private static <T extends CustomPacketPayload> void registerC2SReceiver(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, List<PacketTransformer> packetTransformers, NetworkManager.NetworkReceiver<T> receiver) {
        PacketTransformer transformer = PacketTransformer.concat(packetTransformers);
        C2S_RECEIVER.put(type.id(), receiver);
        C2S_CODECS.put(type.id(), (StreamCodec<ByteBuf, ?>) codec);
        C2S_TRANSFORMERS.put(type.id(), transformer);
        ADAPTOR.get().registerC2S((CustomPacketPayload.Type<BufCustomPacketPayload>) type, BufCustomPacketPayload.streamCodec((CustomPacketPayload.Type<BufCustomPacketPayload>) type), (payload, context) -> {
            RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(payload.payload()), context.registryAccess());
            transformer.inbound(NetworkManager.Side.C2S, type.id(), buf, context, (side, id1, buf1) -> {
                NetworkManager.NetworkReceiver<T> networkReceiver = (NetworkManager.NetworkReceiver<T>) (side == NetworkManager.Side.C2S ? C2S_RECEIVER.get(id1) : S2C_RECEIVER.get(id1));
                if (networkReceiver == null) {
                    throw new IllegalArgumentException("Network Receiver not found! " + id1);
                }
                T actualPayload = codec.decode(buf1);
                networkReceiver.receive(actualPayload, context);
            });
            buf.release();
        });
    }
    
    private static <T extends CustomPacketPayload> void registerS2CReceiver(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, List<PacketTransformer> packetTransformers, NetworkManager.NetworkReceiver<T> receiver) {
        PacketTransformer transformer = PacketTransformer.concat(packetTransformers);
        S2C_RECEIVER.put(type.id(), receiver);
        S2C_CODECS.put(type.id(), (StreamCodec<ByteBuf, ?>) codec);
        S2C_TRANSFORMERS.put(type.id(), transformer);
        ADAPTOR.get().registerS2C((CustomPacketPayload.Type<BufCustomPacketPayload>) type, BufCustomPacketPayload.streamCodec((CustomPacketPayload.Type<BufCustomPacketPayload>) type), (payload, context) -> {
            RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(payload.payload()), context.registryAccess());
            transformer.inbound(NetworkManager.Side.S2C, type.id(), buf, context, (side, id1, buf1) -> {
                NetworkManager.NetworkReceiver<T> networkReceiver = (NetworkManager.NetworkReceiver<T>) (side == NetworkManager.Side.C2S ? C2S_RECEIVER.get(id1) : S2C_RECEIVER.get(id1));
                if (networkReceiver == null) {
                    throw new IllegalArgumentException("Network Receiver not found! " + id1);
                }
                T actualPayload = codec.decode(buf1);
                networkReceiver.receive(actualPayload, context);
            });
            buf.release();
        });
    }
    
    public static void collectPackets(PacketSink sink, NetworkManager.Side side, ResourceLocation id, RegistryFriendlyByteBuf buf) {
        if (side == NetworkManager.Side.C2S) {
            collectPackets(sink, side, new BufCustomPacketPayload(C2S_TYPE.get(id), ByteBufUtil.getBytes(buf)), buf.registryAccess());
        } else {
            collectPackets(sink, side, new BufCustomPacketPayload(S2C_TYPE.get(id), ByteBufUtil.getBytes(buf)), buf.registryAccess());
        }
    }
    
    public static <T extends CustomPacketPayload> void collectPackets(PacketSink sink, NetworkManager.Side side, T payload, RegistryAccess access) {
        CustomPacketPayload.Type<T> type = (CustomPacketPayload.Type<T>) payload.type();
        PacketTransformer transformer = side == NetworkManager.Side.C2S ? C2S_TRANSFORMERS.get(type.id()) : S2C_TRANSFORMERS.get(type.id());
        StreamCodec<ByteBuf, T> codec = (StreamCodec<ByteBuf, T>) (side == NetworkManager.Side.C2S ? C2S_CODECS.get(type.id()) : S2C_CODECS.get(type.id()));
        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), access);
        codec.encode(buf, payload);
        
        if (transformer != null) {
            transformer.outbound(side, type.id(), buf, (side1, id1, buf1) -> {
                if (side == NetworkManager.Side.C2S) {
                    CustomPacketPayload.Type<BufCustomPacketPayload> type1 = C2S_TYPE.getOrDefault(id1, (CustomPacketPayload.Type<BufCustomPacketPayload>) type);
                    sink.accept(toPacket(side1, new BufCustomPacketPayload(type1, ByteBufUtil.getBytes(buf1))));
                } else if (side == NetworkManager.Side.S2C) {
                    CustomPacketPayload.Type<BufCustomPacketPayload> type1 = S2C_TYPE.getOrDefault(id1, (CustomPacketPayload.Type<BufCustomPacketPayload>) type);
                    sink.accept(toPacket(side1, new BufCustomPacketPayload(type1, ByteBufUtil.getBytes(buf1))));
                }
            });
        } else {
            sink.accept(toPacket(side, new BufCustomPacketPayload((CustomPacketPayload.Type<BufCustomPacketPayload>) type, ByteBufUtil.getBytes(buf))));
        }
        buf.release();
    }
    
    public static <T extends CustomPacketPayload> Packet<?> toPacket(NetworkManager.Side side, T payload) {
        if (side == NetworkManager.Side.C2S) {
            return ADAPTOR.get().toC2SPacket(payload);
        } else if (side == NetworkManager.Side.S2C) {
            return ADAPTOR.get().toS2CPacket(payload);
        }
        
        throw new IllegalArgumentException("Invalid side: " + side);
    }
    
    public static void registerS2CType(ResourceLocation id, List<PacketTransformer> packetTransformers) {
        CustomPacketPayload.Type<BufCustomPacketPayload> type = new CustomPacketPayload.Type<>(id);
        S2C_TYPE.put(id, type);
        registerS2CType(type, BufCustomPacketPayload.streamCodec(type), packetTransformers);
    }
    
    public static <T extends CustomPacketPayload> void registerS2CType(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, List<PacketTransformer> packetTransformers) {
        Objects.requireNonNull(type, "Cannot register a null type!");
        packetTransformers = Objects.requireNonNullElse(packetTransformers, List.of());
        S2C_CODECS.put(type.id(), (StreamCodec<ByteBuf, ?>) codec);
        S2C_TRANSFORMERS.put(type.id(), PacketTransformer.concat(packetTransformers));
        ADAPTOR.get().registerS2CType((CustomPacketPayload.Type<BufCustomPacketPayload>) type, BufCustomPacketPayload.streamCodec((CustomPacketPayload.Type<BufCustomPacketPayload>) type));
    }
    
    public interface Adaptor {
        <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.NetworkReceiver<T> receiver);
        
        <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.NetworkReceiver<T> receiver);
        
        <T extends CustomPacketPayload> Packet<?> toC2SPacket(T payload);
        
        <T extends CustomPacketPayload> Packet<?> toS2CPacket(T payload);
        
        <T extends CustomPacketPayload> void registerS2CType(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec);
    }
    
    public record BufCustomPacketPayload(Type<BufCustomPacketPayload> _type,
                                         byte[] payload) implements CustomPacketPayload {
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return this._type();
        }
        
        public static StreamCodec<ByteBuf, BufCustomPacketPayload> streamCodec(Type<BufCustomPacketPayload> type) {
            return ByteBufCodecs.BYTE_ARRAY.map(bytes -> new BufCustomPacketPayload(type, bytes), BufCustomPacketPayload::payload);
        }
    }
}
