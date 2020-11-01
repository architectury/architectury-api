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

package me.shedaniel.architectury.platform.forge;

import net.minecraftforge.eventbus.api.IEventBus;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class EventBuses {
    private EventBuses() {}
    
    private static final Map<String, IEventBus> EVENT_BUS_MAP = new HashMap<>();
    
    public static void registerModEventBus(String modId, IEventBus bus) {
        IEventBus previous = EVENT_BUS_MAP.put(modId, bus);
        if (previous != null) {
            EVENT_BUS_MAP.put(modId, previous);
            throw new IllegalStateException("Can't register event bus for mod '" + modId + "' because it was previously registered!");
        }
    }
    
    public static Optional<IEventBus> getModEventBus(String modId) {
        return Optional.ofNullable(EVENT_BUS_MAP.get(modId));
    }
}
