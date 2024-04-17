/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022 architectury
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

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
