/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

package me.shedaniel.architectury.mixin.forge;

import me.shedaniel.architectury.event.forge.EventHandlerImplCommon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.ref.WeakReference;

@Mixin(ChunkSerializer.class)
public class MixinChunkSerializer {
    @Unique
    private static ThreadLocal<WeakReference<ServerLevel>> level = new ThreadLocal<>();
    
    @Inject(method = "read", at = @At("HEAD"))
    private static void read(ServerLevel worldIn, StructureManager templateManagerIn, PoiManager poiManager, ChunkPos pos, CompoundTag compound, CallbackInfoReturnable<ProtoChunk> cir) {
        level.set(new WeakReference<>(worldIn));
    }
    
    @ModifyArg(method = "read", at = @At(value = "INVOKE",
            ordinal = 1,
            target = "Lnet/minecraftforge/eventbus/api/IEventBus;post(Lnet/minecraftforge/eventbus/api/Event;)Z"),
            index = 0)
    private static Event modifyProtoChunkLevel(Event event) {
        // We should get this PRed to Forge
        WeakReference<ServerLevel> levelRef = level.get();
        if (levelRef != null && event instanceof ChunkDataEvent.Load) {
            ChunkDataEvent.Load load = (ChunkDataEvent.Load) event;
            ((EventHandlerImplCommon.WorldEventAttachment) load).architectury$attachLevel(levelRef.get());
        }
        level.set(null);
        return event;
    }
}
