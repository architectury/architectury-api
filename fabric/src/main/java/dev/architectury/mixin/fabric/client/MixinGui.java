package dev.architectury.mixin.fabric.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinGui {
    @Unique
    private ThreadLocal<Boolean> setScreenCancelled = new ThreadLocal<>();

    @ModifyVariable(
            method = "setScreen",
            at = @At(value = "FIELD",
                    opcode = Opcodes.PUTFIELD,
                    target = "Lnet/minecraft/client/gui/Gui;screen:Lnet/minecraft/client/gui/screens/Screen;",
                    shift = At.Shift.BY,
                    by = -1),
            argsOnly = true
    )
    public Screen modifyScreen(Screen screen) {
        var old = screen;
        var event = ClientGuiEvent.SET_SCREEN.invoker().modifyScreen(screen);
        if (event.isPresent()) {
            if (event.isFalse()) {
                setScreenCancelled.set(true);
                return old;
            } else {
                screen = event.object();
                if (old != null && screen != old) {
                    old.removed();
                }
            }
        }
        setScreenCancelled.set(false);
        return screen;
    }

    @Inject(
            method = "setScreen",
            at = @At(value = "FIELD",
                    opcode = Opcodes.PUTFIELD,
                    target = "Lnet/minecraft/client/gui/Gui;screen:Lnet/minecraft/client/gui/screens/Screen;",
                    shift = At.Shift.BY,
                    by = -1),
            cancellable = true
    )
    public void cancelSetScreen(@Nullable Screen screen, CallbackInfo ci) {
        if (setScreenCancelled.get()) {
            ci.cancel();
            setScreenCancelled.set(false);
        }
    }
    
    @WrapOperation(method = "extractRenderState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;extractRenderStateWithTooltipAndSubtitles(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
                    ordinal = 0))
    public void wrapRenderScreen(Screen screen, GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta, Operation<Void> original) {
        if (ClientGuiEvent.RENDER_PRE.invoker().render(screen, graphics, mouseX, mouseY, delta).isFalse()) {
            return;
        }
        original.call(screen, graphics, mouseX, mouseY, delta);
        ClientGuiEvent.RENDER_POST.invoker().render(screen, graphics, mouseX, mouseY, delta);
    }
}
