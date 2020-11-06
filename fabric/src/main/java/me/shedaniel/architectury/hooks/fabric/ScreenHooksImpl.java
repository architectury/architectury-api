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

package me.shedaniel.architectury.hooks.fabric;

import me.shedaniel.architectury.hooks.ScreenHooks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;

@Environment(EnvType.CLIENT)
public class ScreenHooksImpl implements ScreenHooks.Impl {
    @Override
    public <T extends AbstractWidget> T addButton(Screen screen, T widget) {
        return screen.addButton(widget);
    }
    
    @Override
    public <T extends GuiEventListener> T addChild(Screen screen, T listener) {
        return screen.addWidget(listener);
    }
}
