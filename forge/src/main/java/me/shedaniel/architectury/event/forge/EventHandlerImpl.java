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

import me.shedaniel.architectury.forge.ArchitecturyForge;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

public class EventHandlerImpl {
    @OnlyIn(Dist.CLIENT)
    public static void registerClient() {
        MinecraftForge.EVENT_BUS.register(EventHandlerImplClient.class);
        EventBuses.getModEventBus(ArchitecturyForge.MOD_ID).orElseThrow(() -> new IllegalStateException("Where is architectury?")).register(EventHandlerImplClient.ModBasedEventHandler.class);
    }
    
    public static void registerCommon() {
        MinecraftForge.EVENT_BUS.register(EventHandlerImplCommon.class);
        EventBuses.getModEventBus(ArchitecturyForge.MOD_ID).orElseThrow(() -> new IllegalStateException("Where is architectury?")).register(EventHandlerImplCommon.ModBasedEventHandler.class);
    }
    
    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void registerServer() {
        MinecraftForge.EVENT_BUS.register(EventHandlerImplServer.class);
        EventBuses.getModEventBus(ArchitecturyForge.MOD_ID).orElseThrow(() -> new IllegalStateException("Where is architectury?")).register(EventHandlerImplServer.ModBasedEventHandler.class);
    }
}
