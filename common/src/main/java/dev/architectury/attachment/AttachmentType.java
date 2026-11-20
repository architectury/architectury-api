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
import dev.architectury.extensions.injected.InjectedAttachmentHolderExtension;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * A type of data that can be attached to an {@link InjectedAttachmentHolderExtension attachment holder}:
 * an {@link net.minecraft.world.entity.Entity}, a {@link net.minecraft.world.level.block.entity.BlockEntity},
 * a {@link net.minecraft.world.level.chunk.ChunkAccess} or a {@link net.minecraft.world.level.Level}.
 * <p>
 * Instances are created with {@link AttachmentRegistry}. On Fabric this wraps a Fabric Data Attachment API type,
 * and on NeoForge a NeoForge {@code AttachmentType}.
 * <p>
 * <b>Attached values should be treated as immutable.</b> Changes are only saved and synced when the value is
 * replaced through {@link #set} or {@link #modify}; mutating an attached object in place is not reliably
 * detected on every platform. Block entities and chunks are marked as changed automatically when a value is set.
 * <p>
 * Persistent data is stored in each platform's own attachment storage, so saves are not portable between loaders.
 *
 * @param <T> the type of the attached data
 */
@ApiStatus.NonExtendable
public interface AttachmentType<T> {
    /**
     * @return the unique identifier of this attachment type
     */
    Identifier id();

    /**
     * @return whether this attachment is saved with its holder
     */
    boolean isPersistent();

    /**
     * @return whether this attachment is synced to clients
     */
    boolean isSynced();

    /**
     * @return whether this attachment is kept when a player dies or an entity is converted
     */
    boolean copyOnDeath();

    /**
     * @return the default value initializer, or {@code null} if this attachment has no default value
     */
    @Nullable
    Supplier<T> initializer();

    /**
     * Returns the value attached to the holder.
     *
     * @param holder the holder
     * @return the attached value, or {@code null} if nothing is attached
     */
    @Nullable
    T get(InjectedAttachmentHolderExtension holder);

    /**
     * Returns whether a value is attached to the holder.
     *
     * @param holder the holder
     * @return whether a value is attached
     */
    boolean has(InjectedAttachmentHolderExtension holder);

    /**
     * Attaches a value to the holder, replacing any previous value.
     *
     * @param holder the holder
     * @param value  the new value, or {@code null} to remove the attachment
     * @return the previously attached value, or {@code null} if there was none
     */
    @Nullable
    T set(InjectedAttachmentHolderExtension holder, @Nullable T value);

    /**
     * Returns the value attached to the holder, attaching the {@link #initializer() default value} first if
     * nothing is attached.
     *
     * @param holder the holder
     * @return the attached value
     * @throws IllegalStateException if nothing is attached and this attachment has no default value
     */
    default T getOrCreate(InjectedAttachmentHolderExtension holder) {
        T attached = get(holder);
        if (attached != null) return attached;
        Supplier<T> initializer = initializer();
        if (initializer == null) {
            throw new IllegalStateException("Attachment " + id() + " has no default value");
        }
        T created = Objects.requireNonNull(initializer.get(), "Attachment initializer returned null");
        set(holder, created);
        return created;
    }

    /**
     * Returns the value attached to the holder, or the fallback if nothing is attached.
     * This does not attach the fallback.
     *
     * @param holder   the holder
     * @param fallback the value to return if nothing is attached
     * @return the attached value, or the fallback
     */
    @Contract("_, !null -> !null")
    default T getOrElse(InjectedAttachmentHolderExtension holder, @Nullable T fallback) {
        T attached = get(holder);
        return attached != null ? attached : fallback;
    }

    /**
     * Removes the value attached to the holder.
     *
     * @param holder the holder
     * @return the previously attached value, or {@code null} if there was none
     */
    @Nullable
    default T remove(InjectedAttachmentHolderExtension holder) {
        return set(holder, null);
    }

    /**
     * Replaces the value attached to the holder with the result of the operator.
     *
     * @param holder   the holder
     * @param operator receives the current value (or {@code null}) and returns the new value (or {@code null} to remove it)
     * @return the previously attached value, or {@code null} if there was none
     */
    @Nullable
    default T modify(InjectedAttachmentHolderExtension holder, UnaryOperator<T> operator) {
        return set(holder, operator.apply(get(holder)));
    }

    /**
     * The options of an attachment type, passed to {@link AttachmentRegistry#register(Identifier, Builder)}.
     *
     * @param <T> the type of the attached data
     */
    final class Builder<T> {
        @Nullable
        private Supplier<T> initializer;
        @Nullable
        private Codec<T> codec;
        private boolean copyOnDeath;
        @Nullable
        private StreamCodec<? super RegistryFriendlyByteBuf, T> syncCodec;
        @Nullable
        private AttachmentSyncPredicate syncPredicate;

        public Builder() {
        }

        /**
         * Sets the default value, used by {@link AttachmentType#getOrCreate}.
         *
         * @param initializer creates the default value, must not return {@code null}
         * @return this builder
         */
        public Builder<T> initializer(Supplier<T> initializer) {
            this.initializer = Objects.requireNonNull(initializer, "initializer");
            return this;
        }

        /**
         * Makes the attachment saved with its holder.
         *
         * @param codec the codec used to save the attached value
         * @return this builder
         */
        public Builder<T> persistent(Codec<T> codec) {
            this.codec = Objects.requireNonNull(codec, "codec");
            return this;
        }

        /**
         * Keeps the attachment when a player dies or an entity is converted.
         * The attachment must be {@link #persistent persistent}, so call that first.
         *
         * @return this builder
         * @throws IllegalStateException if the attachment is not persistent
         */
        public Builder<T> copyOnDeath() {
            if (this.codec == null) {
                throw new IllegalStateException("copyOnDeath requires the attachment to be persistent, call persistent(codec) first");
            }
            this.copyOnDeath = true;
            return this;
        }

        /**
         * Syncs the attachment to clients. Values are synced when they are set on the server,
         * and when a player starts tracking the holder.
         *
         * @param streamCodec the codec used to send the attached value
         * @param predicate   which players receive the value
         * @return this builder
         */
        public Builder<T> syncWith(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, AttachmentSyncPredicate predicate) {
            this.syncCodec = Objects.requireNonNull(streamCodec, "streamCodec");
            this.syncPredicate = Objects.requireNonNull(predicate, "predicate");
            return this;
        }

        @Nullable
        public Supplier<T> getInitializer() {
            return initializer;
        }

        @Nullable
        public Codec<T> getCodec() {
            return codec;
        }

        public boolean isCopyOnDeath() {
            return copyOnDeath;
        }

        @Nullable
        public StreamCodec<? super RegistryFriendlyByteBuf, T> getSyncCodec() {
            return syncCodec;
        }

        @Nullable
        public AttachmentSyncPredicate getSyncPredicate() {
            return syncPredicate;
        }
    }
}
