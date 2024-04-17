package dev.architectury.tags.forge;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class EntityTypeTagsImpl {
    public static TagKey<EntityType<?>> impl_BOSSES() {
        return net.neoforged.neoforge.common.Tags.EntityTypes.BOSSES;
    }
    public static TagKey<EntityType<?>> impl_MINECARTS() {
        return net.neoforged.neoforge.common.Tags.EntityTypes.MINECARTS;
    }
    public static TagKey<EntityType<?>> impl_BOATS() {
        return net.neoforged.neoforge.common.Tags.EntityTypes.BOATS;
    }
    public static TagKey<EntityType<?>> impl_CAPTURING_NOT_SUPPORTED() {
        return net.neoforged.neoforge.common.Tags.EntityTypes.CAPTURING_NOT_SUPPORTED;
    }
    public static TagKey<EntityType<?>> impl_TELEPORTING_NOT_SUPPORTED() {
        return net.neoforged.neoforge.common.Tags.EntityTypes.TELEPORTING_NOT_SUPPORTED;
    }
}