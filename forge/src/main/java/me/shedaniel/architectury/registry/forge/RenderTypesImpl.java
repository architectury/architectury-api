package me.shedaniel.architectury.registry.forge;

import me.shedaniel.architectury.registry.RenderTypes;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.fluid.Fluid;

public class RenderTypesImpl implements RenderTypes.Impl {
    @Override
    public void register(RenderType type, Block... blocks) {
        for (Block block : blocks) {
            RenderTypeLookup.setRenderLayer(block, type);
        }
    }
    
    @Override
    public void register(RenderType type, Fluid... fluids) {
        for (Fluid fluid : fluids) {
            RenderTypeLookup.setRenderLayer(fluid, type);
        }
    }
}
