package dev.architectury.tags.fabric;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class EntityTypeTagsImpl {
    public static TagKey<EntityType<?>> impl_BOSSES() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags.BOSSES;
    }
    public static TagKey<EntityType<?>> impl_MINECARTS() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags.MINECARTS;
    }
    public static TagKey<EntityType<?>> impl_BOATS() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags.BOATS;
    }
    public static TagKey<EntityType<?>> impl_CAPTURING_NOT_SUPPORTED() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags.CAPTURING_NOT_SUPPORTED;
    }
    public static TagKey<EntityType<?>> impl_TELEPORTING_NOT_SUPPORTED() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags.TELEPORTING_NOT_SUPPORTED;
    }
}