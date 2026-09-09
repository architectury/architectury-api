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

package dev.architectury.test.events;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.*;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.test.TestMod;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.network.chat.Component;

import static dev.architectury.test.events.DebugEvents.logSide;
import static dev.architectury.test.events.DebugEvents.toSimpleName;

public class ClientDebugEvents {
    public static void debugEventsClient() {
        ClientTickEvent.CLIENT_LEVEL_PRE.register(instance -> {
            try {
                // Uncomment the following line to see the profiler spike for root.tick.level.architecturyClientLevelPreTick
                //Thread.sleep(10);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
        ClientChatEvent.SEND.register((message, component) -> {
            TestMod.SINK.accept("Client chat sent: " + message);
            if (message.contains("error")) {
                return EventResult.interruptFalse();
            }
            return EventResult.pass();
        });
        ClientChatEvent.RECEIVED.register((type, message) -> {
            TestMod.SINK.accept("Client chat received: " + message.getString());
            if (message.getString().contains("terraria")) {
                return CompoundEventResult.interruptTrue(message.copy().append(" + terraria is a great game!"));
            }
            if (message.getString().contains("potato")) {
                return CompoundEventResult.interruptFalse(Component.empty());
            }
            return CompoundEventResult.pass();
        });
        ClientSystemMessageEvent.RECEIVED.register(message -> {
            TestMod.SINK.accept("Client system message received: " + message.getString());
            if (message.getString().contains("stardew")) {
                return CompoundEventResult.interruptTrue(message.copy().append(" + stardew valley is a great game!"));
            }
            return CompoundEventResult.pass();
        });
        ClientLifecycleEvent.CLIENT_LEVEL_LOAD.register(world -> {
            TestMod.SINK.accept("Client world loaded: " + world.dimension().identifier().toString());
        });
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> {
            TestMod.SINK.accept(player.getScoreboardName() + " joined (client)");
        });
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(player -> {
            if (player != null) {
                TestMod.SINK.accept(player.getScoreboardName() + " quit (client)");
            }
        });
        ClientPlayerEvent.CLIENT_PLAYER_RESPAWN.register((oldPlayer, newPlayer) -> {
            TestMod.SINK.accept(newPlayer.getScoreboardName() + " respawned (client)");
        });
        ClientGuiEvent.INIT_PRE.register((screen, access) -> {
            TestMod.SINK.accept(toSimpleName(screen) + " initializes");
            return EventResult.pass();
        });
        ClientGuiEvent.INIT_POST.register(((screen, access) -> {
            TestMod.SINK.accept(toSimpleName(screen) + " initialized");
        }));
        InteractionEvent.CLIENT_LEFT_CLICK_AIR.register((player, hand) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " left clicks air" + logSide(player.level()));
        });
        InteractionEvent.CLIENT_RIGHT_CLICK_AIR.register((player, hand) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " right clicks air" + logSide(player.level()));
        });
        InteractionEvent.CLIENT_PRE_ATTACK.register((player, clickCount) -> {
            if (clickCount != 0) {
                TestMod.SINK.accept(player.getScoreboardName() + " pre-attack (%d clicks)", clickCount);
            }
            return EventResult.pass();
        });
        ClientRecipeUpdateEvent.EVENT.register(recipeManager -> {
            TestMod.SINK.accept("Client recipes received");
        });
