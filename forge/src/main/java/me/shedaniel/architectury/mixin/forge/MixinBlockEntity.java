/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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

import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public abstract class MixinBlockEntity {
    @Inject(method = "getUpdatePacket", at = @At("HEAD"), cancellable = true)
    public void getUpdatePacket(CallbackInfoReturnable<ClientboundBlockEntityDataPacket> cir) {
        if (this instanceof BlockEntityExtension) {
            BlockEntityExtension entity = (BlockEntityExtension) this;
            BlockEntity be = (BlockEntity) entity;
            cir.setReturnValue(new ClientboundBlockEntityDataPacket(be.getBlockPos(), 10, be.getUpdateTag()));
            cir.cancel();
        }
    }
    
    @Inject(method = "getUpdateTag", at = @At("HEAD"), cancellable = true)
    public void getUpdateTag(CallbackInfoReturnable<CompoundTag> cir) {
        if (this instanceof BlockEntityExtension) {
            BlockEntityExtension entity = (BlockEntityExtension) this;
            BlockEntity be = (BlockEntity) entity;
            cir.setReturnValue(entity.saveClientData(entity.getSuperUpdateTag()));
            cir.cancel();
        }
    }
}
