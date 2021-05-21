package me.shedaniel.architectury.mixin.fabric;

import me.shedaniel.architectury.event.events.ChunkEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChunkMap.class)
public class MixinChunkMap {
    @Shadow
    @Final
    private ServerLevel level;
    
    @Inject(
            method = "save",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkMap;write(Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/nbt/CompoundTag;)V", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void save(ChunkAccess chunkAccess, CallbackInfoReturnable<Boolean> cir, ChunkPos pos, ChunkStatus chunkStatus, CompoundTag nbt) {
        ChunkEvent.SAVE.invoker().save(chunkAccess, this.level, nbt);
    }
    
}
