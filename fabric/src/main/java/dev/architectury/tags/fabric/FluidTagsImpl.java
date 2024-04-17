package dev.architectury.tags.fabric;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class FluidTagsImpl {
    public static TagKey<Fluid> impl_LAVA() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags.LAVA;
    }
    public static TagKey<Fluid> impl_WATER() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags.WATER;
    }
    public static TagKey<Fluid> impl_MILK() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags.MILK;
    }
    public static TagKey<Fluid> impl_HONEY() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags.HONEY;
    }
    public static TagKey<Fluid> impl_HIDDEN_FROM_RECIPE_VIEWERS() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags.HIDDEN_FROM_RECIPE_VIEWERS;
    }
    public static TagKey<Fluid> impl_GASEOUS() {
        return null;
    }
    public static TagKey<Fluid> impl_POTION() {
        return null;
    }
    public static TagKey<Fluid> impl_SUSPICIOUS_STEW() {
        return null;
    }
    public static TagKey<Fluid> impl_MUSHROOM_STEW() {
        return null;
    }
    public static TagKey<Fluid> impl_RABBIT_STEW() {
        return null;
    }
    public static TagKey<Fluid> impl_BEETROOT_SOUP() {
        return null;
    }
}