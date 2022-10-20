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
import dev.architectury.event.events.common.*;
import dev.architectury.hooks.level.ExplosionHooks;
import dev.architectury.platform.Platform;
import dev.architectury.test.TestMod;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class DebugEvents {
    public static void initialize() {
        debugEvents();
        if (Platform.getEnvironment() == Env.CLIENT)
            debugEventsClient();
    }
    
    public static void debugEvents() {
        BlockEvent.BREAK.register((world, pos, state, player, xp) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " breaks " + toShortString(pos) + logSide(player.level));
            return EventResult.pass();
        });
        BlockEvent.PLACE.register((world, pos, state, placer) -> {
            TestMod.SINK.accept(Optional.ofNullable(placer).map(Entity::getScoreboardName).orElse("null") + " places block at " + toShortString(pos) + logSide(world));
            return EventResult.pass();
        });
        ChatEvent.DECORATE.register((player, component) -> {
            component.set(component.get().copy().withStyle(ChatFormatting.AQUA).append(" + new text"));
        });
        ChatEvent.RECEIVED.register((player, message) -> {
            TestMod.SINK.accept("Server chat received: " + message);
            if (message.getString().contains("shit")) {
                return EventResult.interruptFalse();
            }
            return EventResult.interruptTrue();
        });
        CommandPerformEvent.EVENT.register(event -> {
            TestMod.SINK.accept("Server command performed: " + event.getResults().getReader().getString());
            return EventResult.pass();
        });
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            TestMod.SINK.accept("Server commands registers");
        });
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            if (entity instanceof Player) {
                TestMod.SINK.accept(entity.getScoreboardName() + " died to " + source.getMsgId() + logSide(entity.level));
            }
            return EventResult.pass();
        });
        EntityEvent.LIVING_HURT.register((entity, source, amount) -> {
            if (source.getDirectEntity() instanceof Player) {
                TestMod.SINK.accept(source.getDirectEntity().getScoreboardName() + " deals %.2f damage" + logSide(entity.level), amount);
            }
            return EventResult.pass();
        });
        EntityEvent.ADD.register((entity, level) -> {
            if (entity instanceof Player) {
                TestMod.SINK.accept(entity.getScoreboardName() + " was added to " + level.dimension().location().toString() + logSide(level));
            }
            return EventResult.pass();
        });
        EntityEvent.ENTER_SECTION.register(((entity, nx, ny, nz, ox, oy, oz) -> {
            if (entity instanceof Player player) {
                TestMod.SINK.accept("%s switched section: %s => %s", entity.getScoreboardName(), sectionPos(ox, oy, oz), sectionPos(nx, oy, nz));
                player.displayClientMessage(Component.literal("Entering chunk: " + sectionPos(nx, ny, nz)), true);
            }
        }));
        EntityEvent.LIVING_CHECK_SPAWN.register(((entity, level, x, y, z, type, spawner) -> {
            var sb = new StringBuilder();
            sb.append(entity.getType());
            sb.append(" is trying to spawn");
            sb.append(" at ");
            sb.append(toShortString(new Vec3(x, y, z)));
            if (level instanceof Level) {
                sb.append(" in world ");
                sb.append(((Level) level).dimension().location());
            }
            sb.append(" from cause ");
            sb.append(type.name());
            if (spawner != null) {
                sb.append(" (");
                sb.append(spawner);
                sb.append(") ");
            }
            
            TestMod.SINK.accept(sb.toString());
            return EventResult.pass();
        }));
        EntityEvent.ANIMAL_TAME.register(((animal, player) -> {
            TestMod.SINK.accept("%s tamed %s at %s", player.getScoreboardName(), animal.getDisplayName().getString(), toShortString(animal.position()));
            return EventResult.pass();
        }));
        ExplosionEvent.DETONATE.register((world, explosion, affectedEntities) -> {
            TestMod.SINK.accept(world.dimension().location() + " explodes at " + toShortString(ExplosionHooks.getPosition(explosion)) + logSide(world));
        });
        InteractionEvent.LEFT_CLICK_BLOCK.register((player, hand, pos, face) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " left clicks " + toShortString(pos) + logSide(player.level));
            return EventResult.pass();
        });
        InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, pos, face) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " right clicks " + toShortString(pos) + logSide(player.level));
            return EventResult.pass();
        });
        InteractionEvent.RIGHT_CLICK_ITEM.register((player, hand) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " uses " + (hand == InteractionHand.MAIN_HAND ? "main hand" : "off hand") + logSide(player.level));
            return CompoundEventResult.pass();
        });
        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " interacts with " + entity.getScoreboardName() + " using " + (hand == InteractionHand.MAIN_HAND ? "main hand" : "off hand") + logSide(player.level));
            return EventResult.pass();
        });
        InteractionEvent.FARMLAND_TRAMPLE.register((level, pos, state, distance, entity) -> {
            if (entity instanceof Player && ((Player) entity).getItemBySlot(EquipmentSlot.FEET).getItem() == Items.DIAMOND_BOOTS) {
                return EventResult.interrupt(false);
            }
            TestMod.SINK.accept("%s trampled farmland (%s) at %s in %s (Fall height: %f blocks)", entity, state, pos, level, distance);
            return EventResult.pass();
        });
        LifecycleEvent.SERVER_BEFORE_START.register(instance -> {
            TestMod.SINK.accept("Server ready to start");
        });
        LifecycleEvent.SERVER_STARTING.register(instance -> {
            TestMod.SINK.accept("Server starting");
        });
        LifecycleEvent.SERVER_STARTED.register(instance -> {
            TestMod.SINK.accept("Server started");
        });
        LifecycleEvent.SERVER_STOPPING.register(instance -> {
            TestMod.SINK.accept("Server stopping");
        });
        LifecycleEvent.SERVER_STOPPED.register(instance -> {
            TestMod.SINK.accept("Server stopped");
        });
        LifecycleEvent.SERVER_LEVEL_LOAD.register(instance -> {
            TestMod.SINK.accept("Server world loaded: " + instance.dimension().location());
        });
        LifecycleEvent.SERVER_LEVEL_UNLOAD.register(instance -> {
            TestMod.SINK.accept("Server world unloaded: " + instance.dimension().location());
        });
        LifecycleEvent.SERVER_LEVEL_SAVE.register(instance -> {
            TestMod.SINK.accept("Server world saved: " + instance.dimension().location());
        });
        PlayerEvent.PLAYER_JOIN.register(player -> {
            TestMod.SINK.accept(player.getScoreboardName() + " joined" + logSide(player.level));
        });
        PlayerEvent.PLAYER_QUIT.register(player -> {
            TestMod.SINK.accept(player.getScoreboardName() + " quit" + logSide(player.level));
        });
        PlayerEvent.PLAYER_RESPAWN.register((player, conqueredEnd) -> {
            if (!conqueredEnd) {
                TestMod.SINK.accept(player.getScoreboardName() + " respawns " + logSide(player.level));
            }
        });
        PlayerEvent.PLAYER_CLONE.register((oldPlayer, newPlayer, wonGame) -> {
            TestMod.SINK.accept("Player cloned: " + newPlayer.getScoreboardName() + logSide(newPlayer.level));
        });
        PlayerEvent.PLAYER_ADVANCEMENT.register((player, advancement) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " was awarded with %s" + logSide(player.level), advancement.getChatComponent().getString());
        });
        PlayerEvent.CRAFT_ITEM.register((player, constructed, inventory) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " crafts " + Component.translatable(constructed.getDescriptionId()).getString() + logSide(player.level));
        });
        PlayerEvent.SMELT_ITEM.register((player, smelted) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " smelts " + Component.translatable(smelted.getDescriptionId()).getString() + logSide(player.level));
        });
        PlayerEvent.PICKUP_ITEM_POST.register((player, entity, stack) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " picks up " + Component.translatable(stack.getDescriptionId()).getString() + logSide(player.level));
        });
        PlayerEvent.DROP_ITEM.register((player, entity) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " drops " + Component.translatable(entity.getItem().getDescriptionId()).getString() + logSide(player.level));
            return EventResult.pass();
        });
        PlayerEvent.OPEN_MENU.register((player, menu) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " opens " + toSimpleName(menu) + logSide(player.level));
        });
        PlayerEvent.CLOSE_MENU.register((player, menu) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " closes " + toSimpleName(menu) + logSide(player.level));
        });
        PlayerEvent.CHANGE_DIMENSION.register((player, oldLevel, newLevel) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " switched from " + oldLevel.location() + " to " + newLevel.location() + logSide(player.level));
        });
        PlayerEvent.FILL_BUCKET.register(((player, level, stack, target) -> {
            TestMod.SINK.accept("%s used a bucket (%s) in %s%s while looking at %s", player.getScoreboardName(), stack, level.dimension().location(), logSide(level), target == null ? "nothing" : target.getLocation());
            return CompoundEventResult.pass();
        }));
        LightningEvent.STRIKE.register((bolt, level, pos, toStrike) -> {
            TestMod.SINK.accept(bolt.getScoreboardName() + " struck at " + toShortString(pos) + logSide(level));
        });
        ChunkEvent.LOAD_DATA.register((chunk, level, nbt) -> {
//            TestMod.SINK.accept("Chunk loaded at x=" + chunk.getPos().x + ", z=" + chunk.getPos().z + " in dimension '" + level.dimension().location() + "'");
        });
        ChunkEvent.SAVE_DATA.register((chunk, level, nbt) -> {
//            TestMod.SINK.accept("Chunk saved at x=" + chunk.getPos().x + ", z=" + chunk.getPos().z + " in dimension '" + level.dimension().location() + "'");
        });
    }
    
    public static String toShortString(Vec3i pos) {
        return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }
    
    public static String toShortString(Position pos) {
        return pos.x() + ", " + pos.y() + ", " + pos.z();
    }
    
    public static String logSide(Level level) {
        if (level.isClientSide())
            return " (client)";
        return " (server)";
    }
    
    @Environment(EnvType.CLIENT)
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
        ClientLifecycleEvent.CLIENT_LEVEL_LOAD.register(world -> {
            TestMod.SINK.accept("Client world loaded: " + world.dimension().location().toString());
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
        InteractionEvent.CLIENT_LEFT_CLICK_AIR.register((player, hand) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " left clicks air" + logSide(player.level));
        });
        InteractionEvent.CLIENT_RIGHT_CLICK_AIR.register((player, hand) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " right clicks air" + logSide(player.level));
        });
        ClientRecipeUpdateEvent.EVENT.register(recipeManager -> {
            TestMod.SINK.accept("Client recipes received");
        });
