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

package me.shedaniel.architectury.registry.forge;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class CreativeTabsImpl {
    public static ItemGroup create(ResourceLocation resourceLocation, Supplier<ItemStack> supplier) {
        return new ItemGroup(String.format("%s.%s", resourceLocation.getNamespace(), resourceLocation.getPath())) {
            @Override
            @Nonnull
            public ItemStack makeIcon() {
                return supplier.get();
            }
        };
    }
}
