package me.shedaniel.architectury.hooks.screen;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

public interface ScreenAccess {
    Screen getScreen();
    
    List<NarratableEntry> getNarratables();
    
    List<Widget> getRenderables();
    
    <T extends AbstractWidget & Widget & NarratableEntry> T addRenderableWidget(T widget);
    
    <T extends Widget> T addRenderableOnly(T listener);
    
    <T extends GuiEventListener & NarratableEntry> T addWidget(T listener);
}
