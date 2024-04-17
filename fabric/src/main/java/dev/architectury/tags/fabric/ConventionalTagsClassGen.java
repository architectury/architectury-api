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

package dev.architectury.tags.fabric;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.tag.convention.v2.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Fabric specific code to generate conventional tags.
 * <br>
 * Fabric also handles the conventional tags class generation.
 */
public class ConventionalTagsClassGen {
    public static final Class[] TAG_CLASSES = new Class[]{
            ConventionalBiomeTags.class,
            ConventionalBlockTags.class,
            ConventionalEnchantmentTags.class,
            ConventionalEntityTypeTags.class,
            ConventionalFluidTags.class,
            ConventionalItemTags.class,
            ConventionalStructureTags.class
    };
    
    public static void generate() {
        try {
            generateStubClass();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Generated Classes");
        System.exit(0);
    }
    
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
        Files.writeString(Path.of("fabric-tags.txt"), builder.toString());
        System.out.println("Generated Fabric's conventional tags");
        System.exit(0);
    }
    
    public static void generateStubClass() throws IOException {
        Path fabricTagsPath = Path.of("fabric-tags.txt");
        Path neoForgeTagsPath = Path.of("neoforge-tags.txt");
        List<ConventionalTag> fabricConventionTags = new ArrayList<>();
        List<ConventionalTag> neoForgeConventionTags = new ArrayList<>();
        String fabricTagsString = Files.readString(fabricTagsPath);
        String neoForgeTagsString = Files.readString(neoForgeTagsPath);
        for (String s : fabricTagsString.split("\n")) {
            fabricConventionTags.add(ConventionalTag.parse(s));
        }
        for (String s : neoForgeTagsString.split("\n")) {
            neoForgeConventionTags.add(ConventionalTag.parse(s));
        }
        List<Pair<ConventionalTag, ConventionalTag>> paired = ConventionalTag.pair(fabricConventionTags, neoForgeConventionTags);
        Map<ResourceLocation, List<Pair<ConventionalTag, ConventionalTag>>> categories = ConventionalTag.categories(paired);
        for (Map.Entry<ResourceLocation, List<Pair<ConventionalTag, ConventionalTag>>> entry : categories.entrySet()) {
            StringBuilder commonClass = new StringBuilder();
            StringBuilder fabricClass = new StringBuilder();
            StringBuilder neoForgeClass = new StringBuilder();
            
            Class<?> registryClass = switch (entry.getKey().getPath()) {
                case "worldgen/biome" -> Biome.class;
                case "block" -> Block.class;
                case "enchantment" -> Enchantment.class;
                case "entity_type" -> EntityType.class;
                case "fluid" -> Fluid.class;
                case "item" -> Item.class;
                case "worldgen/structure" -> Structure.class;
                default -> throw new IllegalStateException("Unexpected value: " + entry.getKey().getPath());
            };
            String importRegistryName = registryClass.getName().replaceAll("\\$", "/").replace("/", ".");
            String[] temp = entry.getKey().getPath().split("/");
            String categoryShortName = WordUtils.capitalize(temp[temp.length-1].replaceAll("_", " ")).replaceAll(" ", "");
            commonClass.append(String.format("""
                    package dev.architectury.tags;
                    import dev.architectury.injectables.annotations.ExpectPlatform;
                    import dev.architectury.injectables.annotations.PlatformOnly;
                    import net.minecraft.tags.TagKey;
                    import %s;
                    
                    // Only available on Fabric and NeoForge
                    @SuppressWarnings({"UnimplementedExpectPlatform", "unused"})
                    public class %sTags {
                                        """, importRegistryName, categoryShortName));
            fabricClass.append(String.format("""
                    package dev.architectury.tags.fabric;
                    import net.minecraft.tags.TagKey;
                    import %s;
                    
                    public class %sTagsImpl {
                                        """, importRegistryName, categoryShortName));
            neoForgeClass.append(String.format("""
                    package dev.architectury.tags.forge;
                    import net.minecraft.tags.TagKey;
                    import %s;
                    
                    public class %sTagsImpl {
                                        """, importRegistryName, categoryShortName));
            StringBuilder commonClassEnd = new StringBuilder();
            StringBuilder fabricClassEnd = new StringBuilder();
            StringBuilder neoForgeClassEnd = new StringBuilder();
            String categoryShortName2 = categoryShortName;
            if (categoryShortName2.equals("EntityType")) categoryShortName2 += "<?>";
            for (Pair<ConventionalTag, ConventionalTag> pair : entry.getValue()) {
                ConventionalTag master = pair.getFirst() == null ? pair.getSecond() : pair.getFirst();
                String fieldName = master.tagId.getPath().replaceAll("/", "_").toUpperCase(Locale.ROOT);
                {
                    String bothStart = "    public static TagKey<%s> %s = impl_%s();\n";
                    String fabricStart = "    // Fabric only\n    public static TagKey<%s> %s = impl_%s();\n";
                    String neoForgeStart = "    // NeoForge only\n    public static TagKey<%s> %s = impl_%s();\n";
                    String bothEnd = "    @PlatformOnly({\"fabric\",\"neoforge\"})\n    @ExpectPlatform\n    private static TagKey<%s> impl_%s() {\n        throw new AssertionError();\n    }\n";
                    String fabricEnd = "    // Returns null on NeoForge\n    @PlatformOnly({\"fabric\",\"neoforge\"})\n    @ExpectPlatform\n    private static TagKey<%s> impl_%s() {\n        throw new AssertionError();\n    }\n";
                    String neoForgeEnd = "    // Returns null on Fabric\n    @PlatformOnly({\"fabric\",\"neoforge\"})\n    @ExpectPlatform\n    private static TagKey<%s> impl_%s() {\n        throw new AssertionError();\n    }\n";
                    if (pair.getFirst() == null) {
                        commonClass.append(neoForgeStart.formatted(categoryShortName2, fieldName, fieldName));
                        commonClassEnd.append(neoForgeEnd.formatted(categoryShortName2, fieldName));
                    } else if (pair.getSecond() == null) {
                        commonClass.append(fabricStart.formatted(categoryShortName2, fieldName, fieldName));
                        commonClassEnd.append(fabricEnd.formatted(categoryShortName2, fieldName));
                    } else {
                        commonClass.append(bothStart.formatted(categoryShortName2, fieldName, fieldName));
                        commonClassEnd.append(bothEnd.formatted(categoryShortName2, fieldName));
                    }
                }
                {
                    String str = "    public static TagKey<%s> impl_%s() {\n        return %s.%s;\n    }\n";
                    String str2 = "    public static TagKey<%s> impl_%s() {\n        return null;\n    }\n";
                    if (pair.getFirst() != null) {
                        fabricClass.append(str.formatted(categoryShortName2, fieldName, pair.getFirst().tagClassName.replaceAll("\\$", "."), pair.getFirst().tagFieldName));
                    } else {
                        fabricClass.append(str2.formatted(categoryShortName2, fieldName));
                    }
                    if (pair.getSecond() != null) {
                        neoForgeClass.append(str.formatted(categoryShortName2, fieldName, pair.getSecond().tagClassName.replaceAll("\\$", "."), pair.getSecond().tagFieldName));
                    } else {
                        neoForgeClass.append(str2.formatted(categoryShortName2, fieldName));
                    }
                }
            }
            commonClassEnd.append("}");
            fabricClassEnd.append("}");
            neoForgeClassEnd.append("}");
            commonClass.append(commonClassEnd);
            fabricClass.append(fabricClassEnd);
            neoForgeClass.append(neoForgeClassEnd);
            Files.writeString(Path.of("../../common/src/main/java/dev/architectury/tags/" + categoryShortName + "Tags.java"), commonClass);
            Files.writeString(Path.of("../../fabric/src/main/java/dev/architectury/tags/fabric/" + categoryShortName + "TagsImpl.java"), fabricClass);
            Files.writeString(Path.of("../../neoforge/src/main/java/dev/architectury/tags/forge/" + categoryShortName + "TagsImpl.java"), neoForgeClass);
        }
    }
    