//        ClientTextureStitchEvent.POST.register(atlas -> {
//            TestMod.SINK.accept("Client texture stitched: " + atlas.location());
//        });
        ClientGuiEvent.SCREEN_CLOSING.register(screen -> {
            TestMod.SINK.accept("Screen closing: " + screen.getClass().getSimpleName());
        });
        ClientGuiEvent.RENDER_BACKGROUND.register((screen, graphics, mouseX, mouseY, delta) -> {
//            TestMod.SINK.accept("Screen background rendered: " + screen.getClass().getSimpleName());
        });
        // These fire every frame, so they only log when uncommented.
        ClientLevelRenderEvent.END_EXTRACTION.register(context -> {
//            TestMod.SINK.accept("Level render state extracted for " + context.level().dimension().identifier());
        });
        ClientLevelRenderEvent.AFTER_OPAQUE_BLOCKS.register(context -> {
//            TestMod.SINK.accept("After opaque blocks");
        });
        ClientLevelRenderEvent.COLLECT_SUBMITS.register(context -> {
//            TestMod.SINK.accept("Collecting submits into " + context.submitNodeCollector());
        });
        ClientLevelRenderEvent.AFTER_OPAQUE_FEATURES.register(context -> {
//            TestMod.SINK.accept("After opaque features");
        });
        ClientLevelRenderEvent.AFTER_TRANSLUCENT_FEATURES.register(context -> {
//            TestMod.SINK.accept("After translucent features");
        });
        ClientLevelRenderEvent.AFTER_TRANSLUCENT_BLOCKS.register(context -> {
//            TestMod.SINK.accept("After translucent blocks");
        });
        ClientLevelRenderEvent.AFTER_TRANSLUCENT_PARTICLES.register(context -> {
//            TestMod.SINK.accept("After translucent particles");
        });
        ClientScreenInputEvent.MOUSE_SCROLLED_PRE.register((client, screen, mouseX, mouseY, amountX, amountY) -> {
            TestMod.SINK.accept("Screen Mouse scrolled: %.2f x-distance %.2f y-distance", amountX, amountY);
            return EventResult.pass();
        });
        ClientScreenInputEvent.MOUSE_CLICKED_PRE.register((client, screen, event, doubleClick) -> {
            TestMod.SINK.accept("Screen Mouse clicked: " + event.button());
            return EventResult.pass();
        });
        ClientScreenInputEvent.MOUSE_RELEASED_PRE.register((client, screen, event) -> {
            TestMod.SINK.accept("Screen Mouse released: " + event.button());
            return EventResult.pass();
        });
        ClientScreenInputEvent.MOUSE_RELEASED_POST.register((client, screen, event) -> {
            TestMod.SINK.accept("Screen Mouse released (post): " + event.button());
            return EventResult.pass();
        });
        ClientScreenInputEvent.MOUSE_DRAGGED_PRE.register((client, screen, event, mouseX2, mouseY2) -> {
            TestMod.SINK.accept("Screen Mouse dragged: %d (%d,%d) by (%d,%d)", event.button(), (int) event.x(), (int) event.y(), (int) mouseX2, (int) mouseY2);
            return EventResult.pass();
        });
        ClientScreenInputEvent.MOUSE_DRAGGED_POST.register((client, screen, event, mouseX2, mouseY2) -> {
            TestMod.SINK.accept("Screen Mouse dragged (post): %d (%d,%d) by (%d,%d)", event.button(), (int) event.x(), (int) event.y(), (int) mouseX2, (int) mouseY2);
            return EventResult.pass();
        });
        ClientScreenInputEvent.CHAR_TYPED_PRE.register((client, screen, characterEvent) -> {
            TestMod.SINK.accept("Screen Char typed: " + characterEvent.codepointAsString());
            return EventResult.pass();
        });
        ClientScreenInputEvent.CHAR_TYPED_POST.register((client, screen, characterEvent) -> {
            TestMod.SINK.accept("Screen Char typed (post): " + characterEvent.codepointAsString());
            return EventResult.pass();
        });
        ClientScreenInputEvent.KEY_PRESSED_PRE.register((client, screen, keyEvent) -> {
            TestMod.SINK.accept("Screen Key pressed: " + InputConstants.getKey(keyEvent).getDisplayName().getString());
            return EventResult.pass();
        });
        ClientScreenInputEvent.KEY_RELEASED_PRE.register((client, screen, keyEvent) -> {
            TestMod.SINK.accept("Screen Key released: " + InputConstants.getKey(keyEvent).getDisplayName().getString());
            return EventResult.pass();
        });
        ClientRawInputEvent.MOUSE_SCROLLED.register((client, amountX, amountY) -> {
            TestMod.SINK.accept("Raw Mouse scrolled: %.2f x-distance %.2f y-distance", amountX, amountY);
            return EventResult.pass();
        });
        ClientRawInputEvent.MOUSE_CLICKED_PRE.register((client, event, action) -> {
            TestMod.SINK.accept("Raw Mouse clicked: " + event.button());
            return EventResult.pass();
        });
        ClientRawInputEvent.KEY_PRESSED.register((client, keyCode, keyEvent) -> {
            TestMod.SINK.accept("Raw Key pressed: " + InputConstants.getKey(keyEvent).getDisplayName().getString());
            return EventResult.pass();
        });
        ClientGuiEvent.SET_SCREEN.register(screen -> {
            if (screen instanceof AnvilScreen) {
                return CompoundEventResult.interruptFalse(screen);
            }
            
            TestMod.SINK.accept("Screen has been changed to " + toSimpleName(screen));
            return CompoundEventResult.pass();
        });
    }
}
