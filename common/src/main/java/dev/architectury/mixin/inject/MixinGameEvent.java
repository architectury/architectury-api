package dev.architectury.mixin.inject;

import dev.architectury.extensions.injected.InjectedGameEventExtension;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GameEvent.class)
public class MixinGameEvent implements InjectedGameEventExtension {
}
