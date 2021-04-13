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

package me.shedaniel.architectury.mixin.fabric.client;

import me.shedaniel.architectury.event.events.EntityEvent;
import me.shedaniel.architectury.event.events.client.ClientLifecycleEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public class MixinClientLevel {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void construct(ClientPacketListener clientPacketListener, ClientLevel.ClientLevelData clientLevelData, ResourceKey<Level> resourceKey, DimensionType dimensionType, int i, Supplier<ProfilerFiller> supplier, LevelRenderer levelRenderer, boolean bl, long l, CallbackInfo ci) {
        ClientLifecycleEvent.CLIENT_WORLD_LOAD.invoker().act((ClientLevel) (Object) this);
    }
    
    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    private void addEntity(int i, Entity entity, CallbackInfo ci) {
        if (EntityEvent.ADD.invoker().add(entity, (ClientLevel) (Object) this) == InteractionResult.FAIL) {
            ci.cancel();
        }
    }
}
