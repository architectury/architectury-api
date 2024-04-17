package dev.architectury.tags.forge;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentTagsImpl {
    public static TagKey<Enchantment> impl_INCREASE_BLOCK_DROPS() {
        return net.neoforged.neoforge.common.Tags.Enchantments.INCREASE_BLOCK_DROPS;
    }
    public static TagKey<Enchantment> impl_INCREASE_ENTITY_DROPS() {
        return net.neoforged.neoforge.common.Tags.Enchantments.INCREASE_ENTITY_DROPS;
    }
    public static TagKey<Enchantment> impl_WEAPON_DAMAGE_ENHANCEMENTS() {
        return net.neoforged.neoforge.common.Tags.Enchantments.WEAPON_DAMAGE_ENHANCEMENTS;
    }
    public static TagKey<Enchantment> impl_ENTITY_SPEED_ENHANCEMENTS() {
        return net.neoforged.neoforge.common.Tags.Enchantments.ENTITY_SPEED_ENHANCEMENTS;
    }
    public static TagKey<Enchantment> impl_ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS() {
        return net.neoforged.neoforge.common.Tags.Enchantments.ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS;
    }
    public static TagKey<Enchantment> impl_ENTITY_DEFENSE_ENHANCEMENT() {
        return null;
    }
    public static TagKey<Enchantment> impl_ENTITY_DEFENSE_ENHANCEMENTS() {
        return net.neoforged.neoforge.common.Tags.Enchantments.ENTITY_DEFENSE_ENHANCEMENTS;
    }
}