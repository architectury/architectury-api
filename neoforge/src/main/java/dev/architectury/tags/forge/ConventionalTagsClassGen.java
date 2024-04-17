package dev.architectury.tags.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.Tags;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * NeoForge specific code to generate conventional tags.
 * <br>
 * Fabric handles the actual class generation, this just generates a {@code neoforge-tags.txt} file.
 */
public class ConventionalTagsClassGen {
    public static final Class[] TAG_CLASSES = new Class[]{
            Tags.Biomes.class,
            Tags.Blocks.class,
            Tags.DamageTypes.class,
            Tags.Enchantments.class,
            Tags.EntityTypes.class,
            Tags.Fluids.class,
            Tags.Items.class,
            Tags.Structures.class
    };
    
    public static void generateTagList() throws IllegalAccessException, IOException {
        StringBuilder builder = new StringBuilder();
        for (Class<?> tagClass : TAG_CLASSES) {
            for (Field field : tagClass.getFields()) {
                if (Modifier.isStatic(field.getModifiers()) && TagKey.class.isAssignableFrom(field.getType())) {
                    TagKey<?> tagKey = (TagKey<?>) field.get(null);
                    ResourceLocation tagType = tagKey.registry().location();
                    ResourceLocation tagId = tagKey.location();
                    if (!"c".equals(tagId.getNamespace())) continue;;
                    builder.append(tagType).append("|").append(tagClass.getName()).append("|").append(field.getName()).append("|").append(tagId).append("\n");
                }
            }
        }
        Files.writeString(Path.of("../../fabric/run/neoforge-tags.txt"), builder.toString());
        System.out.println("Generated NeoForge's conventional tags");
        System.exit(0);
    }
}
