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

package me.shedaniel.architectury.compat.fabric;

import com.google.common.collect.Maps;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.architectury.platform.fabric.PlatformImpl;
import me.shedaniel.architectury.platform.Mod;

import java.util.Map;

public class ModMenuCompatibility implements ModMenuApi {
    private static final Map<String, ConfigScreenFactory<?>> FACTORIES = Maps.newHashMap();
    
    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        validateMap();
        return FACTORIES;
    }
    
    private void validateMap() {
        for (Map.Entry<String, Mod.ConfigurationScreenProvider> entry : PlatformImpl.CONFIG_SCREENS.entrySet()) {
            if (!FACTORIES.containsKey(entry.getKey())) {
                FACTORIES.put(entry.getKey(), entry.getValue()::provide);
            }
        }
    }
}
