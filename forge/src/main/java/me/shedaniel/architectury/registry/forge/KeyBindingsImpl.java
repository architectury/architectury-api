package me.shedaniel.architectury.registry.forge;

import me.shedaniel.architectury.registry.KeyBindings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindingsImpl implements KeyBindings.Impl {
    @Override
    public void registerKeyBinding(KeyBinding keyBinding) {
        ClientRegistry.registerKeyBinding(keyBinding);
    }
}
