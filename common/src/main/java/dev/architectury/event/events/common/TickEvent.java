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

package dev.architectury.event.events.common;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public interface TickEvent<T> {
    /**
     * Invoked before a server tick is processed.
     * Equivalent to Forge's {@code ServerTickEvent} event in the START Phase.
     *
     * @see #tick(Object)
     */
    Event<Server> SERVER_PRE = EventFactory.createLoop();
    /**
     * Invoked after a server tick has been processed.
     * Equivalent to Forge's {@code ServerTickEvent} event in the END Phase.
     *
     * @see #tick(Object)
     */
    Event<Server> SERVER_POST = EventFactory.createLoop();
    /**
     * Invoked before a server level tick is processed.
     * Equivalent to Forge's {@code WorldTickEvent} event in the START Phase.
     *
     * @see #tick(Object)
     */
    Event<ServerLevelTick> SERVER_LEVEL_PRE = EventFactory.createLoop();
    /**
     * Invoked after a server level tick has been processed.
     * Equivalent to Forge's {@code WorldTickEvent} event in the END Phase.
     *
     * @see #tick(Object)
     */
    Event<ServerLevelTick> SERVER_LEVEL_POST = EventFactory.createLoop();
    /**
     * Invoked before a player tick is processed.
     * Equivalent to Forge's {@code PlayerTickEvent} event in the START Phase.
     *
     * @see #tick(Object)
     */
    Event<Player> PLAYER_PRE = EventFactory.createLoop();
    /**
     * Invoked after a player tick has been processed.
     * Equivalent to Forge's {@code PlayerTickEvent} event in the END Phase.
     *
     * @see #tick(Object)
     */
    Event<Player> PLAYER_POST = EventFactory.createLoop();
    
    /**
     * Callback method for tick events.
     *
     * @param instance The object ticking.
     */
    void tick(T instance);
    
    interface Server extends TickEvent<MinecraftServer> {
    }
    
    interface LevelTick<T extends Level> extends TickEvent<T> {
    }
    
    interface ServerLevelTick extends LevelTick<ServerLevel> {
    }
    
    interface Player extends TickEvent<net.minecraft.world.entity.player.Player> {
    }
}
