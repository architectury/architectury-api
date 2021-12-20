package dev.architectury.event.events.client;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface ClientReloadShadersEvent {
    /**
     * Invoked when client reloads shaders.
     *
     * @see net.minecraft.client.renderer.GameRenderer#reloadShaders(ResourceManager)
     */
    Event<ClientReloadShadersEvent> EVENT = EventFactory.createLoop();
    
    void reload(ResourceManager manager, ShadersSink sink);
    
    @FunctionalInterface
    interface ShadersSink {
        void registerShader(ShaderInstance shader, Consumer<ShaderInstance> callback);
    }
}
