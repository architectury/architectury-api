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

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.hooks.client.screen.ScreenAccess;
import dev.architectury.impl.ScreenAccessImpl;
import dev.architectury.impl.fabric.ScreenInputDelegate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Screen.class)
public abstract class MixinScreen implements ScreenInputDelegate {
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
    
    @Inject(method = "rebuildWidgets", at = @At(value = "HEAD"), cancellable = true)
    private void preInit(CallbackInfo ci) {
        if (ClientGuiEvent.INIT_PRE.invoker().init((Screen) (Object) this, getAccess()).isFalse()) {
            ci.cancel();
        }
    }
    
    @Inject(method = "init(Lnet/minecraft/client/Minecraft;II)V", at = @At(value = "RETURN"))
    private void postInit(Minecraft minecraft, int i, int j, CallbackInfo ci) {
        ClientGuiEvent.INIT_POST.invoker().init((Screen) (Object) this, getAccess());
    }
}
