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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPickItemFromBlockPacket;
import net.minecraft.network.protocol.game.ServerboundPickItemFromEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * NeoForge has no pick-item event, so both halves are bridged here. Fabric exposes them natively as
 * {@code PlayerPickItemEvents}, and these injection points mirror the ones Fabric uses.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListenerImpl {
    @Shadow
    public ServerPlayer player;
    
    @Shadow
    private void tryPickItem(ItemStack stack) {
        throw new AssertionError();
    }
    
    @WrapOperation(method = "handlePickItemFromBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getCloneItemStack(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/LevelReader;ZLnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack pickItemFromBlock(BlockState state, BlockPos pos, LevelReader level, boolean includeData, Player pickingPlayer, Operation<ItemStack> original, @Local(argsOnly = true) ServerboundPickItemFromBlockPacket packet) {
        CompoundEventResult<ItemStack> result = InteractionEvent.PICK_ITEM_FROM_BLOCK.invoker().pick(player, pos, state, packet.includeData());
        if (!result.isPresent()) {
            return original.call(state, pos, level, includeData, pickingPlayer);
        }
        ItemStack stack = result.object();
        if (stack != null && !stack.isEmpty()) {
            tryPickItem(stack);
        }
        
        return ItemStack.EMPTY;
    }
    
    @WrapOperation(method = "handlePickItemFromEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getPickResult()Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack pickItemFromEntity(Entity entity, Operation<ItemStack> original, @Local(argsOnly = true) ServerboundPickItemFromEntityPacket packet) {
        CompoundEventResult<ItemStack> result = InteractionEvent.PICK_ITEM_FROM_ENTITY.invoker().pick(player, entity, packet.includeData());
        if (!result.isPresent()) {
            return original.call(entity);
        }
        ItemStack stack = result.object();
        if (stack != null && !stack.isEmpty()) {
            tryPickItem(stack);
        }
        
        return ItemStack.EMPTY;
    }
}
