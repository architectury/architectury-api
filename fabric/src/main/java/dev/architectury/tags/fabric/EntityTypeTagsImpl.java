package dev.architectury.tags.fabric;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class EntityTypeTagsImpl {
    public static TagKey<EntityType<?>> implBOSSES() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags.BOSSES;
    }
    public static TagKey<EntityType<?>> implMINECARTS() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags.MINECARTS;
    }
    public static TagKey<EntityType<?>> implBOATS() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags.BOATS;
    }
    public static TagKey<EntityType<?>> implCAPTURING_NOT_SUPPORTED() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags.CAPTURING_NOT_SUPPORTED;
    }
    public static TagKey<EntityType<?>> implTELEPORTING_NOT_SUPPORTED() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags.TELEPORTING_NOT_SUPPORTED;
    }
}