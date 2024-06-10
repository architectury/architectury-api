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

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.Program;
import com.mojang.datafixers.util.Pair;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.Consumer;

@Mixin(value = GameRenderer.class, priority = 1100)
public abstract class MixinGameRenderer {
    @Shadow
    @Final
    private Minecraft minecraft;
    
    @Inject(method = "render(Lnet/minecraft/client/DeltaTracker;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;renderWithTooltip(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
                    ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    public void renderScreenPre(DeltaTracker tickDelta, boolean tick, CallbackInfo ci, boolean isGameLoadFinished, int mouseX, int mouseY, Window window, Matrix4f matrix, Matrix4fStack matrices, GuiGraphics graphics) {
        if (ClientGuiEvent.RENDER_PRE.invoker().render(minecraft.screen, graphics, mouseX, mouseY, tickDelta.getGameTimeDeltaTicks()).isFalse()) {
            ci.cancel();
        }
    }
    
    @Inject(method = "render(Lnet/minecraft/client/DeltaTracker;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;renderWithTooltip(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
                    shift = At.Shift.AFTER, ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void renderScreenPost(DeltaTracker tickDelta, boolean tick, CallbackInfo ci, boolean isGameLoadFinished, int mouseX, int mouseY, Window window, Matrix4f matrix, Matrix4fStack matrices, GuiGraphics graphics) {
        ClientGuiEvent.RENDER_POST.invoker().render(minecraft.screen, graphics, mouseX, mouseY, tickDelta.getGameTimeDeltaTicks());
    }
    
    @Inject(method = "reloadShaders",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void reloadShaders(ResourceProvider provider, CallbackInfo ci, List<Program> programs, List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaders) {
        ClientReloadShadersEvent.EVENT.invoker().reload(provider, (shader, callback) -> {
            shaders.add(Pair.of(shader, callback));
        });
    }
}