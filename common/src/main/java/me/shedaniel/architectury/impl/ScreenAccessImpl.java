package me.shedaniel.architectury.impl;

import me.shedaniel.architectury.hooks.screen.ScreenAccess;
import me.shedaniel.architectury.hooks.screen.ScreenHooks;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

public class ScreenAccessImpl implements ScreenAccess {
    private Screen screen;
    
    public ScreenAccessImpl(Screen screen) {
        this.screen = screen;
    }
    
    public void setScreen(Screen screen) {
        this.screen = screen;
    }
    
    @Override
    public Screen getScreen() {
        return screen;
    }
    
    @Override
    public List<NarratableEntry> getNarratables() {
        return ScreenHooks.getNarratables(screen);
    }
    
    @Override
    public List<Widget> getRenderables() {
        return ScreenHooks.getRenderables(screen);
    }
    
    @Override
    public <T extends AbstractWidget & Widget & NarratableEntry> T addRenderableWidget(T widget) {
        return ScreenHooks.addRenderableWidget(screen, widget);
    }
    
    @Override
    public <T extends Widget> T addRenderableOnly(T listener) {
        return ScreenHooks.addRenderableOnly(screen, listener);
    }
    
    @Override
    public <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
        return ScreenHooks.addWidget(screen, listener);
    }
}
