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

package dev.architectury.event.fabric;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.events.client.*;
import dev.architectury.event.events.common.*;
import dev.architectury.utils.ArchitecturyConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public final class ClientEventHandlerImpl {
    private ClientEventHandlerImpl() {
    }
    
    public static void registerClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(instance -> ClientLifecycleEvent.CLIENT_STARTED.invoker().stateChanged(instance));
        ClientLifecycleEvents.CLIENT_STOPPING.register(instance -> ClientLifecycleEvent.CLIENT_STOPPING.invoker().stateChanged(instance));
        
        ClientTickEvents.START_CLIENT_TICK.register(instance -> ClientTickEvent.CLIENT_PRE.invoker().tick(instance));
        ClientTickEvents.END_CLIENT_TICK.register(instance -> ClientTickEvent.CLIENT_POST.invoker().tick(instance));
        ClientTickEvents.START_LEVEL_TICK.register(instance -> ClientTickEvent.CLIENT_LEVEL_PRE.invoker().tick(instance));
        ClientTickEvents.END_LEVEL_TICK.register(instance -> ClientTickEvent.CLIENT_LEVEL_POST.invoker().tick(instance));
        
        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipFlag, list) -> ClientTooltipEvent.ITEM.invoker().append(itemStack, list, tooltipContext, tooltipFlag));
        
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(ArchitecturyConstants.MOD_ID, "render_hud"),
                (graphics, tickDelta) -> ClientGuiEvent.RENDER_HUD.invoker().renderHud(graphics, tickDelta));
        
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) ->
                ClientCommandRegistrationEvent.EVENT.invoker().register((CommandDispatcher<ClientCommandRegistrationEvent.ClientCommandSourceStack>)
                        (CommandDispatcher<?>) dispatcher, access));
        
        ScreenEvents.AFTER_INIT.register((minecraft, screen, scaledWidth, scaledHeight) -> {
            ScreenKeyboardEvents.allowCharType(screen).register((parent, event) ->
                    ClientScreenInputEvent.CHAR_TYPED_PRE.invoker().charTyped(minecraft, parent, event).isEmpty());
            ScreenKeyboardEvents.afterCharType(screen).register((parent, event) ->
                    ClientScreenInputEvent.CHAR_TYPED_POST.invoker().charTyped(minecraft, parent, event));
            ScreenEvents.afterBackground(screen).register((parent, graphics, mouseX, mouseY, tickProgress) ->
                    ClientGuiEvent.RENDER_BACKGROUND.invoker().render(parent, graphics, mouseX, mouseY, tickProgress));
            ScreenEvents.remove(screen).register(parent ->
                    ClientGuiEvent.SCREEN_CLOSING.invoker().closing(parent));
        });

        ClientChunkEvents.CHUNK_LOAD.register((level, chunk) -> ChunkEvent.LOAD.invoker().load(chunk, level, false));
        ClientChunkEvents.CHUNK_UNLOAD.register((level, chunk) -> ChunkEvent.UNLOAD.invoker().unload(chunk, level));

        ClientEntityEvents.ENTITY_UNLOAD.register((entity, level) -> EntityEvent.REMOVE.invoker().remove(entity, level));
        
        ClientPreAttackCallback.EVENT.register((client, player, clickCount) ->
                InteractionEvent.CLIENT_PRE_ATTACK.invoker().preAttack(player, clickCount).isFalse());
        
        LevelRenderEvents.END_EXTRACTION.register(context ->
                ClientLevelRenderEvent.END_EXTRACTION.invoker().extract(new LevelExtractionContextImpl(context)));
        LevelRenderEvents.AFTER_OPAQUE_TERRAIN.register(context ->
                ClientLevelRenderEvent.AFTER_OPAQUE_BLOCKS.invoker().render(new LevelRenderContextImpl(context)));
        LevelRenderEvents.COLLECT_SUBMITS.register(context ->
                ClientLevelRenderEvent.COLLECT_SUBMITS.invoker().collectSubmits(new LevelSubmitContextImpl(context)));
        LevelRenderEvents.AFTER_SOLID_FEATURES.register(context ->
                ClientLevelRenderEvent.AFTER_OPAQUE_FEATURES.invoker().render(new LevelSubmitContextImpl(context)));
        LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES.register(context ->
                ClientLevelRenderEvent.AFTER_TRANSLUCENT_FEATURES.invoker().render(new LevelSubmitContextImpl(context)));
        LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register(context ->
                ClientLevelRenderEvent.AFTER_TRANSLUCENT_BLOCKS.invoker().render(new LevelSubmitContextImpl(context)));
        LevelRenderEvents.END_MAIN.register(context ->
                ClientLevelRenderEvent.AFTER_TRANSLUCENT_PARTICLES.invoker().render(new LevelSubmitContextImpl(context)));
    }
}
