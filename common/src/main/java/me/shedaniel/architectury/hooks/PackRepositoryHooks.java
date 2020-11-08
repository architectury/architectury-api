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

package me.shedaniel.architectury.hooks;

import me.shedaniel.architectury.ArchitecturyPopulator;
import me.shedaniel.architectury.Populatable;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;

public class PackRepositoryHooks {
    private PackRepositoryHooks() {}
    
    @Populatable
    private static final Impl IMPL = null;
    
    public static void addSource(PackRepository repository, RepositorySource source) {
        IMPL.addSource(repository, source);
    }
    
    public interface Impl {
        void addSource(PackRepository repository, RepositorySource source);
    }
    
    static {
        ArchitecturyPopulator.populate();
    }
}
