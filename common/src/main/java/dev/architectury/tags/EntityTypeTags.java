package dev.architectury.tags;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

// Only available on Fabric and NeoForge
@SuppressWarnings({"UnimplementedExpectPlatform", "unused"})
public class EntityTypeTags {
    public static TagKey<EntityType<?>> BOSSES = impl_BOSSES();
    public static TagKey<EntityType<?>> MINECARTS = impl_MINECARTS();
    public static TagKey<EntityType<?>> BOATS = impl_BOATS();
    public static TagKey<EntityType<?>> CAPTURING_NOT_SUPPORTED = impl_CAPTURING_NOT_SUPPORTED();
    public static TagKey<EntityType<?>> TELEPORTING_NOT_SUPPORTED = impl_TELEPORTING_NOT_SUPPORTED();
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<EntityType<?>> impl_BOSSES() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<EntityType<?>> impl_MINECARTS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<EntityType<?>> impl_BOATS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<EntityType<?>> impl_CAPTURING_NOT_SUPPORTED() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<EntityType<?>> impl_TELEPORTING_NOT_SUPPORTED() {
        throw new AssertionError();
    }
}