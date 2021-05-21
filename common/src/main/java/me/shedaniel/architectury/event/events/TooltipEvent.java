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

/**
 * Should be moved to the client package in version 2.0
 */
@Environment(EnvType.CLIENT)
public interface TooltipEvent {
    /**
     * @see Item#append(ItemStack, List, TooltipFlag)
     */
    Event<Item> ITEM = EventFactory.createLoop();
    /**
     * This is not invoked on Forge due to fundamental code differences!
     *
     * @see RenderVanilla#renderTooltip(PoseStack, List, int, int)
     */
    Event<RenderVanilla> RENDER_VANILLA_PRE = EventFactory.createInteractionResult();
    /**
     * This is not invoked on Forge due to fundamental code differences!
     *
     * @see RenderForge#renderTooltip(PoseStack, List, int, int)
     */
    Event<RenderForge> RENDER_FORGE_PRE = EventFactory.createInteractionResult();
    /**
     * @see RenderModifyPosition#renderTooltip(PoseStack, PositionContext)
     */
    Event<RenderModifyPosition> RENDER_MODIFY_POSITION = EventFactory.createLoop();
    /**
     * @see RenderModifyColor#renderTooltip(PoseStack, int, int, ColorContext)
     */
    Event<RenderModifyColor> RENDER_MODIFY_COLOR = EventFactory.createLoop();
    
    @Environment(EnvType.CLIENT)
    interface Item {
        /**
         * Called whenever a item tooltip is rendered.
         * Equal to the Forge {@code ItemTooltipEvent} event and
         * Fabric's {@code ItemTooltipCallback}.
         *
         * @param stack The rendered stack.
         * @param lines The tooltip components. Components can be added or removed.
         * @param flag A flag indicating if advanced mode is active.
         */
        void append(ItemStack stack, List<Component> lines, TooltipFlag flag);
    }
    
    @Environment(EnvType.CLIENT)
    interface RenderVanilla {
        /**
         * Called before the tooltip for a tooltip is rendered.
         *
         * This is not invoked on Forge due to fundamental differences in Forge and vanilla logic.
         *
         * @param matrices The pose stack.
         * @param texts The texts that are rendered. Can be manipulated.
         * @param x The x-coordinate of the tooltip.
         * @param y The y-coordinate of the tooltip.
         * @return Returning {@link InteractionResult#FAIL} cancels the rendering.
         */
        InteractionResult renderTooltip(PoseStack matrices, List<? extends FormattedCharSequence> texts, int x, int y);
    }
    
    @Environment(EnvType.CLIENT)
    interface RenderForge {
        /**
         * Called before the tooltip for a tooltip is rendered.
         *
         * This is not invoked on Forge due to fundamental differences in Forge and vanilla logic.
         *
         * @param matrices The pose stack.
         * @param texts The texts that are rendered. Can be manipulated.
         * @param x The x-coordinate of the tooltip.
         * @param y The y-coordinate of the tooltip.
         * @return Returning {@link InteractionResult#FAIL} cancels the rendering.
         */
        InteractionResult renderTooltip(PoseStack matrices, List<? extends FormattedText> texts, int x, int y);
    }
    
    @Environment(EnvType.CLIENT)
    interface RenderModifyPosition {
        /**
         * Event to manipulate the position of the tooltip.
         *
         * @param matrices The pose stack.
         * @param context The current position context.
         */
        void renderTooltip(PoseStack matrices, PositionContext context);
    }
    
    @Environment(EnvType.CLIENT)
    interface RenderModifyColor {
        /**
         * Event to manipulate the color of the tooltip.
         *
         * @param matrices The pose stack.
         * @param x The x-coordinate of the tooltip.
         * @param y The y-coordinate of the tooltip.
         * @param context The current color context.
         */
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
