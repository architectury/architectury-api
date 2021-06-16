package dev.architectury.mixin.fabric;

import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RunAroundLikeCrazyGoal.class)
public class HorseTameInvoker {
    
    @Shadow
    @Final
    private AbstractHorse horse;
    
    @Inject(method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;tameWithName(Lnet/minecraft/world/entity/player/Player;)Z"
            ), cancellable = true
    )
    private void tick(CallbackInfo ci) {
        if (EntityEvent.ANIMAL_TAME.invoker().onTame(this.horse, (Player) this.horse.getPassengers().get(0)) == InteractionResult.FAIL) {
            ci.cancel();
        }
    }
}
