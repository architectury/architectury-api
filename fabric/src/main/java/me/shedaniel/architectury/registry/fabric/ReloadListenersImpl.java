package me.shedaniel.architectury.registry.fabric;

import com.google.common.primitives.Longs;
import me.shedaniel.architectury.registry.ReloadListeners;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ReloadListenersImpl implements ReloadListeners.Impl {
    private static final SecureRandom RANDOM = new SecureRandom();
    
    @Override
    public void registerReloadListener(PackType type, PreparableReloadListener listener) {
        byte[] bytes = new byte[8];
        RANDOM.nextBytes(bytes);
        ResourceLocation id = new ResourceLocation("architectury:reload_" + StringUtils.leftPad(Math.abs(Longs.fromByteArray(bytes)) + "", 19));
        ResourceManagerHelper.get(type).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return id;
            }
    
            @Override
            public String getName() {
                return listener.getName();
            }
    
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
                return listener.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
            }
        });
    }
}
