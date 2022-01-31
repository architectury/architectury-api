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

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.event.events.client.ClientChatEvent;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.hooks.client.screen.ScreenAccess;
import dev.architectury.impl.ScreenAccessImpl;
import dev.architectury.impl.TooltipEventColorContextImpl;
import dev.architectury.impl.TooltipEventPositionContextImpl;
import dev.architectury.impl.fabric.ScreenInputDelegate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Screen.class)
public abstract class MixinScreen implements ScreenInputDelegate {
    @Unique
    private static ThreadLocal<TooltipEventPositionContextImpl> tooltipPositionContext = ThreadLocal.withInitial(TooltipEventPositionContextImpl::new);
    @Unique
    private static ThreadLocal<TooltipEventColorContextImpl> tooltipColorContext = ThreadLocal.withInitial(TooltipEventColorContextImpl::new);
    @Unique
    private ScreenAccessImpl access;
    
    @Shadow
    public abstract List<? extends GuiEventListener> children();
    
    @Unique
    private Screen inputDelegate;
    
    @Unique
    private ScreenAccess getAccess() {
        if (access == null) {
            return access = new ScreenAccessImpl((Screen) (Object) this);
        }
        
        access.setScreen((Screen) (Object) this); // Preventive set
        return access;
    }
    
    @Override
    public Screen architectury_delegateInputs() {
        if (inputDelegate == null) {
            inputDelegate = new DelegateScreen((Screen) (Object) this);
        }
        return inputDelegate;
    }
    
    @Inject(method = "init(Lnet/minecraft/client/Minecraft;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;clearWidgets()V", ordinal = 0),
            cancellable = true)
    private void preInit(Minecraft minecraft, int i, int j, CallbackInfo ci) {
        if (ClientGuiEvent.INIT_PRE.invoker().init((Screen) (Object) this, getAccess()).isFalse()) {
            ci.cancel();
        }
    }
    
    @Inject(method = "init(Lnet/minecraft/client/Minecraft;II)V", at = @At(value = "RETURN"))
    private void postInit(Minecraft minecraft, int i, int j, CallbackInfo ci) {
        ClientGuiEvent.INIT_POST.invoker().init((Screen) (Object) this, getAccess());
    }
    
    @ModifyVariable(method = "sendMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String modifyMessage(String message) {
        var process = ClientChatEvent.PROCESS.invoker().process(message);
        if (process.isPresent()) {
            if (process.isFalse())
                return "";
            if (process.object() != null)
                return process.object();
        }
        return message;
    }
    
    @Inject(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemStack;II)V", at = @At("HEAD"))
    private void preRenderTooltipItem(PoseStack poseStack, ItemStack stack, int x, int y, CallbackInfo ci) {
        ClientTooltipEvent.additionalContexts().setItem(stack);
    }
    
    @Inject(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemStack;II)V", at = @At("RETURN"))
    private void postRenderTooltipItem(PoseStack poseStack, ItemStack stack, int x, int y, CallbackInfo ci) {
        ClientTooltipEvent.additionalContexts().setItem(null);
    }
    
    @Inject(method = "renderTooltipInternal", at = @At("HEAD"), cancellable = true)
    private void renderTooltip(PoseStack poseStack, List<? extends ClientTooltipComponent> list, int x, int y, CallbackInfo ci) {
        if (!list.isEmpty()) {
            var colorContext = tooltipColorContext.get();
            colorContext.reset();
            var positionContext = tooltipPositionContext.get();
            positionContext.reset(x, y);
            if (ClientTooltipEvent.RENDER_PRE.invoker().renderTooltip(poseStack, list, x, y).isFalse()) {
                ci.cancel();
            } else {
                ClientTooltipEvent.RENDER_MODIFY_COLOR.invoker().renderTooltip(poseStack, x, y, colorContext);
                ClientTooltipEvent.RENDER_MODIFY_POSITION.invoker().renderTooltip(poseStack, positionContext);
            }
        }
    }
    
    @ModifyVariable(method = "renderTooltipInternal",
            at = @At(value = "HEAD"), ordinal = 0)
    private int modifyTooltipX(int original) {
        return tooltipPositionContext.get().getTooltipX();
    }
    
    @ModifyVariable(method = "renderTooltipInternal",
            at = @At(value = "HEAD"), ordinal = 1)
    private int modifyTooltipY(int original) {
        return tooltipPositionContext.get().getTooltipY();
    }
    
    @ModifyConstant(method = "renderTooltipInternal", constant = @Constant(intValue = 0xf0100010))
    private int modifyTooltipBackgroundColor(int original) {
        return tooltipColorContext.get().getBackgroundColor();
    }
    
    @ModifyConstant(method = "renderTooltipInternal", constant = @Constant(intValue = 0x505000ff))
    private int modifyTooltipOutlineGradientTopColor(int original) {
        return tooltipColorContext.get().getOutlineGradientTopColor();
    }
    
    @ModifyConstant(method = "renderTooltipInternal", constant = @Constant(intValue = 0x5028007f))
    private int modifyTooltipOutlineGradientBottomColor(int original) {
        return tooltipColorContext.get().getOutlineGradientBottomColor();
    }
}
