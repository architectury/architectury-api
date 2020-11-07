package me.shedaniel.architectury.mixin.fabric;

import me.shedaniel.architectury.event.events.EntityEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void hurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof Player) return;
        if (EntityEvent.LIVING_ATTACK.invoker().attack((LivingEntity) (Object) this, damageSource, f) == InteractionResult.FAIL) {
            cir.setReturnValue(false);
        }
    }
}
