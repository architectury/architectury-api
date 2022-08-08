package dev.architectury.mixin.inject;

import dev.architectury.extensions.injected.InjectedBucketItemExtension;
import net.minecraft.world.item.BucketItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BucketItem.class)
public class MixinBucketItem implements InjectedBucketItemExtension {
}
