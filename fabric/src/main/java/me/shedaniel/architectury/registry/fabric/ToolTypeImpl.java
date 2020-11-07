package me.shedaniel.architectury.registry.fabric;

import me.shedaniel.architectury.registry.ToolType;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public class ToolTypeImpl implements ToolType.Impl {
    @Override
    public Tag<Item> pickaxeTag() {
        return FabricToolTags.PICKAXES;
    }
    
    @Override
    public Tag<Item> axeTag() {
        return FabricToolTags.AXES;
    }
    
    @Override
    public Tag<Item> hoeTag() {
        return FabricToolTags.HOES;
    }
    
    @Override
    public Tag<Item> shovelTag() {
        return FabricToolTags.SHOVELS;
    }
}
