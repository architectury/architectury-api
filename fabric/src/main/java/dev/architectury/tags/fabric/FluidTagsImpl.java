package dev.architectury.tags.fabric;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class FluidTagsImpl {
    public static TagKey<Fluid> implLAVA() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags.LAVA;
    }
    public static TagKey<Fluid> implWATER() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags.WATER;
    }
    public static TagKey<Fluid> implMILK() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags.MILK;
    }
    public static TagKey<Fluid> implHONEY() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags.HONEY;
    }
    public static TagKey<Fluid> implHIDDEN_FROM_RECIPE_VIEWERS() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags.HIDDEN_FROM_RECIPE_VIEWERS;
    }
    public static TagKey<Fluid> implGASEOUS() {
        return null;
    }
    public static TagKey<Fluid> implPOTION() {
        return null;
    }
    public static TagKey<Fluid> implSUSPICIOUS_STEW() {
        return null;
    }
    public static TagKey<Fluid> implMUSHROOM_STEW() {
        return null;
    }
    public static TagKey<Fluid> implRABBIT_STEW() {
        return null;
    }
    public static TagKey<Fluid> implBEETROOT_SOUP() {
        return null;
    }
}