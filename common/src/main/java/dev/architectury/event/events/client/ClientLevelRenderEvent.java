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

package dev.architectury.event.events.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.renderpearl.api.commands.RenderPass;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.oit.OitStage;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Events fired at fixed points of the level render, letting mods draw into the world without injecting into
 * {@link LevelRenderer#renderLevel} themselves.
 *
 * <p>Vanilla splits a frame into three phases:
 *
 * <ol>
 * <li><em>Extraction</em> walks the level and gathers everything that will be drawn into a {@link LevelRenderState}.
 * Hook it with {@link #END_EXTRACTION}.
 * <li><em>Submission</em> turns that state into submit nodes, using a {@link PoseStack} to position them. This is the
 * only phase that still has a pose stack. Add your own geometry in {@link #COLLECT_SUBMITS}.
 * <li><em>Drawing</em> executes the submit nodes and chunk geometry inside a {@link RenderPass}. The drawing stages
 * hand you that render pass, not a pose stack.
 * </ol>
 *
 * <p>Only the positions that both NeoForge and Fabric fire at are exposed here. Loader-specific stages, such as
 * Fabric's {@code START_MAIN}, {@code BEFORE_GIZMOS}, {@code BEFORE_TRANSLUCENT_TERRAIN} and block outline events,
 * have no counterpart on the other loader and are not mirrored; use the loader's own event for those.
 */
public interface ClientLevelRenderEvent {
    /**
     * @see Extraction#extract(ExtractionContext)
     */
    Event<Extraction> END_EXTRACTION = EventFactory.createLoop();
    /**
     * @see Submit#collectSubmits(SubmitContext)
     */
    Event<Submit> COLLECT_SUBMITS = EventFactory.createLoop();
    /**
     * Invoked after opaque chunk geometry has been drawn and before the solid geometry submitted by entities, block
     * entities and particles. Use this to draw extra terrain-like opaque geometry.
     *
     * <p>Equivalent to NeoForge's {@code RenderLevelStageEvent.AfterOpaqueBlocks} and Fabric's
     * {@code LevelRenderEvents.AFTER_OPAQUE_TERRAIN}.
     *
     * @see Stage#render(StageContext)
     */
    Event<Stage> AFTER_OPAQUE_BLOCKS = EventFactory.createLoop();
    /**
     * Invoked after the solid geometry submitted by entities, block entities and particles has been drawn.
     *
     * <p>Equivalent to NeoForge's {@code RenderLevelStageEvent.AfterOpaqueFeatures} and Fabric's
     * {@code LevelRenderEvents.AFTER_SOLID_FEATURES}.
     *
     * @see Stage#render(StageContext)
     */
    Event<Stage> AFTER_OPAQUE_FEATURES = EventFactory.createLoop();
    /**
     * Invoked after the translucent geometry submitted by entities and block entities has been drawn. Translucent
     * particles are drawn much later and are not included.
     *
     * <p>Equivalent to NeoForge's {@code RenderLevelStageEvent.AfterTranslucentFeatures} and Fabric's
     * {@code LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES}.
     *
     * @see TranslucentStage#render(TranslucentStageContext)
     */
    Event<TranslucentStage> AFTER_TRANSLUCENT_FEATURES = EventFactory.createLoop();
    /**
     * Invoked after translucent chunk geometry has been drawn and before translucent particles. Use this to draw extra
     * terrain-like translucent geometry.
     *
     * <p>Equivalent to NeoForge's {@code RenderLevelStageEvent.AfterTranslucentBlocks} and Fabric's
     * {@code LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN}.
     *
     * @see TranslucentStage#render(TranslucentStageContext)
     */
    Event<TranslucentStage> AFTER_TRANSLUCENT_BLOCKS = EventFactory.createLoop();
    /**
     * Invoked at the end of the main render pass, just before it is closed.
     *
     * <p>When improved transparency is disabled, this runs after translucent terrain, features and particles, clouds,
     * weather and the world border have been drawn. When it is enabled, those are drawn after this event, in the
     * order-independent transparency stages.
     *
     * <p>Equivalent to NeoForge's {@code RenderLevelStageEvent.AfterTranslucentParticles} and Fabric's
     * {@code LevelRenderEvents.END_MAIN}.
     *
     * @see Stage#render(StageContext)
     */
    Event<Stage> AFTER_TRANSLUCENT_PARTICLES = EventFactory.createLoop();

    interface Extraction {
        /**
         * Invoked at the end of the extraction phase, after every vanilla render state has been extracted and before
         * anything is drawn. Prepare the data you need here, then draw it in a later phase.
         *
         * <p>Equivalent to NeoForge's {@code ExtractLevelRenderStateEvent} and Fabric's
         * {@code LevelExtractionEvents.END_EXTRACTION}.
         *
         * @param context The extraction context for this frame.
         */
        void extract(ExtractionContext context);
    }

    interface Submit {
        /**
         * Invoked after entities, block entities, particles and gizmos have added their submit nodes and before any of
         * that geometry is drawn. Add your own geometry to {@link SubmitContext#submitNodeCollector()} here rather than
         * drawing it directly, so it is batched and sorted along with everything else.
         *
         * <p>Equivalent to NeoForge's {@code SubmitCustomGeometryEvent} and Fabric's
         * {@code LevelRenderEvents.COLLECT_SUBMITS}.
         *
         * @param context The submit context for this frame.
         */
        void collectSubmits(SubmitContext context);
    }

    interface Stage {
        /**
         * Invoked at one of the drawing stages. See each event constant for where it fires.
         *
         * @param context The render context for this stage.
         */
        void render(StageContext context);
    }

    interface TranslucentStage {
        /**
         * Invoked at one of the translucent drawing stages. See each event constant for where it fires.
         *
         * <p>When improved transparency is enabled, this is invoked once for each order-independent transparency
         * stage, and {@link TranslucentStageContext#oitStage()} returns the current one. Otherwise it is invoked once
         * per frame and {@code oitStage()} returns {@code null}.
         *
         * @param context The render context for this stage.
         */
        void render(TranslucentStageContext context);
    }

    /**
     * A platform-specific bridge over the state shared by every level render event.
     */
    @ApiStatus.NonExtendable
    interface RenderContext {
        /**
         * {@return the level renderer drawing this frame}
         */
        LevelRenderer levelRenderer();

        /**
         * {@return the render state extracted for this frame}
         */
        LevelRenderState levelRenderState();
    }

    /**
     * A platform-specific bridge over the state available during the extraction phase.
     */
    @ApiStatus.NonExtendable
    interface ExtractionContext extends RenderContext {
        /**
         * {@return the level being rendered}
         */
        ClientLevel level();

        /**
         * {@return the camera this frame is rendered from}
         */
        Camera camera();

        /**
         * {@return the delta tracker for this frame}
         */
        DeltaTracker deltaTracker();
    }

    /**
     * A platform-specific bridge over the state available while submit nodes are being collected.
     */
    @ApiStatus.NonExtendable
    interface SubmitContext extends RenderContext {
        /**
         * {@return the pose stack to position submitted geometry with}
         */
        PoseStack poseStack();

        /**
         * {@return the collector to submit custom geometry to}
         */
        SubmitNodeCollector submitNodeCollector();
    }

    /**
     * A platform-specific bridge over the state available while drawing.
     */
    @ApiStatus.NonExtendable
    interface StageContext extends RenderContext {
        /**
         * {@return the render pass that is open at this stage}
         */
        RenderPass renderPass();
    }

    /**
     * A platform-specific bridge over the state available while drawing translucent geometry.
     */
    @ApiStatus.NonExtendable
    interface TranslucentStageContext extends StageContext {
        /**
         * {@return the current order-independent transparency stage, or {@code null} if improved transparency is
         * disabled}
         */
        @Nullable
        OitStage oitStage();
    }
}
