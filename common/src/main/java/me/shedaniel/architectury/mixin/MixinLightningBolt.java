package me.shedaniel.architectury.mixin;

import me.shedaniel.architectury.event.events.LightningEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(LightningBolt.class)
public abstract class MixinLightningBolt extends Entity {
    
    public MixinLightningBolt(EntityType<?> type, Level level) {
        super(type, level);
        throw new IllegalStateException();
    }
    
    @Inject(method = "tick", at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",
            ordinal = 0,
            shift = At.Shift.BY,
            by = 1
    ), locals = LocalCapture.CAPTURE_FAILHARD)
    public void handleLightning(CallbackInfo ci, double d0, List<Entity> list) {
        if (this.removed || this.level.isClientSide) {
            return;
        }
        
        LightningEvent.STRIKE.invoker().onStrike((LightningBolt) (Object) this, this.level, this.position(), list);
    }
    
}
