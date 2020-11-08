/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
