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
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Matrix4f;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceProvider;
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
    
    @Inject(method = "render(FJZ)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V",
                    ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    public void renderScreenPre(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int mouseX, int mouseY, Window window, Matrix4f matrix, PoseStack matrices, PoseStack matrices2) {
        if (ClientGuiEvent.RENDER_PRE.invoker().render(minecraft.screen, matrices2, mouseX, mouseY, minecraft.getDeltaFrameTime()).isFalse()) {
            ci.cancel();
        }
    }
    
    @Inject(method = "render(FJZ)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V",
                    shift = At.Shift.AFTER, ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void renderScreenPost(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int mouseX, int mouseY, Window window, Matrix4f matrix, PoseStack matrices, PoseStack matrices2) {
        ClientGuiEvent.RENDER_POST.invoker().render(minecraft.screen, matrices2, mouseX, mouseY, minecraft.getDeltaFrameTime());
    }
    
    @Inject(method = "reloadShaders",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void reloadShaders(ResourceProvider provider, CallbackInfo ci, List<Program> programs, List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaders) {
        ClientReloadShadersEvent.EVENT.invoker().reload(provider, (shader, callback) -> {
            shaders.add(Pair.of(shader, callback));
        });
    }
}