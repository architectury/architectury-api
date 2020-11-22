/*
 * This file is part of architectury.
 * Copyright (C) 2020 shedaniel
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

package me.shedaniel.architectury.utils;

import me.shedaniel.architectury.platform.Platform;
import net.fabricmc.api.EnvType;

import java.util.Optional;
import java.util.function.Supplier;

public final class EnvExecutor {
    public static void runInEnv(EnvType type, Supplier<Runnable> runnableSupplier) {
        if (Platform.getEnv() == type) {
            runnableSupplier.get().run();
        }
    }
    
    public static <T> Optional<T> getInEnv(EnvType type, Supplier<Supplier<T>> runnableSupplier) {
        if (Platform.getEnv() == type) {
            return Optional.ofNullable(runnableSupplier.get().get());
        }
        
        return Optional.empty();
    }
    
    public static <T> T getEnvSpecific(Supplier<Supplier<T>> client, Supplier<Supplier<T>> server) {
        if (Platform.getEnv() == EnvType.CLIENT) {
            return client.get().get();
        } else {
            return server.get().get();
        }
    }
    
    private EnvExecutor() {}
}
