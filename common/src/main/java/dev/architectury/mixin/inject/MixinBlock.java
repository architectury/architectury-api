package dev.architectury.mixin.inject;

import dev.architectury.extensions.injected.InjectedBlockExtension;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public class MixinBlock implements InjectedBlockExtension {
}
