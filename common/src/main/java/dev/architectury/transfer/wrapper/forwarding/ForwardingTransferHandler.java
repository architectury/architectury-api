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

package dev.architectury.transfer.wrapper.forwarding;

import dev.architectury.transfer.ResourceView;
import dev.architectury.transfer.TransferHandler;

import java.util.stream.Stream;

public interface ForwardingTransferHandler<T> extends TransferHandler<T>, ForwardingTransferView<T> {
    @Override
    TransferHandler<T> forwardingTo();
    
    default ResourceView<T> forwardResource(ResourceView<T> resource) {
        return resource;
    }
    
    @Override
    default Stream<ResourceView<T>> getContents() {
        return forwardingTo().getContents().map(this::forwardResource);
    }
    
    @Override
    @Deprecated
    default int getContentsSize() {
        return forwardingTo().getContentsSize();
    }
    
    @Override
    @Deprecated
    default ResourceView<T> getContent(int index) {
        return forwardResource(forwardingTo().getContent(index));
    }
}
