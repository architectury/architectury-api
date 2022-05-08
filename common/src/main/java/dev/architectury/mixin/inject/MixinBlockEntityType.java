package dev.architectury.mixin.inject;

import dev.architectury.extensions.injected.InjectedBlockEntityTypeExtension;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntityType.class)
public class MixinBlockEntityType implements InjectedBlockEntityTypeExtension {
}
