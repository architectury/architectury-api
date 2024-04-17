package dev.architectury.tags;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

// Only available on Fabric and NeoForge
@SuppressWarnings({"UnimplementedExpectPlatform", "unused"})
public class EnchantmentTags {
    public static TagKey<Enchantment> INCREASE_BLOCK_DROPS = impl_INCREASE_BLOCK_DROPS();
    public static TagKey<Enchantment> INCREASE_ENTITY_DROPS = impl_INCREASE_ENTITY_DROPS();
    public static TagKey<Enchantment> WEAPON_DAMAGE_ENHANCEMENTS = impl_WEAPON_DAMAGE_ENHANCEMENTS();
    public static TagKey<Enchantment> ENTITY_SPEED_ENHANCEMENTS = impl_ENTITY_SPEED_ENHANCEMENTS();
    public static TagKey<Enchantment> ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS = impl_ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS();
    // Fabric only
    public static TagKey<Enchantment> ENTITY_DEFENSE_ENHANCEMENT = impl_ENTITY_DEFENSE_ENHANCEMENT();
    // NeoForge only
    public static TagKey<Enchantment> ENTITY_DEFENSE_ENHANCEMENTS = impl_ENTITY_DEFENSE_ENHANCEMENTS();
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Enchantment> impl_INCREASE_BLOCK_DROPS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Enchantment> impl_INCREASE_ENTITY_DROPS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Enchantment> impl_WEAPON_DAMAGE_ENHANCEMENTS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Enchantment> impl_ENTITY_SPEED_ENHANCEMENTS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Enchantment> impl_ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS() {
        throw new AssertionError();
    }
    // Returns null on NeoForge
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Enchantment> impl_ENTITY_DEFENSE_ENHANCEMENT() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Enchantment> impl_ENTITY_DEFENSE_ENHANCEMENTS() {
        throw new AssertionError();
    }
}