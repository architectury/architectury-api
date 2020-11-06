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

import me.shedaniel.architectury.event.events.ChatEvent;
import me.shedaniel.architectury.event.events.GuiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Screen.class)
public abstract class MixinScreen {
    @Shadow @Final protected List<AbstractWidget> buttons;
    
    @Shadow
    public abstract List<? extends GuiEventListener> children();
    
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
        InteractionResultHolder<String> process = ChatEvent.CLIENT.invoker().process(message);
        if (process.getResult() == InteractionResult.FAIL)
            return "";
        if (process.getObject() != null)
            return process.getObject();
        return message;
    }
}
