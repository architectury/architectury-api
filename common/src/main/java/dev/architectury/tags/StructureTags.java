package dev.architectury.tags;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

// Only available on Fabric and NeoForge
@SuppressWarnings({"UnimplementedExpectPlatform", "unused"})
public class StructureTags {
    public static TagKey<Structure> HIDDEN_FROM_DISPLAYERS = impl_HIDDEN_FROM_DISPLAYERS();
    public static TagKey<Structure> HIDDEN_FROM_LOCATOR_SELECTION = impl_HIDDEN_FROM_LOCATOR_SELECTION();
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Structure> impl_HIDDEN_FROM_DISPLAYERS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Structure> impl_HIDDEN_FROM_LOCATOR_SELECTION() {
        throw new AssertionError();
    }
}