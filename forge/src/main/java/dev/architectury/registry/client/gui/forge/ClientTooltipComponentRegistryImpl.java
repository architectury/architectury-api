/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022 architectury
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package dev.architectury.registry.client.gui.forge;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
@ApiStatus.Internal
public class ClientTooltipComponentRegistryImpl {
    @Nullable
    private static List<Factory<?>> FACTORIES = new ArrayList<>();
    
    public static void consume(Consumer<? super Factory<?>> consumer) {
        if (FACTORIES != null) {
            FACTORIES.forEach(consumer);
            FACTORIES = null;
        }
    }
    
    public static <T extends TooltipComponent> void register(Class<T> clazz, Function<? super T, ? extends ClientTooltipComponent> factory) {
        if (FACTORIES == null) {
            throw new IllegalStateException("Cannot register ClientTooltipComponent factory when factories are already aggregated!");
        }
        FACTORIES.add(new Factory<>(clazz, factory));
    }
    
    public record Factory<T extends TooltipComponent>(
            Class<T> clazz, Function<? super T, ? extends ClientTooltipComponent> factory
    ) {
    }
}
