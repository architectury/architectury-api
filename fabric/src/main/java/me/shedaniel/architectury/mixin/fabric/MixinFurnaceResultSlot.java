package me.shedaniel.architectury.mixin.fabric;

import me.shedaniel.architectury.event.events.PlayerEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FurnaceResultSlot.class)
public class MixinFurnaceResultSlot {
    @Shadow @Final private Player player;
    
    @Inject(method = "checkTakeAchievements", at = @At("RETURN"))
    private void checkTakeAchievements(ItemStack itemStack, CallbackInfo ci) {
        PlayerEvent.SMELT_ITEM.invoker().smelt(player, itemStack);
    }
}
