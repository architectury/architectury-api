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

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.*;
import dev.architectury.platform.Platform;
import dev.architectury.test.TestMod;
import dev.architectury.utils.Env;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
            ClientDebugEvents.debugEventsClient();
    }
    
    public static void debugEvents() {
        BlockEvent.BREAK.register((world, pos, state, player, xp) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " breaks " + toShortString(pos) + logSide(player.level()));
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
            if (event.getResults().getReader().getString().startsWith("help")) {
                TestMod.SINK.accept("Cancelling help command as a test!");
                return EventResult.interruptFalse();
            }
            
            TestMod.SINK.accept("Server command performed: " + event.getResults().getReader().getString());
            return EventResult.pass();
        });
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            TestMod.SINK.accept("Server commands registers");
        });
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            if (entity instanceof Player) {
                TestMod.SINK.accept(entity.getScoreboardName() + " died to " + source.getMsgId() + logSide(entity.level()));
            }
            return EventResult.pass();
        });
        EntityEvent.LIVING_HURT.register((entity, source, amount) -> {
            if (source.getDirectEntity() instanceof Player) {
                TestMod.SINK.accept(source.getDirectEntity().getScoreboardName() + " deals %.2f damage" + logSide(entity.level()), amount);
            }
            return EventResult.pass();
        });
        EntityEvent.ADD.register((entity, level) -> {
            if (entity instanceof Player) {
                TestMod.SINK.accept(entity.getScoreboardName() + " was added to " + level.dimension().identifier().toString() + logSide(level));
            }
            return EventResult.pass();
        });
        EntityEvent.ENTER_SECTION.register(((entity, nx, ny, nz, ox, oy, oz) -> {
            if (entity instanceof Player player) {
                TestMod.SINK.accept("%s switched section: %s => %s", entity.getScoreboardName(), sectionPos(ox, oy, oz), sectionPos(nx, oy, nz));
                player.sendOverlayMessage(Component.literal("Entering chunk: " + sectionPos(nx, ny, nz)));
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
                sb.append(((Level) level).dimension().identifier());
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
            TestMod.SINK.accept(world.dimension().identifier() + " explodes at " + toShortString(explosion.center()) + logSide(world));
        });
        InteractionEvent.LEFT_CLICK_BLOCK.register((player, hand, pos, face) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " left clicks " + toShortString(pos) + logSide(player.level()));
            return InteractionResult.PASS;
        });
        InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, pos, face) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " right clicks " + toShortString(pos) + logSide(player.level()));
            return InteractionResult.PASS;
        });
        InteractionEvent.RIGHT_CLICK_ITEM.register((player, hand) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " uses " + (hand == InteractionHand.MAIN_HAND ? "main hand" : "off hand") + logSide(player.level()));
            return InteractionResult.PASS;
        });
        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " interacts with " + entity.getScoreboardName() + " using " + (hand == InteractionHand.MAIN_HAND ? "main hand" : "off hand") + logSide(player.level()));
            return EventResult.pass();
        });
        InteractionEvent.FARMLAND_TRAMPLE.register((level, pos, state, distance, entity) -> {
            if (entity instanceof Player && ((Player) entity).getItemBySlot(EquipmentSlot.FEET).getItem() == Items.DIAMOND_BOOTS) {
                return InteractionResult.FAIL;
            }
            TestMod.SINK.accept("%s trampled farmland (%s) at %s in %s (Fall height: %f blocks)", entity, state, pos, level, distance);
            return InteractionResult.PASS;
        });
        InteractionEvent.USE_ITEM_ON_BLOCK.register((level, player, hand, stack, state, hitResult) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " uses " + stack.getItem() + " on block " + toShortString(hitResult.getBlockPos()) + logSide(level));
            return InteractionResult.PASS;
        });
        InteractionEvent.USE_BLOCK_WITHOUT_ITEM.register((level, player, state, hitResult) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " uses block " + toShortString(hitResult.getBlockPos()) + " without an item" + logSide(level));
            return InteractionResult.PASS;
        });
        InteractionEvent.USE_ITEM_ON.register(context -> {
            TestMod.SINK.accept(context.getItemInHand().getItem() + " is used on " + toShortString(context.getClickedPos()) + logSide(context.getLevel()));
            return InteractionResult.PASS;
        });
        InteractionEvent.USE_ITEM.register((level, player, hand) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " uses item " + player.getItemInHand(hand).getItem() + logSide(level));
            return InteractionResult.PASS;
        });
        InteractionEvent.PICK_ITEM_FROM_BLOCK.register((player, pos, state, includeData) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " picks item from block " + toShortString(pos) + " (data: " + includeData + ")");
            return CompoundEventResult.pass();
        });
        InteractionEvent.PICK_ITEM_FROM_ENTITY.register((player, entity, includeData) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " picks item from " + entity.getScoreboardName() + " (data: " + includeData + ")");
            return CompoundEventResult.pass();
        });
        PlayerEvent.BREAK_SPEED.register((player, state, pos, speed) -> {
            // Diamond boots double your mining speed, because why not.
            if (player.getItemBySlot(EquipmentSlot.FEET).getItem() == Items.DIAMOND_BOOTS) {
                speed.accept(speed.getAsFloat() * 2.0F);
            }
            return EventResult.pass();
        });
        EntityEvent.LIVING_FALL.register((entity, distance, damageMultiplier) -> {
            TestMod.SINK.accept("%s fell %.2f blocks (multiplier %.2f)", entity, distance.getAsDouble(), damageMultiplier.getAsFloat());
            return EventResult.pass();
        });
        EntityEvent.MOUNT.register((entity, vehicle, mounting) -> {
            TestMod.SINK.accept(entity + (mounting ? " mounts " : " dismounts ") + vehicle + logSide(entity.level()));
            return EventResult.pass();
        });
        BlockEvent.PISTON_PRE.register((level, pos, direction, extending) -> {
            TestMod.SINK.accept("Piston at %s is about to %s facing %s%s", toShortString(pos), extending ? "extend" : "retract", direction, logSide(level));
            return EventResult.pass();
        });
        BlockEvent.PISTON_POST.register((level, pos, direction, extending) -> {
            TestMod.SINK.accept("Piston at %s %s facing %s%s", toShortString(pos), extending ? "extended" : "retracted", direction, logSide(level));
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
            TestMod.SINK.accept("Server world loaded: " + instance.dimension().identifier());
        });
        LifecycleEvent.SERVER_LEVEL_UNLOAD.register(instance -> {
            TestMod.SINK.accept("Server world unloaded: " + instance.dimension().identifier());
        });
        LifecycleEvent.SERVER_LEVEL_SAVE.register(instance -> {
            TestMod.SINK.accept("Server world saved: " + instance.dimension().identifier());
        });
        PlayerEvent.PLAYER_JOIN.register(player -> {
            TestMod.SINK.accept(player.getScoreboardName() + " joined" + logSide(player.level()));
        });
        PlayerEvent.PLAYER_QUIT.register(player -> {
            TestMod.SINK.accept(player.getScoreboardName() + " quit" + logSide(player.level()));
        });
        PlayerEvent.PLAYER_RESPAWN.register((player, conqueredEnd, removalReason) -> {
            if (!conqueredEnd) {
                TestMod.SINK.accept(player.getScoreboardName() + " respawns " + logSide(player.level()));
            }
        });
        PlayerEvent.PLAYER_CLONE.register((oldPlayer, newPlayer, wonGame) -> {
            TestMod.SINK.accept("Player cloned: " + newPlayer.getScoreboardName() + logSide(newPlayer.level()));
        });
        PlayerEvent.PLAYER_ADVANCEMENT.register((player, advancement) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " was awarded with %s" + logSide(player.level()), Advancement.name(advancement));
        });
        PlayerEvent.CRAFT_ITEM.register((player, constructed, inventory) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " crafts " + Component.translatable(constructed.getItem().getDescriptionId()).getString() + logSide(player.level()));
        });
        PlayerEvent.SMELT_ITEM.register((player, smelted) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " smelts " + Component.translatable(smelted.getItem().getDescriptionId()).getString() + logSide(player.level()));
        });
        PlayerEvent.PICKUP_ITEM_POST.register((player, entity, stack) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " picks up " + Component.translatable(stack.getItem().getDescriptionId()).getString() + logSide(player.level()));
        });
        PlayerEvent.DROP_ITEM.register((player, entity) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " drops " + Component.translatable(entity.getItem().getItem().getDescriptionId()).getString() + logSide(player.level()));
            return EventResult.pass();
        });
        PlayerEvent.OPEN_MENU.register((player, menu) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " opens " + toSimpleName(menu) + logSide(player.level()));
        });
        PlayerEvent.CLOSE_MENU.register((player, menu) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " closes " + toSimpleName(menu) + logSide(player.level()));
        });
        PlayerEvent.CHANGE_DIMENSION.register((player, oldLevel, newLevel) -> {
            TestMod.SINK.accept(player.getScoreboardName() + " switched from " + oldLevel.identifier() + " to " + newLevel.identifier() + logSide(player.level()));
        });
        PlayerEvent.FILL_BUCKET.register(((player, level, stack, target) -> {
            TestMod.SINK.accept("%s used a bucket (%s) in %s%s while looking at %s", player.getScoreboardName(), stack, level.dimension().identifier(), logSide(level), target == null ? "nothing" : target.getLocation());
            return InteractionResult.PASS;
        }));
        LightningEvent.STRIKE.register((bolt, level, pos, toStrike) -> {
            TestMod.SINK.accept(bolt.getScoreboardName() + " struck at " + toShortString(pos) + logSide(level));
        });
        ChunkEvent.LOAD_DATA.register((chunk, level, data) -> {
//            TestMod.SINK.accept("Chunk loaded at x=" + chunk.getPos().x + ", z=" + chunk.getPos().z + " in dimension '" + level.dimension().location() + "'");
        });
        ChunkEvent.SAVE_DATA.register((chunk, level, data) -> {
//            TestMod.SINK.accept("Chunk saved at x=" + chunk.getPos().x + ", z=" + chunk.getPos().z + " in dimension '" + level.dimension().location() + "'");
        });
        EntityEvent.REMOVE.register((entity, level) -> {
            if (entity instanceof Player) {
                TestMod.SINK.accept(entity.getScoreboardName() + " was removed from " + level.dimension().identifier().toString() + logSide(level));
            }
        });
        EntityEvent.EQUIPMENT_CHANGE.register((entity, slot, previousStack, currentStack) -> {
            if (entity instanceof Player) {
                TestMod.SINK.accept("%s changed %s: %s => %s", entity.getScoreboardName(), slot.getName(), previousStack, currentStack);
            }
        });
        EntityEvent.START_TRACKING.register((entity, player) -> {
            if (entity instanceof Player) {
                TestMod.SINK.accept(player.getScoreboardName() + " started tracking " + entity.getScoreboardName());
            }
        });
        EntityEvent.STOP_TRACKING.register((entity, player) -> {
            if (entity instanceof Player) {
                TestMod.SINK.accept(player.getScoreboardName() + " stopped tracking " + entity.getScoreboardName());
            }
        });
        EntityEvent.LIVING_DAMAGE_POST.register((entity, source, originalDamage, appliedDamage, blocked) -> {
            if (entity instanceof Player) {
                TestMod.SINK.accept("%s took %.2f of %.2f damage from %s (blocked: %s)",
                        entity.getScoreboardName(), appliedDamage, originalDamage, source.getMsgId(), blocked);
            }
        });
        MobEffectEvent.ALLOW_ADD.register((entity, effect) -> {
            if (entity instanceof Player) {
                TestMod.SINK.accept(entity.getScoreboardName() + " is gaining effect " + effect.getEffect().getRegisteredName());
            }
            return EventResult.pass();
        });
        MobEffectEvent.AFTER_ADD.register((entity, effect) -> {
            if (entity instanceof Player) {
                TestMod.SINK.accept(entity.getScoreboardName() + " gained effect " + effect.getEffect().getRegisteredName());
            }
        });
        MobEffectEvent.ALLOW_REMOVE.register((entity, effect) -> {
            if (entity instanceof Player) {
                TestMod.SINK.accept(entity.getScoreboardName() + " is losing effect " + effect.getEffect().getRegisteredName());
            }
            return EventResult.pass();
        });
        ChunkEvent.LOAD.register((chunk, level, newChunk) -> {
//            TestMod.SINK.accept("Chunk loaded at x=" + chunk.getPos().x + ", z=" + chunk.getPos().z + " (new: " + newChunk + ")" + logSide(level));
        });
        ChunkEvent.UNLOAD.register((chunk, level) -> {
//            TestMod.SINK.accept("Chunk unloaded at x=" + chunk.getPos().x + ", z=" + chunk.getPos().z + logSide(level));
        });
        LifecycleEvent.TAGS_UPDATED.register((registries, client) -> {
            TestMod.SINK.accept("Tags updated (client: " + client + ")");
        });
        LifecycleEvent.DATAPACK_SYNC.register((player, joined) -> {
            TestMod.SINK.accept("Datapack contents synced to " + player.getScoreboardName() + " (joined: " + joined + ")");
        });
        LootEvent.REPLACE_LOOT_TABLE.register((registries, key, original) -> {
//            TestMod.SINK.accept("Loot table loading: " + key.identifier());
            return CompoundEventResult.pass();
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
    
    private static String chunkPos(int x, int z) {
        return "[" + x + ", " + z + "]";
    }
    
    private static String sectionPos(int x, int y, int z) {
        return "[" + x + ", " + y + ", " + z + "]";
    }
    
    static String toSimpleName(Object o) {
        return o == null ? "null" : o.getClass().getSimpleName() + "@" + Integer.toHexString(o.hashCode());
    }
}
