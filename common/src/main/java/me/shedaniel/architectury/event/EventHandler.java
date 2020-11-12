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

package me.shedaniel.architectury.event;

import me.shedaniel.architectury.ExpectPlatform;
import me.shedaniel.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public final class EventHandler {
    private EventHandler() {}
    
    private static boolean initialized = false;
    
    public static void init() {
        if (initialized) return;
        initialized = true;
        if (Platform.getEnv() == EnvType.CLIENT)
            registerClient();
        registerCommon();
        if (Platform.getEnv() == EnvType.SERVER)
            registerServer();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    private static void registerClient() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    private static void registerCommon() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.SERVER)
    private static void registerServer() {
        throw new AssertionError();
    }
}
