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

package dev.architectury.test.tags;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.hooks.tags.TagHooks;
import dev.architectury.test.TestMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

public class TestTags {
    public static void initialize() {
        // This will not be present, but it should return an empty tag
        var heartParticles = TagHooks.optionalBlock(new ResourceLocation(TestMod.MOD_ID, "heart_particles"));
        // This will act like a normal tag, we have emerald block here
        var heartParticles2 = TagHooks.optionalBlock(new ResourceLocation(TestMod.MOD_ID, "heart_particles2"));
        
        BlockEvent.BREAK.register((world, pos, state, player, xp) -> {
            if (player != null && !world.isClientSide() && (state.is(heartParticles) || state.is(heartParticles2))) {
                ((ServerLevel) world).sendParticles(player, ParticleTypes.HEART, false, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.0, 0.0, 0.0, 0.0);
            }
            
            return EventResult.pass();
        });
    }
}
