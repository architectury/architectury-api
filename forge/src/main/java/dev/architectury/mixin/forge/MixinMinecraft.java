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

import dev.architectury.event.events.client.ClientLifecycleEvent;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// adopted from fabric
@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;gameThread:Ljava/lang/Thread;", shift = At.Shift.AFTER, ordinal = 0), method = "run")
    private void onStart(CallbackInfo ci) {
        ClientLifecycleEvent.CLIENT_STARTED.invoker().stateChanged((Minecraft) (Object) this);
    }
    
    // using {1} rather than Log4j directly for 1.18.2+ support
    @Inject(at = @At(value = "INVOKE", target = "{1}(Ljava/lang/String;)V" /* Logger.info */, shift = At.Shift.AFTER, remap = false), method = "destroy")
    private void onStopping(CallbackInfo ci) {
        ClientLifecycleEvent.CLIENT_STOPPING.invoker().stateChanged((Minecraft) (Object) this);
    }
}
