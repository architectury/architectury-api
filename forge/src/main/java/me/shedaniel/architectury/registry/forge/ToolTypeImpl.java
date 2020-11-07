package me.shedaniel.architectury.registry.forge;


import me.shedaniel.architectury.registry.ToolType;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;

public class ToolTypeImpl implements ToolType.Impl {
    @Override
    public ITag<Item> pickaxeTag() {
        return null;
    }
    
    @Override
    public ITag<Item> axeTag() {
        return null;
    }
    
    @Override
    public ITag<Item> hoeTag() {
        return null;
    }
    
    @Override
    public ITag<Item> shovelTag() {
        return null;
    }
}
