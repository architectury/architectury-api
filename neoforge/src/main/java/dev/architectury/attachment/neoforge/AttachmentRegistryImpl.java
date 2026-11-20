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

package dev.architectury.attachment.neoforge;

import dev.architectury.attachment.AttachmentSyncPredicate;
import dev.architectury.attachment.AttachmentType;
import dev.architectury.extensions.injected.InjectedAttachmentHolderExtension;
import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.utils.ArchitecturyConstants;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AttachmentRegistryImpl {
    private static final Map<Identifier, net.neoforged.neoforge.attachment.AttachmentType<?>> TYPES = new LinkedHashMap<>();

    public static <T> AttachmentType<T> register(Identifier id, AttachmentType.Builder<T> builder) {
        Supplier<T> initializer = builder.getInitializer();
        Supplier<T> defaultValue = initializer != null ? initializer : () -> {
            throw new IllegalStateException("Attachment " + id + " has no default value");
        };
        net.neoforged.neoforge.attachment.AttachmentType.Builder<T> neoBuilder = net.neoforged.neoforge.attachment.AttachmentType.builder(defaultValue);
        if (builder.getCodec() != null) {
            neoBuilder.serialize(builder.getCodec().fieldOf("value"));
        }
        if (builder.isCopyOnDeath()) {
            neoBuilder.copyOnDeath();
        }
        if (builder.getSyncCodec() != null) {
            AttachmentSyncPredicate predicate = builder.getSyncPredicate();
            neoBuilder.sync((holder, player) -> predicate.test((InjectedAttachmentHolderExtension) holder, player), builder.getSyncCodec());
        }
        net.neoforged.neoforge.attachment.AttachmentType<T> type = neoBuilder.build();
        if (TYPES.putIfAbsent(id, type) != null) {
            throw new IllegalArgumentException("Attachment type " + id + " is already registered");
        }
        return new NeoForgeAttachmentType<>(id, type, initializer, builder.getCodec() != null, builder.getSyncCodec() != null, builder.isCopyOnDeath());
    }

    static {
        EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> bus.register(AttachmentRegistryImpl.class));
    }

    @SubscribeEvent
    public static void event(RegisterEvent event) {
        event.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, helper -> {
            for (Map.Entry<Identifier, net.neoforged.neoforge.attachment.AttachmentType<?>> entry : TYPES.entrySet()) {
                helper.register(entry.getKey(), entry.getValue());
            }
        });
    }
}
