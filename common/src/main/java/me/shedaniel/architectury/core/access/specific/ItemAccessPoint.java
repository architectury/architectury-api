package me.shedaniel.architectury.core.access.specific;

import me.shedaniel.architectury.core.access.DelegateAccessPoint;
import me.shedaniel.architectury.impl.access.ItemAccessPointImpl;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public interface ItemAccessPoint<T, SELF extends ItemAccessPoint<T, SELF>> extends DelegateAccessPoint<ItemAccess<T>, SELF>, ItemAccess<T> {
    static <T> ItemAccessPoint<T, ?> create() {
        return new ItemAccessPointImpl<>();
    }
    
    SELF forItem(Item item, ItemAccess<T> access);
    
    SELF forItemClass(Class<? extends Item> item, ItemAccess<T> access);
    
    SELF forItem(Tag.Named<Item> tag, ItemAccess<T> access);
}
