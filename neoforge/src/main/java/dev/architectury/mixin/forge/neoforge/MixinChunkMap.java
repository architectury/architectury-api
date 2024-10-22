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

package dev.architectury.mixin.forge.neoforge;

import dev.architectury.event.forge.EventHandlerImplCommon;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChunkMap.class)
public class MixinChunkMap {
    @Shadow
    @Final
    ServerLevel level;
    
    @ModifyArg(method = {"method_43375", "lambda$scheduleChunkLoad$17"}, at = @At(value = "INVOKE",
            ordinal = 0,
            target = "Lnet/neoforged/bus/api/IEventBus;post(Lnet/neoforged/bus/api/Event;)Lnet/neoforged/bus/api/Event;"),
            index = 0, remap = false)
    private Event modifyProtoChunkLevel(Event event) {
        if (event instanceof ChunkDataEvent.Load) {
            ChunkDataEvent.Load load = (ChunkDataEvent.Load) event;
            ((EventHandlerImplCommon.LevelEventAttachment) load).architectury$attachLevel(this.level);
        }
        return event;
    }
}
