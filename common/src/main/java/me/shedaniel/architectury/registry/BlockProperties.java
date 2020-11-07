package me.shedaniel.architectury.registry;

import me.shedaniel.architectury.ArchitecturyPopulator;
import me.shedaniel.architectury.Populatable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Function;

public abstract class BlockProperties extends BlockBehaviour.Properties implements BlockPropertiesExtension {
    @Populatable
    private static final Impl IMPL = null;
    
    public BlockProperties(Material material, Function<BlockState, MaterialColor> function) {
        super(material, function);
    }
    
    public static BlockProperties of(Material material) {
        return of(material, material.getColor());
    }
    
    public static BlockProperties of(Material material, DyeColor color) {
        return of(material, color.getMaterialColor());
    }
    
    public static BlockProperties of(Material material, MaterialColor color) {
        return IMPL.of(material, color);
    }
    
    public static BlockProperties of(Material material, Function<BlockState, MaterialColor> color) {
        return IMPL.of(material, color);
    }
    
    public static BlockProperties copy(BlockBehaviour block) {
        return IMPL.copy(block);
    }
    
    public static BlockProperties copy(BlockBehaviour.Properties properties) {
        return IMPL.copy(properties);
    }
    
    public interface Impl {
        BlockProperties of(Material material, MaterialColor color);
        
        BlockProperties of(Material material, Function<BlockState, MaterialColor> color);
        
        BlockProperties copy(BlockBehaviour block);
        
        BlockProperties copy(BlockBehaviour.Properties properties);
    }
    
    static {
        ArchitecturyPopulator.populate();
    }
}
