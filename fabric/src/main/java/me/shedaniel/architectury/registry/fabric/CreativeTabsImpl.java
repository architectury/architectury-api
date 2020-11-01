package me.shedaniel.architectury.registry.fabric;

import me.shedaniel.architectury.registry.CreativeTabs;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class CreativeTabsImpl implements CreativeTabs.Impl {
    @Override
    public CreativeModeTab create(ResourceLocation name, Supplier<ItemStack> icon) {
        return FabricItemGroupBuilder.build(name, icon);
    }
}
