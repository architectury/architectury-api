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

package me.shedaniel.architectury.me.shedaniel.architectury.hooks.forge;

import me.shedaniel.architectury.hooks.ScreenHooks;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;

public class ScreenHooksImpl implements ScreenHooks.Impl {
    @Override
    public <T extends Widget> T addButton(Screen screen, T t) {
        try {
            return (T) ObfuscationReflectionHelper.findMethod(Screen.class, "func_230480_a_", Widget.class).invoke(screen, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public <T extends IGuiEventListener> T addChild(Screen screen, T t) {
        try {
            return (T) ObfuscationReflectionHelper.findMethod(Screen.class, "func_230481_d_", IGuiEventListener.class).invoke(screen, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
