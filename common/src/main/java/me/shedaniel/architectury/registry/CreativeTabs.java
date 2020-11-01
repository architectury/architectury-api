package me.shedaniel.architectury.registry;

import me.shedaniel.architectury.ArchitecturyPopulator;
import me.shedaniel.architectury.Populatable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public final class CreativeTabs {
    public CreativeTabs() {}
    
    @Populatable
    private static final Impl IMPL = null;
    
    // I am sorry, fabric wants a resource location instead of the translation key for whatever reason
    public static CreativeModeTab create(ResourceLocation name, Supplier<ItemStack> icon) {
        return IMPL.create(name, icon);
    }
    
    public interface Impl {
        CreativeModeTab create(ResourceLocation name, Supplier<ItemStack> icon);
    }
    
    static {
        ArchitecturyPopulator.populate(CreativeTabs.class);
    }
}
