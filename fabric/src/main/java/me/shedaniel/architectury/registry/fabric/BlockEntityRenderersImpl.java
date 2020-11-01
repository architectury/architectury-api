package me.shedaniel.architectury.registry.fabric;

import me.shedaniel.architectury.registry.BlockEntityRenderers;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Function;

public class BlockEntityRenderersImpl implements BlockEntityRenderers.Impl {
    @Override
    public <T extends BlockEntity> void registerRenderer(BlockEntityType<T> type, Function<BlockEntityRenderDispatcher, BlockEntityRenderer<T>> provider) {
        BlockEntityRendererRegistry.INSTANCE.register(type, provider);
    }
}
