package me.shedaniel.architectury.registry;

public interface BlockPropertiesExtension {
    default BlockProperties tool(ToolType type) {
        return tool(type, 0);
    }
    
    BlockProperties tool(ToolType type, int level);
}