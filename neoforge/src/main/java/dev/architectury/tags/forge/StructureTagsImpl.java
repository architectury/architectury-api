package dev.architectury.tags.forge;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class StructureTagsImpl {
    public static TagKey<Structure> implHIDDEN_FROM_DISPLAYERS() {
        return net.neoforged.neoforge.common.Tags.Structures.HIDDEN_FROM_DISPLAYERS;
    }
    public static TagKey<Structure> implHIDDEN_FROM_LOCATOR_SELECTION() {
        return net.neoforged.neoforge.common.Tags.Structures.HIDDEN_FROM_LOCATOR_SELECTION;
    }
}