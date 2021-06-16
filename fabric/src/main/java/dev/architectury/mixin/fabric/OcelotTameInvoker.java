package dev.architectury.mixin.fabric;

import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Ocelot.class)
public class OcelotTameInvoker {
    
    @Inject(method = "mobInteract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/Ocelot;setTrusting(Z)V"
            ), cancellable = true)
    private void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (EntityEvent.ANIMAL_TAME.invoker().onTame((Animal) (Object) this, player) == InteractionResult.FAIL) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            cir.cancel();
        }
    }
}
