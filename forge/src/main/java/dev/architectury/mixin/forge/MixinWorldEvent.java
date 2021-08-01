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

package dev.architectury.mixin.forge;

/*
import dev.architectury.event.forge.EventHandlerImplCommon;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.world.WorldEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.lang.ref.WeakReference;

@Mixin(WorldEvent.class)
public class MixinWorldEvent implements EventHandlerImplCommon.WorldEventAttachment {
    @Unique
    private WeakReference<LevelAccessor> level;
    
    @Override
    public LevelAccessor architectury$getAttachedLevel() {
        return level == null ? null : level.get();
    }
    
    @Override
    public void architectury$attachLevel(LevelAccessor level) {
        this.level = new WeakReference<>(level);
    }
}
*/
