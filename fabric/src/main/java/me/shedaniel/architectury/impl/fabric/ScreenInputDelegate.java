package me.shedaniel.architectury.impl.fabric;

import me.shedaniel.architectury.event.events.client.ClientScreenInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;

public interface ScreenInputDelegate {
    GuiEventListener architectury_delegateInputs();
    
    class DelegateScreen extends Screen {
        private Screen parent;
        
        public DelegateScreen(Screen parent) {
            super(TextComponent.EMPTY);
            this.parent = parent;
        }
        
        @Override
        public boolean mouseDragged(double d, double e, int i, double f, double g) {
            if (ClientScreenInputEvent.MOUSE_DRAGGED_PRE.invoker().mouseDragged(Minecraft.getInstance(), parent, d, e, i, f, g) != InteractionResult.PASS)
                return true;
            if (parent.mouseDragged(d, e, i, f, g))
                return true;
            return ClientScreenInputEvent.MOUSE_DRAGGED_PRE.invoker().mouseDragged(Minecraft.getInstance(), parent, d, e, i, f, g) != InteractionResult.PASS;
        }
        
        @Override
        public boolean charTyped(char c, int i) {
            if (ClientScreenInputEvent.CHAR_TYPED_PRE.invoker().charTyped(Minecraft.getInstance(), parent, c, i) != InteractionResult.PASS)
                return true;
            if (parent.charTyped(c, i))
                return true;
            return ClientScreenInputEvent.CHAR_TYPED_POST.invoker().charTyped(Minecraft.getInstance(), parent, c, i) != InteractionResult.PASS;
        }
    }
}
