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

package dev.architectury.mixin.forge;

import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * NeoForge's {@code UseItemOnBlockEvent} only covers the block phase that takes the held item into account, so the
 * empty-handed phase is bridged here. Fabric exposes it natively as {@code BlockEvents.USE_WITHOUT_ITEM}.
 */
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class MixinBlockStateBase {
    @Shadow
    protected abstract BlockState asState();
    
    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    private void useWithoutItem(Level level, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        InteractionResult result = InteractionEvent.USE_BLOCK_WITHOUT_ITEM.invoker().useWithoutItem(level, player, asState(), hitResult);
        if (result != InteractionResult.PASS) {
            cir.setReturnValue(result);
        }
    }
}
