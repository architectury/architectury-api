package me.shedaniel.architectury.mixin.fabric.client;

import com.mojang.blaze3d.shaders.Program;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Unique
@Mixin(EffectInstance.class)
public class MixinEffectInstance {
    @Redirect(
            method = "<init>",
            at = @At(value = "NEW",
                    target = "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
                    ordinal = 0)
    )
    private ResourceLocation mojangPls(String _0, ResourceManager rm, String str) {
        return mojangPls(new ResourceLocation(str), ".json");
    }

    @Redirect(
            method = "getOrCreate",
            at = @At(value = "NEW",
                    target = "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
                    ordinal = 0)
    )
    private static ResourceLocation mojangPls(String _0, ResourceManager rm, Program.Type type, String str) {
        return mojangPls(new ResourceLocation(str), type.getExtension());
    }

    private static ResourceLocation mojangPls(ResourceLocation rl, String ext) {
        return new ResourceLocation(rl.getNamespace(), "shaders/program/" + rl.getPath() + ext);
    }
}
