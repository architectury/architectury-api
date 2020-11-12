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

package me.shedaniel.architectury;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

@ApiStatus.Internal
public class Architectury {
    private static final String MOD_LOADER;
    private static final ImmutableMap<String,String> MOD_LOADERS = ImmutableMap.<String,String>builder()
            .put("net.fabricmc.loader.FabricLoader", "fabric")
            .put("net.minecraftforge.fml.common.Mod", "forge")
            .build();
    
    public static String getModLoader() {
        return MOD_LOADER;
    }
    
    static {
        String loader = null;
        for (Map.Entry<String, String> entry : MOD_LOADERS.entrySet()) {
            try {
                Class.forName(entry.getKey(), false, Architectury.class.getClassLoader());
                loader = entry.getValue();
                break;
            } catch (ClassNotFoundException ignored) {}
        }
        if (loader == null)
            throw new IllegalStateException("No detected mod loader!");
        MOD_LOADER = loader;
    }
}
