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

package dev.architectury.transfer;

import com.google.common.base.Predicates;
import dev.architectury.fluid.FluidStack;
import dev.architectury.transfer.wrapper.filtering.FilteringTransferHandler;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A handler for transferring resources.
 * This is wrapped around apis given by the platform,
 * <p>
 * <b>DO NOT</b> extend this interface, binary compatibility is not guaranteed
 * with future versions if you do.
 *
 * @param <T> the type of resource
 */
@ApiStatus.NonExtendable
public interface TransferHandler<T> extends TransferView<T> {
    /**
     * Returns an empty item transfer handler, which does nothing.
     *
     * @return an empty item transfer handler
     */
    static TransferHandler<ItemStack> emptyItem() {
        return EmptyTransferHandler.Item.INSTANCE;
    }
    
    /**
     * Returns an empty fluid transfer handler, which does nothing.
     *
     * @return an empty fluid transfer handler
     */
    static TransferHandler<FluidStack> emptyFluid() {
        return EmptyTransferHandler.Fluid.INSTANCE;
    }
    
    /**
     * Returns the iterable of immutable resources that are currently in the handler.<br>
     * <b>Please properly close this stream.</b> Failure to do so will result in a potential
     * crash in conflicting transactions.<br><br>
     * You can easily ensure that this is closed by using try-with-resources, otherwise
     * you will have to manually close the stream by calling {@link Stream#close()}.
     *
     * @return the iterable of resources that are currently in the handler
     */
    Stream<ResourceView<T>> getContents();
    
    default void withContents(Consumer<Iterable<ResourceView<T>>> consumer) {
        try (Stream<ResourceView<T>> stream = getContents()) {
            Iterable<ResourceView<T>> iterable = stream::iterator;
            consumer.accept(iterable);
        }
    }
    
    /**
     * Returns the size of the handler.
     * This may be extremely expensive to compute, avoid if you can.
     *
     * @return the size of the handler
     */
    @Deprecated
    int getContentsSize();
    
    /**
     * Returns the resource in a particular index.
     * This may be extremely expensive to compute, avoid if you can.<br>
     * <b>Please properly close this resource.</b> Failure to do so will result in a potential
     * crash in conflicting transactions.<br><br>
     * You can easily ensure that this is closed by using try-with-resources, otherwise
     * you will have to manually close the stream by calling {@link ResourceView#close()}.
     *
     * @param index the index of the resource
     * @return the resource in the given index
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    @Deprecated
    ResourceView<T> getContent(int index);
    
    default void withContent(int index, Consumer<ResourceView<T>> consumer) {
        try (ResourceView<T> resource = getContent(index)) {
            consumer.accept(resource);
        }
    }
    
    /**
     * Inserts the given resource into the handler, returning the amount that was inserted.
     *
     * @param toInsert the resource to insert
     * @param action   whether to simulate or actually insert the resource
     * @return the amount that was inserted
     */
    long insert(T toInsert, TransferAction action);
    
    /**
     * Extracts the given resource from a given resource index, returning the stack that was extracted.
     *
     * @param index     the index of the resource
     * @param toExtract the resource to extract
     * @param action    whether to simulate or actually extract the resource
     * @return the stack that was extracted
     */
    default T extract(int index, T toExtract, TransferAction action) {
        try (ResourceView<T> resource = getContent(index)) {
            return resource.extract(toExtract, action);
        }
    }
    
    /**
     * Extracts the given resource from a given resource index, returning the stack that was extracted.
     *
     * @param index     the index of the resource
     * @param toExtract the predicates to use to filter the resources to extract
     * @param maxAmount the maximum amount of resources to extract
     * @param action    whether to simulate or actually extract the resource
     * @return the stack that was extracted
     */
    default T extract(int index, Predicate<T> toExtract, long maxAmount, TransferAction action) {
        try (ResourceView<T> resource = getContent(index)) {
            return resource.extract(toExtract, maxAmount, action);
        }
    }
    
    /**
     * Extracts the any resource from a given resource index, returning the stack that was extracted.
     *
     * @param index     the index of the resource
     * @param maxAmount the maximum amount of resources to extract
     * @param action    whether to simulate or actually extract the resource
     * @return the stack that was extracted
     */
    default T extract(int index, long maxAmount, TransferAction action) {
        try (ResourceView<T> resource = getContent(index)) {
            return resource.extract(maxAmount, action);
        }
    }
    
    default TransferHandler<T> unmodifiable() {
        return filter(Predicates.alwaysFalse());
    }
    
    default TransferHandler<T> onlyInsert() {
        return filter(Predicates.alwaysTrue(), Predicates.alwaysFalse());
    }
    
    default TransferHandler<T> onlyExtract() {
        return filter(Predicates.alwaysFalse(), Predicates.alwaysTrue());
    }
    
    default TransferHandler<T> filter(Predicate<T> filter) {
        return filter(filter, filter);
    }
    
    default TransferHandler<T> filter(Predicate<T> insert, Predicate<T> extract) {
        return FilteringTransferHandler.of(this, insert, extract);
    }
}
