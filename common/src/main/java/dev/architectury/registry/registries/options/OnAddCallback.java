package dev.architectury.registry.registries.options;

import net.minecraft.resources.ResourceLocation;

/**
 * A {@link RegistrarOption} that adds code to run for each registry entry added.
 *
 * @param <T> The registry object type
 */
@FunctionalInterface
public interface OnAddCallback<T> extends RegistrarOption {
    void onAdd(int id, ResourceLocation name, T object);
}
