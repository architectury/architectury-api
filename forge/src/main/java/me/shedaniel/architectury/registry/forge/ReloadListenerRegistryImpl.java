package me.shedaniel.architectury.registry.forge;

import com.google.common.collect.Lists;
import me.shedaniel.architectury.registry.ReloadListenerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.ResourcePackType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;

import java.util.List;

public class ReloadListenerRegistryImpl implements ReloadListenerRegistry.Impl {
    private List<IFutureReloadListener> serverDataReloadListeners = Lists.newArrayList();
    
    public ReloadListenerRegistryImpl() {
        MinecraftForge.EVENT_BUS.<AddReloadListenerEvent>addListener(event -> {
            for (IFutureReloadListener listener : serverDataReloadListeners) {
                event.addListener(listener);
            }
        });
    }
    
    @Override
    public void registerReloadListener(ResourcePackType type, IFutureReloadListener listener) {
        if (type == ResourcePackType.SERVER_DATA) {
            serverDataReloadListeners.add(listener);
        } else if (type == ResourcePackType.CLIENT_RESOURCES) {
            reloadClientReloadListener(listener);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void reloadClientReloadListener(IFutureReloadListener listener) {
        ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(listener);
    }
}
