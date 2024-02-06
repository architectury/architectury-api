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

package dev.architectury.test.registry.objects;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ItemWithTooltip extends Item {
    public ItemWithTooltip(Properties properties) {
        super(properties);
    }
    
    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        return Optional.of(new MyTooltipComponent(itemStack.getCount()));
    }
    
    public record MyTooltipComponent(int count) implements TooltipComponent {
    
    }
    
    @Environment(EnvType.CLIENT)
    public record MyClientTooltipComponent(MyTooltipComponent component) implements ClientTooltipComponent {
        @Override
        public int getHeight() {
            return 100;
        }
        
        @Override
        public int getWidth(Font font) {
            return 100;
        }
        
        @Override
        public void renderImage(Font font, int x, int y, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset) {
            poseStack.pushPose();
            poseStack.translate(0, 0, blitOffset);
            font.draw(poseStack, "Count: " + component.count, x + getWidth(font) / 2, y + (getHeight() - font.lineHeight) / 2, 0xFF00FF00);
            poseStack.popPose();
        }
    }
}
