/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.shedaniel.architectury.mixin.fabric.client;

import me.shedaniel.architectury.event.events.GuiEvent;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugScreenOverlay.class)
public class MixinDebugScreenOverlay {
    @Inject(method = "getGameInformation", at = @At("RETURN"))
    private void getLeftTexts(CallbackInfoReturnable<List<String>> cir) {
        GuiEvent.DEBUG_TEXT_LEFT.invoker().gatherText(cir.getReturnValue());
    }
    
    @Inject(method = "getSystemInformation", at = @At("RETURN"))
    private void getRightTexts(CallbackInfoReturnable<List<String>> cir) {
        GuiEvent.DEBUG_TEXT_RIGHT.invoker().gatherText(cir.getReturnValue());
    }
}
