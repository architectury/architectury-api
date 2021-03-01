package me.shedaniel.architectury.registry.entity;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public final class EntityRenderers {
    private EntityRenderers() {}
    
    @ExpectPlatform
    public static <T extends Entity> void register(EntityType<T> type, Function<EntityRenderDispatcher, EntityRenderer<T>> factory) {
        throw new AssertionError();
    }
}
