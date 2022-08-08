package dev.architectury.extensions.injected;

import dev.architectury.hooks.fluid.FluidBucketHooks;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;

public interface InjectedBucketItemExtension {
    default Fluid arch$getFluid() {
        return FluidBucketHooks.getFluid((BucketItem) this);
    }
}
