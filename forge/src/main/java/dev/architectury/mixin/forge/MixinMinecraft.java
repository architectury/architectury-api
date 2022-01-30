package dev.architectury.mixin.forge;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// adopted from fabric
@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;gameThread:Ljava/lang/Thread;", shift = At.Shift.AFTER, ordinal = 0), method = "run")
    private void onStart(CallbackInfo ci) {
        ClientLifecycleEvent.CLIENT_STARTED.invoker().stateChanged((Minecraft) (Object) this);
    }
    
    // using {1} rather than Log4j directly for 1.18.2+ support
    @Inject(at = @At(value = "INVOKE", target = "{1}(Ljava/lang/String;)V" /* Logger.info */, shift = At.Shift.AFTER, remap = false), method = "destroy")
    private void onStopping(CallbackInfo ci) {
        ClientLifecycleEvent.CLIENT_STOPPING.invoker().stateChanged((Minecraft) (Object) this);
    }
}
