package dev.architectury.hooks.item.fabric;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

import java.util.function.Supplier;

public class FoodPropertiesHooksImpl {
    public static void effect(FoodProperties.Builder builder,
                              Supplier<? extends MobEffectInstance> effectSupplier, float chance) {
        // Fabric doesn't have deferred registration, so the mob effect should always be available anyway
        builder.effect(effectSupplier.get(), chance);
    }
}
