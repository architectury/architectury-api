/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022 architectury
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package me.shedaniel.architectury.test.debug.client;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.architectury.event.events.GuiEvent;
import me.shedaniel.architectury.test.debug.ConsoleMessageSink;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ClientOverlayMessageSink extends ConsoleMessageSink {
    private final List<Message> messages = Collections.synchronizedList(Lists.newArrayList());
    
    public ClientOverlayMessageSink() {
        GuiEvent.RENDER_POST.register((screen, matrices, mouseX, mouseY, delta) -> render(matrices, delta));
        GuiEvent.RENDER_HUD.register((matrices, tickDelta) -> {
            if (Minecraft.getInstance().screen == null && !Minecraft.getInstance().options.renderDebug) {
                render(matrices, tickDelta);
            }
        });
    }
    
    @Override
    public void accept(String message) {
        super.accept(message);
        messages.add(0, new Message(new TextComponent(message), Util.getMillis()));
    }
    
    public void render(PoseStack matrices, float delta) {
        matrices.pushPose();
        matrices.scale(0.5f, 0.5f, 1f);
        Minecraft minecraft = Minecraft.getInstance();
        long currentMills = Util.getMillis();
        int lineHeight = minecraft.font.lineHeight;
        
        synchronized (messages) {
            Iterator<Message> messageIterator = messages.iterator();
            int y = 1;
            
            RenderSystem.enableBlend();
            
            while (messageIterator.hasNext()) {
                Message message = messageIterator.next();
                int timeExisted = (int) (currentMills - message.created);
                
                if (timeExisted >= 5000) {
                    messageIterator.remove();
                } else {
                    if (y - 1 < minecraft.getWindow().getGuiScaledHeight()) {
                        int textWidth = minecraft.font.width(message.text);
                        int alpha = (int) Mth.clamp((5000 - timeExisted) / 5000f * 400f + 8, 0, 255);
                        GuiComponent.fill(matrices, 0, y - 1, 2 + textWidth + 1, y + lineHeight - 1, 0x505050 + ((alpha * 144 / 255) << 24));
                        minecraft.font.draw(matrices, message.text, 1, y, 0xE0E0E0 + (alpha << 24));
                    }
                    y += lineHeight;
                }
            }
        }
        
        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();
        matrices.popPose();
    }
    
    private static class Message {
        private final Component text;
        private final long created;
        
        public Message(Component text, long created) {
            this.text = text;
            this.created = created;
        }
    }
}
