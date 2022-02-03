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

package dev.architectury.transfer.item.wrapper;

import dev.architectury.transfer.TransferHandler;
import dev.architectury.transfer.wrapper.FilteringTransferHandler;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class WorldlyContainerTransferHandler extends ContainerTransferHandler {
    protected final Direction direction;
    
    public WorldlyContainerTransferHandler(WorldlyContainer container, Direction direction) {
        super(container);
        this.direction = direction;
    }
    
    @Override
    protected Iterable<TransferHandler<ItemStack>> createHandlers() {
        WorldlyContainer container = (WorldlyContainer) this.container;
        int[] slots = container.getSlotsForFace(this.direction);
        TransferHandler<ItemStack>[] handlers = new TransferHandler[slots.length];
        for (int i = 0; i < slots.length; ++i) {
            int index = i;
            handlers[i] = FilteringTransferHandler.of(new SlotTransferHandler(container, slots[i]),
                    stack -> container.canPlaceItemThroughFace(index, stack, direction),
                    stack -> container.canTakeItemThroughFace(index, stack, direction));
        }
        return Arrays.asList(handlers);
    }
}
