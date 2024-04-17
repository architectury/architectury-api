package dev.architectury.tags.fabric;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class StructureTagsImpl {
    public static TagKey<Structure> implHIDDEN_FROM_DISPLAYERS() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalStructureTags.HIDDEN_FROM_DISPLAYERS;
    }
    public static TagKey<Structure> implHIDDEN_FROM_LOCATOR_SELECTION() {
        return net.fabricmc.fabric.api.tag.convention.v2.ConventionalStructureTags.HIDDEN_FROM_LOCATOR_SELECTION;
    }
}