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

package me.shedaniel.architectury.event.events;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import java.util.List;

public interface ExplosionEvent {
    Event<Pre> PRE = EventFactory.createInteractionResult(Pre.class);
    Event<Detonate> DETONATE = EventFactory.createInteractionResult(Detonate.class);
    
    interface Pre {
        InteractionResult explode(Level world, Explosion explosion);
    }
    
    interface Detonate {
        void explode(Level world, Explosion explosion, List<Entity> affectedEntities);
    }
}
