package dev.architectury.tags.forge;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class EntityTypeTagsImpl {
    public static TagKey<EntityType<?>> implBOSSES() {
        return net.neoforged.neoforge.common.Tags.EntityTypes.BOSSES;
    }
    public static TagKey<EntityType<?>> implMINECARTS() {
        return net.neoforged.neoforge.common.Tags.EntityTypes.MINECARTS;
    }
    public static TagKey<EntityType<?>> implBOATS() {
        return net.neoforged.neoforge.common.Tags.EntityTypes.BOATS;
    }
    public static TagKey<EntityType<?>> implCAPTURING_NOT_SUPPORTED() {
        return net.neoforged.neoforge.common.Tags.EntityTypes.CAPTURING_NOT_SUPPORTED;
    }
    public static TagKey<EntityType<?>> implTELEPORTING_NOT_SUPPORTED() {
        return net.neoforged.neoforge.common.Tags.EntityTypes.TELEPORTING_NOT_SUPPORTED;
    }
}