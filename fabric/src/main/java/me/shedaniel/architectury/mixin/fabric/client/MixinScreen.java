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

package me.shedaniel.architectury.mixin.fabric.client;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.architectury.event.events.GuiEvent;
import me.shedaniel.architectury.event.events.TooltipEvent;
import me.shedaniel.architectury.event.events.client.ClientChatEvent;
import me.shedaniel.architectury.impl.TooltipEventColorContextImpl;
import me.shedaniel.architectury.impl.TooltipEventPositionContextImpl;
import me.shedaniel.architectury.impl.fabric.ScreenInputDelegate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Screen.class)
public abstract class MixinScreen implements ScreenInputDelegate {
    @Shadow @Final public List<AbstractWidget> buttons;
    @Unique private static ThreadLocal<TooltipEventPositionContextImpl> tooltipPositionContext = ThreadLocal.withInitial(TooltipEventPositionContextImpl::new);
    @Unique private static ThreadLocal<TooltipEventColorContextImpl> tooltipColorContext = ThreadLocal.withInitial(TooltipEventColorContextImpl::new);
    
    @Shadow
    public abstract List<? extends GuiEventListener> children();
    
    @Unique
    private GuiEventListener inputDelegate;
    
    @Override
    public GuiEventListener architectury_delegateInputs() {
        if (inputDelegate == null) {
            inputDelegate = new DelegateScreen((Screen) (Object) this);
        }
        return inputDelegate;
    }
    
    @Inject(method = "init(Lnet/minecraft/client/Minecraft;II)V", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", ordinal = 0),
            cancellable = true)
    private void preInit(Minecraft minecraft, int i, int j, CallbackInfo ci) {
        if (GuiEvent.INIT_PRE.invoker().init((Screen) (Object) this, buttons, (List<GuiEventListener>) children()) == InteractionResult.FAIL) {
            ci.cancel();
        }
    }
    
    @Inject(method = "init(Lnet/minecraft/client/Minecraft;II)V", at = @At(value = "RETURN"))
    private void postInit(Minecraft minecraft, int i, int j, CallbackInfo ci) {
        GuiEvent.INIT_POST.invoker().init((Screen) (Object) this, buttons, (List<GuiEventListener>) children());
    }
    
    @ModifyVariable(method = "sendMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String modifyMessage(String message) {
        InteractionResultHolder<String> process = ClientChatEvent.CLIENT.invoker().process(message);
        if (process.getResult() == InteractionResult.FAIL)
            return "";
        if (process.getObject() != null)
            return process.getObject();
        return message;
    }
    
    @Inject(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;II)V", at = @At("HEAD"), cancellable = true)
    private void renderTooltip(PoseStack poseStack, List<? extends FormattedCharSequence> list, int x, int y, CallbackInfo ci) {
        if (!list.isEmpty()) {
            TooltipEventColorContextImpl colorContext = tooltipColorContext.get();
            colorContext.reset();
            TooltipEventPositionContextImpl positionContext = tooltipPositionContext.get();
            positionContext.reset(x, y);
            if (TooltipEvent.RENDER_VANILLA_PRE.invoker().renderTooltip(poseStack, list, x, y) == InteractionResult.FAIL) {
                ci.cancel();
            } else {
                TooltipEvent.RENDER_MODIFY_COLOR.invoker().renderTooltip(poseStack, x, y, colorContext);
                TooltipEvent.RENDER_MODIFY_POSITION.invoker().renderTooltip(poseStack, positionContext);
            }
        }
    }
    
    @ModifyVariable(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;II)V",
                    at = @At(value = "HEAD"), ordinal = 0)
    private int modifyTooltipX(int original) {
        return tooltipPositionContext.get().getTooltipX();
    }
    
    @ModifyVariable(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;II)V",
                    at = @At(value = "HEAD"), ordinal = 1)
    private int modifyTooltipY(int original) {
        return tooltipPositionContext.get().getTooltipY();
    }
    
    @ModifyConstant(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;II)V", constant = @Constant(intValue = 0xf0100010))
    private int modifyTooltipBackgroundColor(int original) {
        return tooltipColorContext.get().getBackgroundColor();
    }
    
    @ModifyConstant(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;II)V", constant = @Constant(intValue = 0x505000ff))
    private int modifyTooltipOutlineGradientTopColor(int original) {
        return tooltipColorContext.get().getOutlineGradientTopColor();
    }
    
    @ModifyConstant(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;II)V", constant = @Constant(intValue = 0x5028007f))
    private int modifyTooltipOutlineGradientBottomColor(int original) {
        return tooltipColorContext.get().getOutlineGradientBottomColor();
    }
}
