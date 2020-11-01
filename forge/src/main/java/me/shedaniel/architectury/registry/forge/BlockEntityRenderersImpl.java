package me.shedaniel.architectury.registry.forge;

import me.shedaniel.architectury.registry.BlockEntityRenderers;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.function.Function;

public class BlockEntityRenderersImpl implements BlockEntityRenderers.Impl {
    @Override
    public <T extends TileEntity> void registerRenderer(TileEntityType<T> type, Function<TileEntityRendererDispatcher, TileEntityRenderer<T>> provider) {
        ClientRegistry.bindTileEntityRenderer(type, provider);
    }
}
