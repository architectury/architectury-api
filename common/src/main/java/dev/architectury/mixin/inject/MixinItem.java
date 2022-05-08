package dev.architectury.mixin.inject;

import dev.architectury.extensions.injected.InjectedItemExtension;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class MixinItem implements InjectedItemExtension {
}
