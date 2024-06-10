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

package dev.architectury.forge;

import dev.architectury.event.EventHandler;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.level.biome.forge.BiomeModificationsImpl;
import dev.architectury.utils.ArchitecturyConstants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ArchitecturyConstants.MOD_ID)
public class ArchitecturyForge {
    public ArchitecturyForge() {
        EventBuses.registerModEventBus(ArchitecturyConstants.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        EventHandler.init();
        BiomeModificationsImpl.init();
    }
}