//        ClientTextureStitchEvent.POST.register(atlas -> {
//            TestMod.SINK.accept("Client texture stitched: " + atlas.location());
//        });
        ClientScreenInputEvent.MOUSE_SCROLLED_PRE.register((client, screen, mouseX, mouseY, amount) -> {
            TestMod.SINK.accept("Screen Mouse scrolled: %.2f distance", amount);
            return EventResult.pass();
        });
        ClientScreenInputEvent.MOUSE_CLICKED_PRE.register((client, screen, mouseX, mouseY, button) -> {
            TestMod.SINK.accept("Screen Mouse clicked: " + button);
            return EventResult.pass();
        });
        ClientScreenInputEvent.MOUSE_RELEASED_PRE.register((client, screen, mouseX, mouseY, button) -> {
            TestMod.SINK.accept("Screen Mouse released: " + button);
            return EventResult.pass();
        });
        ClientScreenInputEvent.MOUSE_DRAGGED_PRE.register((client, screen, mouseX1, mouseY1, button, mouseX2, mouseY2) -> {
            TestMod.SINK.accept("Screen Mouse dragged: %d (%d,%d) by (%d,%d)", button, (int) mouseX1, (int) mouseY1, (int) mouseX2, (int) mouseY2);
            return EventResult.pass();
        });
        ClientScreenInputEvent.CHAR_TYPED_PRE.register((client, screen, character, keyCode) -> {
            TestMod.SINK.accept("Screen Char typed: " + character);
            return EventResult.pass();
        });
        ClientScreenInputEvent.KEY_PRESSED_PRE.register((client, screen, keyCode, scanCode, modifiers) -> {
            TestMod.SINK.accept("Screen Key pressed: " + InputConstants.getKey(keyCode, scanCode).getDisplayName().getString());
            return EventResult.pass();
        });
        ClientScreenInputEvent.KEY_RELEASED_PRE.register((client, screen, keyCode, scanCode, modifiers) -> {
            TestMod.SINK.accept("Screen Key released: " + InputConstants.getKey(keyCode, scanCode).getDisplayName().getString());
            return EventResult.pass();
        });
        ClientRawInputEvent.MOUSE_SCROLLED.register((client, amount) -> {
            TestMod.SINK.accept("Raw Mouse scrolled: %.2f distance", amount);
            return EventResult.pass();
        });
        ClientRawInputEvent.MOUSE_CLICKED_PRE.register((client, button, action, mods) -> {
            TestMod.SINK.accept("Raw Mouse clicked: " + button);
            return EventResult.pass();
        });
        ClientRawInputEvent.KEY_PRESSED.register((client, keyCode, scanCode, action, modifiers) -> {
            TestMod.SINK.accept("Raw Key pressed: " + InputConstants.getKey(keyCode, scanCode).getDisplayName().getString());
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
    
    private static String chunkPos(int x, int z) {
        return "[" + x + ", " + z + "]";
    }
    
    private static String sectionPos(int x, int y, int z) {
        return "[" + x + ", " + y + ", " + z + "]";
    }
    
    private static String toSimpleName(Object o) {
        return o == null ? "null" : o.getClass().getSimpleName() + "@" + Integer.toHexString(o.hashCode());
    }
}
