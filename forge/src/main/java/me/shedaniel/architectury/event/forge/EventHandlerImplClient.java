/*
 * This file is part of architectury.
 * Copyright (C) 2020 shedaniel
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

package me.shedaniel.architectury.event.forge;

import me.shedaniel.architectury.event.events.TextureStitchEvent;
import me.shedaniel.architectury.event.events.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.play.server.SUpdateRecipesPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class EventHandlerImplClient {
    @SubscribeEvent
    public static void event(ItemTooltipEvent event) {
        TooltipEvent.ITEM.invoker().append(event.getItemStack(), event.getToolTip(), event.getFlags());
    }
    
    @SubscribeEvent
    public static void event(ClientTickEvent event) {
        if (event.phase == net.minecraftforge.event.TickEvent.Phase.START)
            TickEvent.CLIENT_PRE.invoker().tick(Minecraft.getInstance());
        else if (event.phase == net.minecraftforge.event.TickEvent.Phase.END)
            TickEvent.CLIENT_POST.invoker().tick(Minecraft.getInstance());
    }
    
    @SubscribeEvent
    public static void event(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
            GuiEvent.RENDER_HUD.invoker().renderHud(event.getMatrixStack(), event.getPartialTicks());
    }
    
    @SubscribeEvent
    public static void event(ClientPlayerNetworkEvent.LoggedInEvent event) {
        PlayerEvent.CLIENT_PLAYER_JOIN.invoker().join(event.getPlayer());
    }
    
    @SubscribeEvent
    public static void event(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        PlayerEvent.CLIENT_PLAYER_QUIT.invoker().quit(event.getPlayer());
    }
    
    @SubscribeEvent
    public static void event(ClientPlayerNetworkEvent.RespawnEvent event) {
        PlayerEvent.CLIENT_PLAYER_RESPAWN.invoker().respawn(event.getOldPlayer(), event.getNewPlayer());
    }
    
    @SubscribeEvent
    public static void event(GuiScreenEvent.InitGuiEvent.Pre event) {
        if (GuiEvent.INIT_PRE.invoker().init(event.getGui(), event.getWidgetList(), (List<IGuiEventListener>) event.getGui().children()) == ActionResultType.FAIL) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void event(GuiScreenEvent.InitGuiEvent.Post event) {
        GuiEvent.INIT_POST.invoker().init(event.getGui(), event.getWidgetList(), (List<IGuiEventListener>) event.getGui().children());
    }
    
    @SubscribeEvent
    public static void event(RenderGameOverlayEvent.Text event) {
        GuiEvent.DEBUG_TEXT_LEFT.invoker().gatherText(event.getLeft());
        GuiEvent.DEBUG_TEXT_RIGHT.invoker().gatherText(event.getRight());
    }
    
    @SubscribeEvent
    public static void event(ClientChatEvent event) {
        ActionResult<String> process = ChatEvent.CLIENT.invoker().process(event.getMessage());
        if (process.getObject() != null)
            event.setMessage(process.getObject());
        if (process.getResult() == ActionResultType.FAIL)
            event.setCanceled(true);
    }
    
    @SubscribeEvent
    public static void event(ClientChatReceivedEvent event) {
        ActionResult<ITextComponent> process = ChatEvent.CLIENT_RECEIVED.invoker().process(event.getType(), event.getMessage(), event.getSenderUUID());
        if (process.getObject() != null)
            event.setMessage(process.getObject());
        if (process.getResult() == ActionResultType.FAIL)
            event.setCanceled(true);
    }
    
    @SubscribeEvent
    public static void event(WorldEvent.Save event) {
        if (event.getWorld() instanceof ClientWorld) {
            ClientWorld world = (ClientWorld) event.getWorld();
            LifecycleEvent.CLIENT_WORLD_LOAD.invoker().act(world);
        }
    }
    
    @SubscribeEvent
    public static void event(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (GuiEvent.RENDER_PRE.invoker().render(event.getGui(), event.getMatrixStack(), event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks()) == ActionResultType.FAIL) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void event(GuiScreenEvent.DrawScreenEvent.Post event) {
        GuiEvent.RENDER_POST.invoker().render(event.getGui(), event.getMatrixStack(), event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks());
    }
    
    @SubscribeEvent
    public static void event(PlayerInteractEvent.RightClickEmpty event) {
        InteractionEvent.CLIENT_RIGHT_CLICK_AIR.invoker().click(event.getPlayer(), event.getHand());
    }
    
    @SubscribeEvent
    public static void event(PlayerInteractEvent.LeftClickEmpty event) {
        InteractionEvent.CLIENT_LEFT_CLICK_AIR.invoker().click(event.getPlayer(), event.getHand());
    }
    
    @SubscribeEvent
    public static void event(RecipesUpdatedEvent event) {
        RecipeUpdateEvent.EVENT.invoker().update(event.getRecipeManager());
    }
    
    @OnlyIn(Dist.CLIENT)
    public static class ModBasedEventHandler {
        @SubscribeEvent
        public static void event(net.minecraftforge.client.event.TextureStitchEvent.Pre event) {
            TextureStitchEvent.PRE.invoker().stitch(event.getMap(), event::addSprite);
        }
        
        @SubscribeEvent
        public static void event(net.minecraftforge.client.event.TextureStitchEvent.Post event) {
            TextureStitchEvent.POST.invoker().stitch(event.getMap());
        }
    }
}
