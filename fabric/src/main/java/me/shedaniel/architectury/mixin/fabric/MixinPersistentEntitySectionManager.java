package me.shedaniel.architectury.mixin.fabric;

import me.shedaniel.architectury.event.events.EntityEvent;
import me.shedaniel.architectury.hooks.fabric.PersistentEntitySectionManagerHooks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.ref.WeakReference;

@Mixin(PersistentEntitySectionManager.class)
public class MixinPersistentEntitySectionManager<T extends EntityAccess> implements PersistentEntitySectionManagerHooks {
    @Unique
    private WeakReference<ServerLevel> levelRef;
    
    @Override
    public void architectury_attachLevel(ServerLevel level) {
        levelRef = new WeakReference<>(level);
    }
    
    @Inject(method = "addEntity", at = @At(value = "INVOKE",
                                           target = "Lnet/minecraft/world/level/entity/EntitySectionStorage;entityPosToSectionKey(Lnet/minecraft/core/BlockPos;)J"),
            cancellable = true)
    private void addEntity(T entityAccess, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        // TODO: Check if other classes implements EntityAccess as well
        if (entityAccess instanceof Entity && levelRef != null) {
            ServerLevel level = levelRef.get();
            levelRef = null;
            
            if (level != null) {
                if (EntityEvent.ADD.invoker().add((Entity) entityAccess, level) == InteractionResult.FAIL) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
