package dev.architectury.mixin.inject;

import dev.architectury.extensions.injected.InjectedFluidExtension;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Fluid.class)
public class MixinFluid implements InjectedFluidExtension {
}
