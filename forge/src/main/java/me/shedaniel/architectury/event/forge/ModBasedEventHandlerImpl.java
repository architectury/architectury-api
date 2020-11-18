/*
 * This file is part of architectury.
 * Copyright (C) 2020 shedaniel
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

package me.shedaniel.architectury.event.forge;

import me.shedaniel.architectury.event.events.TextureStitchEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModBasedEventHandlerImpl {
    @OnlyIn(Dist.CLIENT)
    public static class Client {
        @SubscribeEvent
        public static void event(net.minecraftforge.client.event.TextureStitchEvent.Pre event) {
            TextureStitchEvent.PRE.invoker().stitch(event.getMap(), event::addSprite);
        }
        
        @SubscribeEvent
        public static void event(net.minecraftforge.client.event.TextureStitchEvent.Post event) {
            TextureStitchEvent.POST.invoker().stitch(event.getMap());
        }
    }
    
    public static class Common {
        
    }
    
    @OnlyIn(Dist.DEDICATED_SERVER)
    public static class Server {
        
    }
}
