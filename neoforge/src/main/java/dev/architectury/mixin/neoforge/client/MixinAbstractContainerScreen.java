package dev.architectury.mixin.neoforge.client;

import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class MixinAbstractContainerScreen extends Screen {
    protected MixinAbstractContainerScreen(Component component) {
        super(component);
    }
    
    @Inject(method = "extractContents",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
                    ordinal = 0, shift = At.Shift.AFTER))
    public void renderBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ClientGuiEvent.RENDER_CONTAINER_BACKGROUND.invoker().render((AbstractContainerScreen<?>) (Object) this, graphics, mouseX, mouseY, delta);
    }
}
