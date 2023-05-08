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

package dev.architectury.mixin.fabric.client;

import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.impl.TooltipEventColorContextImpl;
import dev.architectury.impl.TooltipEventPositionContextImpl;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiGraphics.class)
public abstract class MixinGuiGraphics {
    @Unique
    private static ThreadLocal<TooltipEventPositionContextImpl> tooltipPositionContext = ThreadLocal.withInitial(TooltipEventPositionContextImpl::new);
    @Unique
    private static ThreadLocal<TooltipEventColorContextImpl> tooltipColorContext = ThreadLocal.withInitial(TooltipEventColorContextImpl::new);
    
    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V", at = @At("HEAD"))
    private void preRenderTooltipItem(Font font, ItemStack stack, int x, int y, CallbackInfo ci) {
        ClientTooltipEvent.additionalContexts().setItem(stack);
    }
    
    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V", at = @At("RETURN"))
    private void postRenderTooltipItem(Font font, ItemStack stack, int x, int y, CallbackInfo ci) {
        ClientTooltipEvent.additionalContexts().setItem(null);
    }
    
    @Inject(method = "renderTooltipInternal", at = @At("HEAD"), cancellable = true)
    private void renderTooltip(Font font, List<? extends ClientTooltipComponent> list, int x, int y, ClientTooltipPositioner positioner, CallbackInfo ci) {
        if (!list.isEmpty()) {
            var colorContext = tooltipColorContext.get();
            colorContext.reset();
            var positionContext = tooltipPositionContext.get();
            positionContext.reset(x, y);
            if (ClientTooltipEvent.RENDER_PRE.invoker().renderTooltip((GuiGraphics) (Object) this, list, x, y).isFalse()) {
                ci.cancel();
            } else {
                // ClientTooltipEvent.RENDER_MODIFY_COLOR.invoker().renderTooltip((GuiGraphics) (Object) this, x, y, colorContext);
                ClientTooltipEvent.RENDER_MODIFY_POSITION.invoker().renderTooltip((GuiGraphics) (Object) this, positionContext);
            }
        }
    }
    
    @ModifyVariable(method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V",
            at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    private int modifyTooltipX(int original) {
        return tooltipPositionContext.get().getTooltipX();
    }
    
    @ModifyVariable(method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V",
            at = @At(value = "HEAD"), ordinal = 1, argsOnly = true)
    private int modifyTooltipY(int original) {
        return tooltipPositionContext.get().getTooltipY();
    }

//    @ModifyConstant(method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", constant = @Constant(intValue = 0xf0100010))
//    private int modifyTooltipBackgroundColor(int original) {
//        return tooltipColorContext.get().getBackgroundColor();
//    }
//    
//    @ModifyConstant(method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", constant = @Constant(intValue = 0x505000ff))
//    private int modifyTooltipOutlineGradientTopColor(int original) {
//        return tooltipColorContext.get().getOutlineGradientTopColor();
//    }
//    
//    @ModifyConstant(method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", constant = @Constant(intValue = 0x5028007f))
//    private int modifyTooltipOutlineGradientBottomColor(int original) {
//        return tooltipColorContext.get().getOutlineGradientBottomColor();
//    }
}
