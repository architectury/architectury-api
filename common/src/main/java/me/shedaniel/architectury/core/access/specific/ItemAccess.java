package me.shedaniel.architectury.core.access.specific;

import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

@FunctionalInterface
public interface ItemAccess<T> {
    T getByItem(ItemStack stack);
    
    default ItemAccess<T> filterItem(Predicate<ItemStack> predicate) {
        return stack -> {
            if (predicate.test(stack)) {
                return this.getByItem(stack);
            }
            return null;
        };
    }
}
