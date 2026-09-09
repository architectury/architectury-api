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

package dev.architectury.mixin.fabric;

import dev.architectury.event.events.common.BlockEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Fabric has no piston event, so both halves are bridged here. NeoForge fires its own
 * {@code PistonEvent.Pre} at the top of each of the two branches below, and {@code PistonEvent.Post} just before
 * {@code triggerEvent} returns {@code true}; the injection points here match those positions.
 */
@Mixin(PistonBaseBlock.class)
public class MixinPistonBaseBlock {
    /**
     * {@code level.getRandom()} is read once, after vanilla has finished deciding whether the move may happen at all
     * and immediately before the extend/retract branch is chosen, so it lands where NeoForge fires from.
     */
    @Inject(method = "triggerEvent", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getRandom()Lnet/minecraft/util/RandomSource;",
            shift = At.Shift.AFTER), cancellable = true)
    private void pistonPre(BlockState state, Level level, BlockPos pos, int id, int param, CallbackInfoReturnable<Boolean> cir) {
        if (BlockEvent.PISTON_PRE.invoker().piston(level, pos, state.getValue(DirectionalBlock.FACING), id == 0).isFalse()) {
            cir.setReturnValue(false);
        }
    }
    
    @Inject(method = "triggerEvent", at = @At("RETURN"))
    private void pistonPost(BlockState state, Level level, BlockPos pos, int id, int param, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            BlockEvent.PISTON_POST.invoker().piston(level, pos, state.getValue(DirectionalBlock.FACING), id == 0);
        }
    }
}
