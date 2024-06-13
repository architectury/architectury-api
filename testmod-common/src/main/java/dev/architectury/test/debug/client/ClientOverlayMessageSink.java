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

package dev.architectury.test.debug.client;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.test.debug.ConsoleMessageSink;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ClientOverlayMessageSink extends ConsoleMessageSink {
    private final List<Message> messages = Collections.synchronizedList(Lists.newArrayList());
    
    public ClientOverlayMessageSink() {
        ClientGuiEvent.RENDER_POST.register((screen, graphics, mouseX, mouseY, delta) -> render(graphics, delta));
        ClientGuiEvent.RENDER_HUD.register((graphics, delta) -> {
            if (Minecraft.getInstance().screen == null && !Minecraft.getInstance().gui.getDebugOverlay().showDebugScreen()) {
                render(graphics, delta);
            }
        });
    }
    
    @Override
    public void accept(String message) {
        super.accept(message);
        messages.add(0, new Message(Component.literal(message), Util.getMillis()));
    }
    
    public void render(GuiGraphics graphics, DeltaTracker delta) {
        graphics.pose().pushPose();
        graphics.pose().scale(0.5f, 0.5f, 1f);
        var minecraft = Minecraft.getInstance();
        var currentMills = Util.getMillis();
        var lineHeight = minecraft.font.lineHeight;
        
        synchronized (messages) {
            var messageIterator = messages.iterator();
            var y = 1;
            
            RenderSystem.enableBlend();
            
            while (messageIterator.hasNext()) {
                var message = messageIterator.next();
                var timeExisted = (int) (currentMills - message.created);
                
                if (timeExisted >= 5000) {
                    messageIterator.remove();
                } else {
                    if (y - 1 < minecraft.getWindow().getGuiScaledHeight()) {
                        var textWidth = minecraft.font.width(message.text);
                        var alpha = (int) Mth.clamp((5000 - timeExisted) / 5000f * 400f + 8, 0, 255);
                        graphics.fill(0, y - 1, 2 + textWidth + 1, y + lineHeight - 1, 0x505050 + ((alpha * 144 / 255) << 24));
                        graphics.drawString(minecraft.font, message.text, 1, y, 0xE0E0E0 + (alpha << 24));
                    }
                    y += lineHeight;
                }
            }
        }
        
        RenderSystem.disableBlend();
        graphics.pose().popPose();
    }
    
    private record Message(Component text, long created) {
    }
}
