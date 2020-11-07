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

package me.shedaniel.architectury.hooks;

import me.shedaniel.architectury.ArchitecturyPopulator;
import me.shedaniel.architectury.Populatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

@Environment(EnvType.CLIENT)
public final class ScreenHooks {
    private ScreenHooks() {}
    
    @Populatable
    private static final Impl IMPL = null;
    
    public static List<AbstractWidget> getButtons(Screen screen) {
        return IMPL.getButtons(screen);
    }
    
    public static <T extends AbstractWidget> T addButton(Screen screen, T widget) {
        return IMPL.addButton(screen, widget);
    }
    
    public static <T extends GuiEventListener> T addChild(Screen screen, T listener) {
        return IMPL.addChild(screen, listener);
    }
    
    public interface Impl {
        List<AbstractWidget> getButtons(Screen screen);
        
        <T extends AbstractWidget> T addButton(Screen screen, T widget);
        
        <T extends GuiEventListener> T addChild(Screen screen, T listener);
    }
    
    static {
        ArchitecturyPopulator.populate(ScreenHooks.class);
    }
}
