package me.shedaniel.architectury.registry.entity.forge;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.function.Function;

public class EntityRenderersImpl {
    public static <T extends Entity> void register(EntityType<T> type, Function<EntityRenderDispatcher, EntityRenderer<T>> factory) {
        RenderingRegistry.registerEntityRenderingHandler(type, factory::apply);
    }
}
