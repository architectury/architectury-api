package me.shedaniel.architectury.registry.fabric;

import me.shedaniel.architectury.registry.KeyBindings;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class KeyBindingsImpl implements KeyBindings.Impl {
    @Override
    public void registerKeyBinding(KeyMapping binding) {
        KeyBindingHelper.registerKeyBinding(binding);
    }
}
