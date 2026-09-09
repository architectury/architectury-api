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
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import org.jetbrains.annotations.ApiStatus;

/**
 * Events fired at fixed points of the level render, letting mods draw into the world without injecting into
 * {@link LevelRenderer#renderLevel} themselves.
 *
 * <p>Vanilla splits a frame into an <em>extraction</em> phase, which walks the level and gathers everything that will
 * be drawn into a {@link LevelRenderState}, and a <em>drawing</em> phase, which turns that state into draw calls.
 * Gather the data you need in {@link #END_EXTRACTION} and draw it in one of the stages below.
 *
 * <p>Only the positions that both NeoForge and Fabric fire at are exposed here. NeoForge's {@code AfterSky},
 * {@code AfterWeather} and {@code AfterLevel} stages, and Fabric's {@code START_MAIN}, {@code BEFORE_GIZMOS},
 * {@code BEFORE_TRANSLUCENT_TERRAIN} and block outline events, have no counterpart on the other loader and are not
 * mirrored; use the loader's own event for those.
 */
public interface ClientLevelRenderEvent {
    /**
     * @see Extraction#extract(ExtractionContext)
     */
    Event<Extraction> END_EXTRACTION = EventFactory.createLoop();
    /**
     * @see TerrainStage#render(RenderContext)
     */
    Event<TerrainStage> AFTER_OPAQUE_BLOCKS = EventFactory.createLoop();
    /**
     * @see Submit#collectSubmits(SubmitContext)
     */
    Event<Submit> COLLECT_SUBMITS = EventFactory.createLoop();
    /**
     * @see Stage#render(StageContext)
     */
    Event<Stage> AFTER_OPAQUE_FEATURES = EventFactory.createLoop();
    /**
     * @see Stage#render(StageContext)
     */
    Event<Stage> AFTER_TRANSLUCENT_FEATURES = EventFactory.createLoop();
    /**
     * @see Stage#render(StageContext)
     */
    Event<Stage> AFTER_TRANSLUCENT_BLOCKS = EventFactory.createLoop();
    /**
     * @see Stage#render(StageContext)
     */
    Event<Stage> AFTER_TRANSLUCENT_PARTICLES = EventFactory.createLoop();
    
    interface Extraction {
        /**
         * Invoked at the end of the extraction phase, after every vanilla render state has been extracted and before
         * anything is drawn. Prepare the data you need here, then draw it in one of the stages.
         *
         * <p>Equivalent to NeoForge's {@code ExtractLevelRenderStateEvent} and Fabric's
         * {@code LevelRenderEvents.END_EXTRACTION}.
         *
         * @param context The extraction context for this frame.
         */
        void extract(ExtractionContext context);
    }
    
    interface TerrainStage {
        /**
         * Invoked after solid and cutout chunk geometry has been drawn, before anything is submitted by entities,
         * block entities or particles. Use this to draw extra terrain-like opaque geometry.
         *
         * <p>Neither loader has a pose stack in hand at this point, which is why this stage passes the smaller
         * {@link RenderContext}; set up your own {@link PoseStack} if you need one.
         *
         * <p>Equivalent to NeoForge's {@code RenderLevelStageEvent.AfterOpaqueBlocks} and Fabric's
         * {@code LevelRenderEvents.AFTER_OPAQUE_TERRAIN}.
         *
         * @param context The render context for this frame.
         */
        void render(RenderContext context);
    }
    
    interface Submit {
        /**
         * Invoked after entities, block entities and particles have added their submit nodes and before any of that
         * geometry is drawn. Add your own geometry to {@link SubmitContext#submitNodeCollector()} here rather than
         * issuing draw calls directly, so it is batched and sorted along with everything else.
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
         * Invoked at one of the later drawing stages of the main render pass, in the order the constants are declared
         * above:
         *
         * <ul>
         * <li>{@link #AFTER_OPAQUE_FEATURES} after the solid geometry submitted by entities, block entities and
         * particles has been drawn.
         * <li>{@link #AFTER_TRANSLUCENT_FEATURES} after the translucent geometry submitted by entities and block
         * entities has been drawn. Translucent particles are drawn much later and are not included.
         * <li>{@link #AFTER_TRANSLUCENT_BLOCKS} after translucent chunk geometry has been drawn. Use this to draw
         * extra terrain-like translucent geometry.
         * <li>{@link #AFTER_TRANSLUCENT_PARTICLES} after translucent particles have been drawn, at the very end of the
         * main pass and before clouds, weather and late debug are drawn.
         * </ul>
         *
         * <p>Equivalent to NeoForge's {@code RenderLevelStageEvent} sub-events and Fabric's {@code LevelRenderEvents}
         * drawing events.
         *
         * @param context The render context for this frame.
         */
        void render(StageContext context);
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
     * A platform-specific bridge over the state available while drawing.
     */
    @ApiStatus.NonExtendable
    interface StageContext extends RenderContext {
        /**
         * {@return the pose stack to draw with}
         */
        PoseStack poseStack();
    }
    
    /**
     * A platform-specific bridge over the state available while submit nodes are being collected.
     */
    @ApiStatus.NonExtendable
    interface SubmitContext extends StageContext {
        /**
         * {@return the collector to submit custom geometry to}
         */
        SubmitNodeCollector submitNodeCollector();
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
}
