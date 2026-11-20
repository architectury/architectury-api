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

package dev.architectury.attachment;

import com.mojang.serialization.Codec;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Creates and registers {@link AttachmentType attachment types}.
 * <p>
 * Attachment types must be registered during mod initialization, on both the client and the server.
 * The returned type can be used as soon as it is returned.
 * <p>
 * On Fabric this delegates to the Fabric Data Attachment API. On NeoForge the type is built immediately
 * and registered into {@code NeoForgeRegistries.ATTACHMENT_TYPES} during the registration phase.
 */
public final class AttachmentRegistry {
    private AttachmentRegistry() {
    }

    /**
     * Registers an attachment type configured by the consumer.
     *
     * @param id       the unique identifier of the attachment type
     * @param consumer configures the attachment type
     * @param <T>      the type of the attached data
     * @return the registered attachment type
     */
    public static <T> AttachmentType<T> register(Identifier id, Consumer<AttachmentType.Builder<T>> consumer) {
        AttachmentType.Builder<T> builder = new AttachmentType.Builder<>();
        consumer.accept(builder);
        return register(id, builder);
    }

    /**
     * Registers an attachment type that is not saved, not synced, and has no default value.
     *
     * @param id  the unique identifier of the attachment type
     * @param <T> the type of the attached data
     * @return the registered attachment type
     */
    public static <T> AttachmentType<T> register(Identifier id) {
        return register(id, new AttachmentType.Builder<>());
    }

    /**
     * Registers an attachment type that is not saved and not synced, with a default value.
     *
     * @param id          the unique identifier of the attachment type
     * @param initializer creates the default value
     * @param <T>         the type of the attached data
     * @return the registered attachment type
     */
    public static <T> AttachmentType<T> registerDefaulted(Identifier id, Supplier<T> initializer) {
        return register(id, new AttachmentType.Builder<T>().initializer(initializer));
    }

    /**
     * Registers an attachment type that is saved with its holder, has no default value and is not synced.
     *
     * @param id    the unique identifier of the attachment type
     * @param codec the codec used to save the attached value
     * @param <T>   the type of the attached data
     * @return the registered attachment type
     */
    public static <T> AttachmentType<T> registerPersistent(Identifier id, Codec<T> codec) {
        return register(id, new AttachmentType.Builder<T>().persistent(codec));
    }

    /**
     * Registers an attachment type from a builder.
     *
     * @param id      the unique identifier of the attachment type
     * @param builder the options of the attachment type
     * @param <T>     the type of the attached data
     * @return the registered attachment type
     */
    @ExpectPlatform
    public static <T> AttachmentType<T> register(Identifier id, AttachmentType.Builder<T> builder) {
        throw new AssertionError();
    }
}
