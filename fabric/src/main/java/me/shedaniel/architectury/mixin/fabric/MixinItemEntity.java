package me.shedaniel.architectury.mixin.fabric;

import me.shedaniel.architectury.event.events.PlayerEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity {
    @Shadow
    public abstract ItemStack getItem();
    
    @Inject(method = "playerTouch",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getCount()I"), cancellable = true)
    private void prePickup(Player player, CallbackInfo ci) {
        InteractionResult canPickUp = PlayerEvent.PICKUP_ITEM_PRE.invoker().canPickup(player, (ItemEntity) (Object) this, getItem());
        if (canPickUp == InteractionResult.FAIL) {
            ci.cancel();
        }
    }
    
    @Inject(method = "playerTouch",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;take(Lnet/minecraft/world/entity/Entity;I)V"))
    private void pickup(Player player, CallbackInfo ci) {
        PlayerEvent.PICKUP_ITEM_POST.invoker().pickup(player, (ItemEntity) (Object) this, getItem());
    }
}