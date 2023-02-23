package dev.architectury.registry.registries.options;

import dev.architectury.registry.registries.Registrar;

import java.util.function.Consumer;

/**
 * A {@link RegistrarOption} that adds code to run when the registrar is created.
 * <p>Fabric: This runs immediately after the registry is created.
 * <p>Forge: This runs during <code>NewRegistryEvent</code> at the same time the built registrar is initialized.
 *
 * @param <T> The registry object type
 */
@FunctionalInterface
public interface OnCreateCallback<T> extends Consumer<Registrar<T>>, RegistrarOption {
}
