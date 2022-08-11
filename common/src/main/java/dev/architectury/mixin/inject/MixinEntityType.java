package dev.architectury.mixin.inject;

import dev.architectury.extensions.injected.InjectedEntityTypeExtension;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityType.class)
public class MixinEntityType implements InjectedEntityTypeExtension {
}
