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

package dev.architectury.impl.fabric;

import dev.architectury.event.events.common.ChatEvent;
import net.minecraft.network.chat.Component;

public class ChatComponentImpl implements ChatEvent.ChatComponent {
    private Component raw;
    private Component filtered;
    
    public ChatComponentImpl(Component raw, Component filtered) {
        this.raw = raw;
        this.filtered = filtered;
    }
    
    @Override
    public Component getRaw() {
        return raw;
    }
    
    @Override
    public Component getFiltered() {
        return filtered;
    }
    
    @Override
    public void setRaw(Component raw) {
        this.raw = raw;
    }
    
    @Override
    public void setFiltered(Component filtered) {
        this.filtered = filtered;
    }
}
