package me.shedaniel.architectury.mixin.fabric;

import me.shedaniel.architectury.hooks.fabric.EntityHooksImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityCollisionContext.class)
public abstract class MixinEntityCollisionContext implements CollisionContext, EntityHooksImpl.CollisionContextExtension {
    
    private Entity arch$entity = null;
    
    @Inject(method = "<init>(Lnet/minecraft/world/entity/Entity;)V",
            at = @At("RETURN"))
    public void saveEntity(Entity entity, CallbackInfo ci) {
        arch$entity = entity;
    }
    
    @Nullable
    @Override
    public Entity getEntity() {
        return arch$entity;
    }
    
}
