package me.shedaniel.architectury.registry.forge;

import me.shedaniel.architectury.registry.CreativeTabs;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class CreativeTabsImpl implements CreativeTabs.Impl {
    @Override
    public ItemGroup create(ResourceLocation resourceLocation, Supplier<ItemStack> supplier) {
        return new ItemGroup(String.format("%s.%s", resourceLocation.getNamespace(), resourceLocation.getPath())) {
            @Override
            @Nonnull
            public ItemStack makeIcon() {
                return supplier.get();
            }
        };
    }
}
