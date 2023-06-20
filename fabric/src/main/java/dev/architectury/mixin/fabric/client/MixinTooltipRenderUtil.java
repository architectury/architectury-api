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

package dev.architectury.mixin.fabric.client;

import dev.architectury.impl.TooltipEventColorContextImpl;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(TooltipRenderUtil.class)
public abstract class MixinTooltipRenderUtil {
    @ModifyConstant(method = "renderTooltipBackground", constant = @Constant(intValue = 0xf0100010))
    private static int modifyTooltipBackgroundColor(int original) {
        return TooltipEventColorContextImpl.CONTEXT.get().getBackgroundColor();
    }
    
    @ModifyConstant(method = "renderTooltipBackground", constant = @Constant(intValue = 0x505000ff))
    private static int modifyTooltipOutlineGradientTopColor(int original) {
        return TooltipEventColorContextImpl.CONTEXT.get().getOutlineGradientTopColor();
    }
    
    @ModifyConstant(method = "renderTooltipBackground", constant = @Constant(intValue = 0x5028007f))
    private static int modifyTooltipOutlineGradientBottomColor(int original) {
        return TooltipEventColorContextImpl.CONTEXT.get().getOutlineGradientBottomColor();
    }
}