    record ConventionalTag(ResourceLocation tagType, String tagClassName, String tagFieldName, ResourceLocation tagId) {
        public static ConventionalTag parse(String str) {
            String[] sections = str.split("\\|");
            //ConventionalBiomeTags
            return new ConventionalTag(new ResourceLocation(sections[0]), sections[1], sections[2], new ResourceLocation(sections[3]));
        }
        
        public static List<Pair<ConventionalTag, ConventionalTag>> pair(List<ConventionalTag> one, List<ConventionalTag> two) {
            List<String> oneIds = one.stream().map(a -> a.tagType() + "|" + a.tagId()).toList();
            List<String> twoIds = two.stream().map(a -> a.tagType() + "|" + a.tagId()).toList();
            List<String> combinedIds = new ArrayList<>();
            for (String oneId : oneIds) {
                if (combinedIds.contains(oneId)) continue;
                combinedIds.add(oneId);
            }
            for (String twoId : twoIds) {
                if (combinedIds.contains(twoId)) continue;
                combinedIds.add(twoId);
            }
            List<Pair<ConventionalTag, ConventionalTag>> pairList = new ArrayList<>();
            for (String combinedId : combinedIds) {
                Optional<ConventionalTag> first = one.stream().filter(a -> combinedId.equals(a.tagType() + "|" + a.tagId())).findFirst();
                Optional<ConventionalTag> second = two.stream().filter(a -> combinedId.equals(a.tagType() + "|" + a.tagId())).findFirst();
                pairList.add(Pair.of(first.orElse(null), second.orElse(null)));
            }
            return pairList;
        }
        public static Map<ResourceLocation, List<Pair<ConventionalTag, ConventionalTag>>> categories(List<Pair<ConventionalTag, ConventionalTag>> pairs) {
            LinkedHashMap<ResourceLocation, List<Pair<ConventionalTag, ConventionalTag>>> map = new LinkedHashMap<>();
            for (Pair<ConventionalTag, ConventionalTag> pair : pairs) {
                ConventionalTag master = pair.getFirst() == null ? pair.getSecond() : pair.getFirst();
                map.computeIfAbsent(master.tagType(), a -> new ArrayList<>());
                map.get(master.tagType()).add(pair);
            }
            return map;
        }
    }
}
