package dev.architectury.tags;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

// Only available on Fabric and NeoForge
@SuppressWarnings({"UnimplementedExpectPlatform", "unused"})
public class FluidTags {
    public static TagKey<Fluid> LAVA = impl_LAVA();
    public static TagKey<Fluid> WATER = impl_WATER();
    public static TagKey<Fluid> MILK = impl_MILK();
    public static TagKey<Fluid> HONEY = impl_HONEY();
    public static TagKey<Fluid> HIDDEN_FROM_RECIPE_VIEWERS = impl_HIDDEN_FROM_RECIPE_VIEWERS();
    // NeoForge only
    public static TagKey<Fluid> GASEOUS = impl_GASEOUS();
    // NeoForge only
    public static TagKey<Fluid> POTION = impl_POTION();
    // NeoForge only
    public static TagKey<Fluid> SUSPICIOUS_STEW = impl_SUSPICIOUS_STEW();
    // NeoForge only
    public static TagKey<Fluid> MUSHROOM_STEW = impl_MUSHROOM_STEW();
    // NeoForge only
    public static TagKey<Fluid> RABBIT_STEW = impl_RABBIT_STEW();
    // NeoForge only
    public static TagKey<Fluid> BEETROOT_SOUP = impl_BEETROOT_SOUP();
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Fluid> impl_LAVA() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Fluid> impl_WATER() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Fluid> impl_MILK() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Fluid> impl_HONEY() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Fluid> impl_HIDDEN_FROM_RECIPE_VIEWERS() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Fluid> impl_GASEOUS() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Fluid> impl_POTION() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Fluid> impl_SUSPICIOUS_STEW() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Fluid> impl_MUSHROOM_STEW() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Fluid> impl_RABBIT_STEW() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Fluid> impl_BEETROOT_SOUP() {
        throw new AssertionError();
    }
}