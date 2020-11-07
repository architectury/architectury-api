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

package me.shedaniel.architectury.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public interface Registry<T> extends Iterable<T> {
    Supplier<T> delegate(ResourceLocation id);
    
    Supplier<T> register(ResourceLocation id, Supplier<T> supplier);
    
    @Nullable
    ResourceLocation getId(T obj);
    
    Optional<ResourceKey<T>> getKey(T obj);
    
    @Nullable
    T get(ResourceLocation id);
    
    boolean contains(ResourceLocation id);
    
    boolean containsValue(T obj);
    
    Set<ResourceLocation> getIds();
    
    Set<Map.Entry<ResourceKey<T>, T>> entrySet();
    
    ResourceKey<? extends net.minecraft.core.Registry<T>> key();
}
