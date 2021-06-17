package dev.architectury.mixin.fabric;

import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TamableAnimal.class)
public class TamableAnimalInvoker {
    
    @Inject(method = "tame", at = @At(value = "HEAD"), cancellable = true)
    private void tame(Player player, CallbackInfo ci) {
        if (EntityEvent.ANIMAL_TAME.invoker().onTame((Animal) (Object) this, player).isFalse()) {
            ci.cancel();
        }
    }
}
