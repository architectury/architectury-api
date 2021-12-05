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

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CapabilitiesAttachListeners {
    private static final List<Consumer<AttachCapabilitiesEvent>> LISTENERS = new ArrayList<>();
    
    static {
        MinecraftForge.EVENT_BUS.register(CapabilitiesAttachListeners.class);
    }
    
    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent event) {
        for (Consumer<AttachCapabilitiesEvent> consumer : LISTENERS) {
            consumer.accept(event);
        }
    }
    
    public static void add(Consumer<AttachCapabilitiesEvent> consumer) {
        LISTENERS.add(consumer);
    }
}
