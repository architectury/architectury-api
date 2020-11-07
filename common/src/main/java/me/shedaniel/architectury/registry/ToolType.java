package me.shedaniel.architectury.registry;

import me.shedaniel.architectury.Populatable;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public final class ToolType {
    public static final ToolType PICKAXE = create("pickaxe", ToolType::pickaxeTag);
    public static final ToolType AXE = create("axe", ToolType::axeTag);
    public static final ToolType HOE = create("hoe", ToolType::hoeTag);
    public static final ToolType SHOVEL = create("shovel", ToolType::shovelTag);
    @Populatable
    private static final Impl IMPL = null;
    
    private static Tag<Item> pickaxeTag() {
        return IMPL.pickaxeTag();
    }
    
    private static Tag<Item> axeTag() {
        return IMPL.axeTag();
    }
    
    private static Tag<Item> hoeTag() {
        return IMPL.hoeTag();
    }
    
    private static Tag<Item> shovelTag() {
        return IMPL.shovelTag();
    }
    
    public final String forgeName;
    public final Supplier<Tag<Item>> fabricTag;
    private Object obj;
    
    private ToolType(String forgeName, Supplier<Tag<Item>> fabricTag) {
        this.forgeName = forgeName;
        this.fabricTag = fabricTag;
    }
    
    public static ToolType create(String forgeName, Supplier<Tag<Item>> fabricTag) {
        return new ToolType(forgeName, fabricTag);
    }
    
    public interface Impl {
        Tag<Item> pickaxeTag();
        
        Tag<Item> axeTag();
        
        Tag<Item> hoeTag();
        
        Tag<Item> shovelTag();
    }
}
