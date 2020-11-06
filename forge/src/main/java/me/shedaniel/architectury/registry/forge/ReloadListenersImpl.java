/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.shedaniel.architectury.registry.forge;

import com.google.common.collect.Lists;
import me.shedaniel.architectury.registry.ReloadListeners;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.ResourcePackType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;

import java.util.List;

public class ReloadListenersImpl implements ReloadListeners.Impl {
    private List<IFutureReloadListener> serverDataReloadListeners = Lists.newArrayList();
    
    public ReloadListenersImpl() {
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
