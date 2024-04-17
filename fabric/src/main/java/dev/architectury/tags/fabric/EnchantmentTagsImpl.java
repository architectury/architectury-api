package dev.architectury.tags.fabric;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentTagsImpl {
    public static TagKey<Enchantment> impl_INCREASE_BLOCK_DROPS() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEnchantmentTags.INCREASE_BLOCK_DROPS;
    }
    public static TagKey<Enchantment> impl_INCREASE_ENTITY_DROPS() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEnchantmentTags.INCREASE_ENTITY_DROPS;
    }
    public static TagKey<Enchantment> impl_WEAPON_DAMAGE_ENHANCEMENTS() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEnchantmentTags.WEAPON_DAMAGE_ENHANCEMENTS;
    }
    public static TagKey<Enchantment> impl_ENTITY_SPEED_ENHANCEMENTS() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEnchantmentTags.ENTITY_SPEED_ENHANCEMENTS;
    }
    public static TagKey<Enchantment> impl_ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEnchantmentTags.ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS;
    }
    public static TagKey<Enchantment> impl_ENTITY_DEFENSE_ENHANCEMENT() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalEnchantmentTags.ENTITY_DEFENSE_ENHANCEMENT;
    }
    public static TagKey<Enchantment> impl_ENTITY_DEFENSE_ENHANCEMENTS() {
        return null;
    }
}