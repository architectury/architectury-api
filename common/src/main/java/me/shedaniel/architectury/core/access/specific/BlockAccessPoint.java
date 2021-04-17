package me.shedaniel.architectury.core.access.specific;

import me.shedaniel.architectury.core.access.DelegateAccessPoint;
import me.shedaniel.architectury.impl.BlockAccessPointImpl;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface BlockAccessPoint<T, SELF extends BlockAccessPoint<T, SELF>> extends DelegateAccessPoint<BlockAccess<T>, SELF>, BlockAccess<T> {
    static <T> BlockAccessPoint<T, ?> create() {
        return new BlockAccessPointImpl<>();
    }
    
    SELF forBlock(Block block, BlockAccess<T> access);
    
    SELF forBlock(Tag.Named<Block> tag, BlockAccess<T> access);
    
    SELF forBlockEntity(BlockEntityType<?> type, BlockAccess<T> access);
}
