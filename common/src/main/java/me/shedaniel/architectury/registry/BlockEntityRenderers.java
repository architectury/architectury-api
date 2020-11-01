package me.shedaniel.architectury.registry;

import me.shedaniel.architectury.ArchitecturyPopulator;
import me.shedaniel.architectury.Populatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public final class BlockEntityRenderers {
    private BlockEntityRenderers() {}
    
    @Populatable
    private static final Impl IMPL = null;
    
    public static <T extends BlockEntity> void registerRenderer(BlockEntityType<T> type, Function<BlockEntityRenderDispatcher, BlockEntityRenderer<T>> provider) {
        IMPL.registerRenderer(type, provider);
    }
    
    public interface Impl {
        <T extends BlockEntity> void registerRenderer(BlockEntityType<T> type, Function<BlockEntityRenderDispatcher, BlockEntityRenderer<T>> provider);
    }
    
    static {
        ArchitecturyPopulator.populate(BlockEntityRenderers.class);
    }
}
