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
import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.events.client.*;
import dev.architectury.event.events.common.*;
import dev.architectury.impl.fabric.ChatComponentImpl;
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
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.effect.ServerMobEffectEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.BlockEvents;
import net.fabricmc.fabric.api.event.player.ItemEvents;
import net.fabricmc.fabric.api.event.player.PlayerPickItemEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

public class EventHandlerImpl {
    @Environment(EnvType.CLIENT)
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
    
    public static void registerCommon() {
        ServerLifecycleEvents.SERVER_STARTING.register(instance -> LifecycleEvent.SERVER_BEFORE_START.invoker().stateChanged(instance));
        ServerLifecycleEvents.SERVER_STARTED.register(instance -> LifecycleEvent.SERVER_STARTED.invoker().stateChanged(instance));
        ServerLifecycleEvents.SERVER_STOPPING.register(instance -> LifecycleEvent.SERVER_STOPPING.invoker().stateChanged(instance));
        ServerLifecycleEvents.SERVER_STOPPED.register(instance -> LifecycleEvent.SERVER_STOPPED.invoker().stateChanged(instance));
        
        ServerTickEvents.START_SERVER_TICK.register(instance -> TickEvent.SERVER_PRE.invoker().tick(instance));
        ServerTickEvents.END_SERVER_TICK.register(instance -> TickEvent.SERVER_POST.invoker().tick(instance));
        ServerTickEvents.START_LEVEL_TICK.register(instance -> TickEvent.SERVER_LEVEL_PRE.invoker().tick(instance));
        ServerTickEvents.END_LEVEL_TICK.register(instance -> TickEvent.SERVER_LEVEL_POST.invoker().tick(instance));
        
        ServerLevelEvents.LOAD.register((server, world) -> LifecycleEvent.SERVER_LEVEL_LOAD.invoker().act(world));
        ServerLevelEvents.UNLOAD.register((server, world) -> LifecycleEvent.SERVER_LEVEL_UNLOAD.invoker().act(world));
        
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, selection) -> CommandRegistrationEvent.EVENT.invoker().register(dispatcher, registry, selection));
        
        UseItemCallback.EVENT.register((player, world, hand) -> InteractionEvent.RIGHT_CLICK_ITEM.invoker().click(player, hand));
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> InteractionEvent.RIGHT_CLICK_BLOCK.invoker().click(player, hand, hitResult.getBlockPos(), hitResult.getDirection()));
        AttackBlockCallback.EVENT.register((player, world, hand, pos, face) -> InteractionEvent.LEFT_CLICK_BLOCK.invoker().click(player, hand, pos, face));
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> PlayerEvent.ATTACK_ENTITY.invoker().attack(player, world, entity, hand, hitResult).asMinecraft());
        
        BlockEvents.USE_ITEM_ON.register((stack, state, level, pos, player, hand, hitResult) ->
                orNull(InteractionEvent.USE_ITEM_ON_BLOCK.invoker().useItemOn(level, player, hand, stack, state, hitResult)));
        BlockEvents.USE_WITHOUT_ITEM.register((state, level, pos, player, hitResult) ->
                orNull(InteractionEvent.USE_BLOCK_WITHOUT_ITEM.invoker().useWithoutItem(level, player, state, hitResult)));
        ItemEvents.USE_ON.register(context ->
                orNull(InteractionEvent.USE_ITEM_ON.invoker().useOn(context)));
        ItemEvents.USE.register((level, player, hand) ->
                orNull(InteractionEvent.USE_ITEM.invoker().use(level, player, hand)));
        PlayerPickItemEvents.BLOCK.register((player, pos, state, includeData) -> {
            CompoundEventResult<ItemStack> result = InteractionEvent.PICK_ITEM_FROM_BLOCK.invoker().pick(player, pos, state, includeData);
            return result.isPresent() ? result.object() : null;
        });
        PlayerPickItemEvents.ENTITY.register((player, entity, includeData) -> {
            CompoundEventResult<ItemStack> result = InteractionEvent.PICK_ITEM_FROM_ENTITY.invoker().pick(player, entity, includeData);
            return result.isPresent() ? result.object() : null;
        });
        
        LootTableEvents.MODIFY.register((key, tableBuilder, source, provider) -> LootEvent.MODIFY_LOOT_TABLE.invoker().modifyLootTable(provider, key, new LootTableModificationContextImpl(tableBuilder), source.isBuiltin()));
        
        ServerMessageDecoratorEvent.EVENT.register(ServerMessageDecoratorEvent.CONTENT_PHASE, (player, component) -> {
            ChatEvent.ChatComponent chatComponent = new ChatComponentImpl(component);
            ChatEvent.DECORATE.invoker().decorate(player, chatComponent);
            return chatComponent.get();
        });
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> !ChatEvent.RECEIVED.invoker().received(sender, message.decoratedContent()).isFalse());

        LootTableEvents.REPLACE.register((key, original, source, provider) -> {
            CompoundEventResult<LootTable> result = LootEvent.REPLACE_LOOT_TABLE.invoker().replaceLootTable(provider, key, original);
            return result.isPresent() ? result.object() : null;
        });

        ServerEntityEvents.ENTITY_UNLOAD.register((entity, level) -> EntityEvent.REMOVE.invoker().remove(entity, level));
        ServerEntityEvents.EQUIPMENT_CHANGE.register((entity, slot, previousStack, currentStack) ->
                EntityEvent.EQUIPMENT_CHANGE.invoker().change(entity, slot, previousStack, currentStack));

        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> EntityEvent.START_TRACKING.invoker().startTracking(trackedEntity, player));
        EntityTrackingEvents.STOP_TRACKING.register((trackedEntity, player) -> EntityEvent.STOP_TRACKING.invoker().stopTracking(trackedEntity, player));

        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) ->
                EntityEvent.LIVING_DAMAGE_POST.invoker().damage(entity, source, baseDamageTaken, damageTaken, blocked));

        ServerMobEffectEvents.ALLOW_ADD.register((effectInstance, entity, ctx) ->
                !MobEffectEvent.ALLOW_ADD.invoker().allowAdd(entity, effectInstance).isFalse());
        ServerMobEffectEvents.AFTER_ADD.register((effectInstance, entity, ctx) ->
                MobEffectEvent.AFTER_ADD.invoker().afterAdd(entity, effectInstance));
        ServerMobEffectEvents.ALLOW_EARLY_REMOVE.register((effectInstance, entity, ctx) ->
                !MobEffectEvent.ALLOW_REMOVE.invoker().allowRemove(entity, effectInstance).isFalse());

        ServerChunkEvents.CHUNK_LOAD.register((level, chunk, generated) -> ChunkEvent.LOAD.invoker().load(chunk, level, generated));
        ServerChunkEvents.CHUNK_UNLOAD.register((level, chunk) -> ChunkEvent.UNLOAD.invoker().unload(chunk, level));

        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> LifecycleEvent.TAGS_UPDATED.invoker().tagsUpdated(registries, client));
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> LifecycleEvent.DATAPACK_SYNC.invoker().sync(player, joined));
    }
    
    @Environment(EnvType.SERVER)
    public static void registerServer() {
    
    }
    
    /**
     * Fabric's use callbacks treat {@code null} as "not handled, carry on", while Architectury spells that
     * {@link InteractionResult#PASS}. This translates between the two.
     */
    @Nullable
    private static InteractionResult orNull(InteractionResult result) {
        return result == InteractionResult.PASS ? null : result;
    }
}
