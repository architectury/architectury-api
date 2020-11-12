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

package me.shedaniel.architectury.forge;

import me.shedaniel.architectury.event.EventHandler;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.stream.Collectors;

@Mod(ArchitecturyForge.MOD_ID)
public class ArchitecturyForge {
    public static final String MOD_ID = "architectury";
    
    public ArchitecturyForge() {
        System.out.println(Platform.getMods().stream()
                .map(me.shedaniel.architectury.platform.Mod::getModId)
                .collect(Collectors.joining(", ")));
        EventBuses.registerModEventBus(ArchitecturyForge.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        EventHandler.init();
    }
}
