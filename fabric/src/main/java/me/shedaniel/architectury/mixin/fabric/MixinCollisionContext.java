package me.shedaniel.architectury.mixin.fabric;

import me.shedaniel.architectury.hooks.fabric.EntityHooksImpl;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CollisionContext.class)
public interface MixinCollisionContext extends EntityHooksImpl.CollisionContextExtension {
}
