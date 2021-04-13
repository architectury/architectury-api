/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

package me.shedaniel.architectury.event.events;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

@Environment(EnvType.CLIENT)
public interface TooltipEvent {
    Event<Item> ITEM = EventFactory.createLoop();
    /**
     * Render vanilla events are not invoked on the forge side.
     */
    Event<RenderVanilla> RENDER_VANILLA_PRE = EventFactory.createInteractionResult();
    /**
     * Render forge events are only invoked on the forge side.
     */
    Event<RenderForge> RENDER_FORGE_PRE = EventFactory.createInteractionResult();
    Event<RenderModifyPosition> RENDER_MODIFY_POSITION = EventFactory.createLoop();
    Event<RenderModifyColor> RENDER_MODIFY_COLOR = EventFactory.createLoop();
    
    @Environment(EnvType.CLIENT)
    interface Item {
        void append(ItemStack stack, List<Component> lines, TooltipFlag flag);
    }
    
    @Environment(EnvType.CLIENT)
    interface RenderVanilla {
        InteractionResult renderTooltip(PoseStack matrices, List<? extends FormattedCharSequence> texts, int x, int y);
    }
    
    @Environment(EnvType.CLIENT)
    interface RenderForge {
        InteractionResult renderTooltip(PoseStack matrices, List<? extends FormattedText> texts, int x, int y);
    }
    
    @Environment(EnvType.CLIENT)
    interface RenderModifyPosition {
        void renderTooltip(PoseStack matrices, PositionContext context);
    }
    
    @Environment(EnvType.CLIENT)
    interface RenderModifyColor {
        void renderTooltip(PoseStack matrices, int x, int y, ColorContext context);
    }
    
    @Environment(EnvType.CLIENT)
    interface PositionContext {
        int getTooltipX();
        
        void setTooltipX(int x);
        
        int getTooltipY();
        
        void setTooltipY(int y);
    }
    
    @Environment(EnvType.CLIENT)
    interface ColorContext {
        int getBackgroundColor();
        
        void setBackgroundColor(int color);
        
        int getOutlineGradientTopColor();
        
        void setOutlineGradientTopColor(int color);
        
        int getOutlineGradientBottomColor();
        
        void setOutlineGradientBottomColor(int color);
    }
}
