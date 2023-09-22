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

package dev.architectury.event.forge;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventPriority;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientChatEvent;
import dev.architectury.event.events.client.*;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.impl.ScreenAccessImpl;
import dev.architectury.impl.TooltipEventColorContextImpl;
import dev.architectury.impl.TooltipEventPositionContextImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.BiConsumer;

@OnlyIn(Dist.CLIENT)
public class EventHandlerImplClient {
    public static void init() {
        for (EventPriority priority : EventPriority.VALUES) {
            registerWithPriority(priority, ItemTooltipEvent.class, EventHandlerImplClient::event);
            registerWithPriority(priority, net.minecraftforge.event.TickEvent.ClientTickEvent.class, EventHandlerImplClient::event);
            registerWithPriority(priority, RenderGuiEvent.Post.class, EventHandlerImplClient::eventRenderGameOverlayEvent);
            registerWithPriority(priority, ClientPlayerNetworkEvent.LoggingIn.class, EventHandlerImplClient::event);
            registerWithPriority(priority, ClientPlayerNetworkEvent.LoggingOut.class, EventHandlerImplClient::event);
            registerWithPriority(priority, ClientPlayerNetworkEvent.Clone.class, EventHandlerImplClient::event);
            registerWithPriority(priority, ScreenEvent.Init.Pre.class, EventHandlerImplClient::eventInitScreenEvent);
            registerWithPriority(priority, ScreenEvent.Init.Post.class, EventHandlerImplClient::eventInitScreenEvent);
            registerWithPriority(priority, CustomizeGuiOverlayEvent.DebugText.class, EventHandlerImplClient::eventRenderGameOverlayEvent);
            registerWithPriority(priority, net.minecraftforge.client.event.ClientChatEvent.class, EventHandlerImplClient::event);
            registerWithPriority(priority, ClientChatReceivedEvent.class, EventHandlerImplClient::event);
            registerWithPriority(priority, LevelEvent.Load.class, EventHandlerImplClient::eventWorldEvent);
            registerWithPriority(priority, ScreenEvent.Opening.class, EventHandlerImplClient::event);
            registerWithPriority(priority, ScreenEvent.Render.Pre.class, EventHandlerImplClient::eventDrawScreenEvent);
            registerWithPriority(priority, ScreenEvent.Render.Post.class, EventHandlerImplClient::eventDrawScreenEvent);
            registerWithPriority(priority, ContainerScreenEvent.Render.Background.class, EventHandlerImplClient::eventContainerScreenEvent);
            registerWithPriority(priority, ContainerScreenEvent.Render.Foreground.class, EventHandlerImplClient::eventContainerScreenEvent);
            registerWithPriority(priority, PlayerInteractEvent.RightClickEmpty.class, EventHandlerImplClient::eventPlayerInteractEvent);
            registerWithPriority(priority, PlayerInteractEvent.LeftClickEmpty.class, EventHandlerImplClient::eventPlayerInteractEvent);
            registerWithPriority(priority, RecipesUpdatedEvent.class, EventHandlerImplClient::event);
            registerWithPriority(priority, RenderTooltipEvent.Pre.class, EventHandlerImplClient::eventRenderTooltipEvent);
            registerWithPriority(priority, RenderTooltipEvent.Color.class, EventHandlerImplClient::eventRenderTooltipEvent);
            registerWithPriority(priority, ScreenEvent.MouseScrolled.Pre.class, EventHandlerImplClient::eventMouseScrollEvent);
            registerWithPriority(priority, ScreenEvent.MouseScrolled.Post.class, EventHandlerImplClient::eventMouseScrollEvent);
            registerWithPriority(priority, ScreenEvent.MouseButtonPressed.Pre.class, EventHandlerImplClient::eventMouseClickedEvent);
            registerWithPriority(priority, ScreenEvent.MouseButtonPressed.Post.class, EventHandlerImplClient::eventMouseClickedEvent);
            registerWithPriority(priority, ScreenEvent.MouseDragged.Pre.class, EventHandlerImplClient::eventMouseDragEvent);
            registerWithPriority(priority, ScreenEvent.MouseDragged.Post.class, EventHandlerImplClient::eventMouseDragEvent);
            registerWithPriority(priority, ScreenEvent.MouseButtonReleased.Pre.class, EventHandlerImplClient::eventMouseReleasedEvent);
            registerWithPriority(priority, ScreenEvent.MouseButtonReleased.Post.class, EventHandlerImplClient::eventMouseReleasedEvent);
            registerWithPriority(priority, ScreenEvent.CharacterTyped.Pre.class, EventHandlerImplClient::eventKeyboardCharTypedEvent);
            registerWithPriority(priority, ScreenEvent.CharacterTyped.Post.class, EventHandlerImplClient::eventKeyboardCharTypedEvent);
            registerWithPriority(priority, ScreenEvent.KeyPressed.Pre.class, EventHandlerImplClient::eventKeyboardKeyPressedEvent);
            registerWithPriority(priority, ScreenEvent.KeyPressed.Post.class, EventHandlerImplClient::eventKeyboardKeyPressedEvent);
            registerWithPriority(priority, ScreenEvent.KeyReleased.Pre.class, EventHandlerImplClient::eventKeyboardKeyReleasedEvent);
            registerWithPriority(priority, ScreenEvent.KeyReleased.Post.class, EventHandlerImplClient::eventKeyboardKeyReleasedEvent);
            registerWithPriority(priority, InputEvent.MouseScrollingEvent.class, EventHandlerImplClient::eventInputEvent);
            registerWithPriority(priority, InputEvent.MouseButton.Pre.class, EventHandlerImplClient::eventInputEvent);
            registerWithPriority(priority, InputEvent.MouseButton.Post.class, EventHandlerImplClient::eventInputEvent);
            registerWithPriority(priority, InputEvent.Key.class, EventHandlerImplClient::eventInputEvent);
        }
    }
    
    public static void event(ItemTooltipEvent event, EventPriority priority) {
        ClientTooltipEvent.ITEM.invoker(priority).append(event.getItemStack(), event.getToolTip(), event.getFlags());
    }
    
    public static void event(net.minecraftforge.event.TickEvent.ClientTickEvent event, EventPriority priority) {
        if (event.phase == net.minecraftforge.event.TickEvent.Phase.START)
            ClientTickEvent.CLIENT_PRE.invoker(priority).tick(Minecraft.getInstance());
        else if (event.phase == net.minecraftforge.event.TickEvent.Phase.END)
            ClientTickEvent.CLIENT_POST.invoker(priority).tick(Minecraft.getInstance());
    }
    
    public static void eventRenderGameOverlayEvent(RenderGuiEvent.Post event, EventPriority priority) {
        ClientGuiEvent.RENDER_HUD.invoker(priority).renderHud(event.getGuiGraphics(), event.getPartialTick());
    }
    
    public static void event(ClientPlayerNetworkEvent.LoggingIn event, EventPriority priority) {
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.invoker(priority).join(event.getPlayer());
    }
    
    public static void event(ClientPlayerNetworkEvent.LoggingOut event, EventPriority priority) {
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.invoker(priority).quit(event.getPlayer());
    }
    
    public static void event(ClientPlayerNetworkEvent.Clone event, EventPriority priority) {
        ClientPlayerEvent.CLIENT_PLAYER_RESPAWN.invoker(priority).respawn(event.getOldPlayer(), event.getNewPlayer());
    }
    
    public static void eventInitScreenEvent(ScreenEvent.Init.Pre event, EventPriority priority) {
        if (ClientGuiEvent.INIT_PRE.invoker(priority).init(event.getScreen(), new ScreenAccessImpl(event.getScreen())).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void eventInitScreenEvent(ScreenEvent.Init.Post event, EventPriority priority) {
        ClientGuiEvent.INIT_POST.invoker(priority).init(event.getScreen(), new ScreenAccessImpl(event.getScreen()));
    }
    
    public static void eventRenderGameOverlayEvent(CustomizeGuiOverlayEvent.DebugText event, EventPriority priority) {
        if (Minecraft.getInstance().options.renderDebug) {
            ClientGuiEvent.DEBUG_TEXT_LEFT.invoker(priority).gatherText(event.getLeft());
            ClientGuiEvent.DEBUG_TEXT_RIGHT.invoker(priority).gatherText(event.getRight());
        }
    }
    
    public static void event(net.minecraftforge.client.event.ClientChatEvent event, EventPriority priority) {
        EventResult process = ClientChatEvent.SEND.invoker(priority).send(event.getMessage(), null);
        if (process.isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void event(ClientChatReceivedEvent event, EventPriority priority) {
        CompoundEventResult<Component> process = ClientChatEvent.RECEIVED.invoker(priority).process(event.getBoundChatType(), event.getMessage());
        if (process.isPresent()) {
            if (process.isFalse())
                event.setCanceled(true);
            else if (process.object() != null)
                event.setMessage(process.object());
        }
    }
    
    public static void eventWorldEvent(LevelEvent.Load event, EventPriority priority) {
        if (event.getLevel().isClientSide()) {
            ClientLevel world = (ClientLevel) event.getLevel();
            ClientLifecycleEvent.CLIENT_LEVEL_LOAD.invoker(priority).act(world);
        }
    }
    
    public static void event(ScreenEvent.Opening event, EventPriority priority) {
        CompoundEventResult<Screen> result = ClientGuiEvent.SET_SCREEN.invoker(priority).modifyScreen(event.getScreen());
        if (result.isPresent()) {
            if (result.isFalse())
                event.setCanceled(true);
            else if (result.object() != null)
                event.setNewScreen(result.object());
        }
    }
    
    public static void eventDrawScreenEvent(ScreenEvent.Render.Pre event, EventPriority priority) {
        if (ClientGuiEvent.RENDER_PRE.invoker(priority).render(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void eventDrawScreenEvent(ScreenEvent.Render.Post event, EventPriority priority) {
        ClientGuiEvent.RENDER_POST.invoker(priority).render(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
    }
    
    public static void eventContainerScreenEvent(ContainerScreenEvent.Render.Background event, EventPriority priority) {
        ClientGuiEvent.RENDER_CONTAINER_BACKGROUND.invoker(priority).render(event.getContainerScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), Minecraft.getInstance().getDeltaFrameTime());
    }
    
    public static void eventContainerScreenEvent(ContainerScreenEvent.Render.Foreground event, EventPriority priority) {
        ClientGuiEvent.RENDER_CONTAINER_FOREGROUND.invoker(priority).render(event.getContainerScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), Minecraft.getInstance().getDeltaFrameTime());
    }
    
    public static void eventPlayerInteractEvent(PlayerInteractEvent.RightClickEmpty event, EventPriority priority) {
        InteractionEvent.CLIENT_RIGHT_CLICK_AIR.invoker(priority).click(event.getEntity(), event.getHand());
    }
    
    public static void eventPlayerInteractEvent(PlayerInteractEvent.LeftClickEmpty event, EventPriority priority) {
        InteractionEvent.CLIENT_LEFT_CLICK_AIR.invoker(priority).click(event.getEntity(), event.getHand());
    }
    
    public static void event(RecipesUpdatedEvent event, EventPriority priority) {
        ClientRecipeUpdateEvent.EVENT.invoker(priority).update(event.getRecipeManager());
    }
    
    private static final ThreadLocal<TooltipEventPositionContextImpl> tooltipPositionContext = ThreadLocal.withInitial(TooltipEventPositionContextImpl::new);
    
    public static void eventRenderTooltipEvent(RenderTooltipEvent.Pre event, EventPriority priority) {
        GuiGraphics graphics = event.getGraphics();
        ClientTooltipEvent.additionalContexts().setItem(event.getItemStack());
        
        try {
            if (ClientTooltipEvent.RENDER_PRE.invoker(priority).renderTooltip(graphics, event.getComponents(), event.getX(), event.getY()).isFalse()) {
                event.setCanceled(true);
                return;
            }
            
            TooltipEventPositionContextImpl positionContext = tooltipPositionContext.get();
            positionContext.reset(event.getX(), event.getY());
            ClientTooltipEvent.RENDER_MODIFY_POSITION.invoker(priority).renderTooltip(graphics, positionContext);
            event.setX(positionContext.getTooltipX());
            event.setY(positionContext.getTooltipY());
        } finally {
            ClientTooltipEvent.additionalContexts().setItem(null);
        }
    }
    
    public static void eventRenderTooltipEvent(RenderTooltipEvent.Color event, EventPriority priority) {
        GuiGraphics graphics = event.getGraphics();
        ClientTooltipEvent.additionalContexts().setItem(event.getItemStack());
        
        try {
            TooltipEventColorContextImpl colorContext = TooltipEventColorContextImpl.CONTEXT.get();
            colorContext.reset();
            colorContext.setBackgroundColor(event.getBackgroundStart());
            colorContext.setOutlineGradientTopColor(event.getBorderStart());
            colorContext.setOutlineGradientBottomColor(event.getBorderEnd());
            ClientTooltipEvent.RENDER_MODIFY_COLOR.invoker(priority).renderTooltip(graphics, event.getX(), event.getY(), colorContext);
            event.setBackground(colorContext.getBackgroundColor());
            event.setBorderEnd(colorContext.getOutlineGradientBottomColor());
            event.setBorderStart(colorContext.getOutlineGradientTopColor());
        } finally {
            ClientTooltipEvent.additionalContexts().setItem(null);
        }
    }
    
    public static void eventMouseScrollEvent(ScreenEvent.MouseScrolled.Pre event, EventPriority priority) {
        if (ClientScreenInputEvent.MOUSE_SCROLLED_PRE.invoker(priority).mouseScrolled(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getScrollDelta()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void eventMouseScrollEvent(ScreenEvent.MouseScrolled.Post event, EventPriority priority) {
        ClientScreenInputEvent.MOUSE_SCROLLED_POST.invoker(priority).mouseScrolled(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getScrollDelta());
    }
    
    public static void eventMouseClickedEvent(ScreenEvent.MouseButtonPressed.Pre event, EventPriority priority) {
        if (ClientScreenInputEvent.MOUSE_CLICKED_PRE.invoker(priority).mouseClicked(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void eventMouseClickedEvent(ScreenEvent.MouseButtonPressed.Post event, EventPriority priority) {
        ClientScreenInputEvent.MOUSE_CLICKED_POST.invoker(priority).mouseClicked(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton());
    }
    
    public static void eventMouseDragEvent(ScreenEvent.MouseDragged.Pre event, EventPriority priority) {
        if (ClientScreenInputEvent.MOUSE_DRAGGED_PRE.invoker(priority).mouseDragged(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void eventMouseDragEvent(ScreenEvent.MouseDragged.Post event, EventPriority priority) {
        ClientScreenInputEvent.MOUSE_DRAGGED_POST.invoker(priority).mouseDragged(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY());
    }
    
    public static void eventMouseReleasedEvent(ScreenEvent.MouseButtonReleased.Pre event, EventPriority priority) {
        if (ClientScreenInputEvent.MOUSE_RELEASED_PRE.invoker(priority).mouseReleased(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void eventMouseReleasedEvent(ScreenEvent.MouseButtonReleased.Post event, EventPriority priority) {
        ClientScreenInputEvent.MOUSE_RELEASED_PRE.invoker(priority).mouseReleased(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton());
    }
    
    public static void eventKeyboardCharTypedEvent(ScreenEvent.CharacterTyped.Pre event, EventPriority priority) {
        if (ClientScreenInputEvent.CHAR_TYPED_PRE.invoker(priority).charTyped(Minecraft.getInstance(), event.getScreen(), event.getCodePoint(), event.getModifiers()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void eventKeyboardCharTypedEvent(ScreenEvent.CharacterTyped.Post event, EventPriority priority) {
        ClientScreenInputEvent.CHAR_TYPED_POST.invoker(priority).charTyped(Minecraft.getInstance(), event.getScreen(), event.getCodePoint(), event.getModifiers());
    }
    
    public static void eventKeyboardKeyPressedEvent(ScreenEvent.KeyPressed.Pre event, EventPriority priority) {
        if (ClientScreenInputEvent.KEY_PRESSED_PRE.invoker(priority).keyPressed(Minecraft.getInstance(), event.getScreen(), event.getKeyCode(), event.getScanCode(), event.getModifiers()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void eventKeyboardKeyPressedEvent(ScreenEvent.KeyPressed.Post event, EventPriority priority) {
        ClientScreenInputEvent.KEY_PRESSED_POST.invoker(priority).keyPressed(Minecraft.getInstance(), event.getScreen(), event.getKeyCode(), event.getScanCode(), event.getModifiers());
    }
    
    public static void eventKeyboardKeyReleasedEvent(ScreenEvent.KeyReleased.Pre event, EventPriority priority) {
        if (ClientScreenInputEvent.KEY_RELEASED_PRE.invoker(priority).keyReleased(Minecraft.getInstance(), event.getScreen(), event.getKeyCode(), event.getScanCode(), event.getModifiers()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void eventKeyboardKeyReleasedEvent(ScreenEvent.KeyReleased.Post event, EventPriority priority) {
        ClientScreenInputEvent.KEY_RELEASED_POST.invoker(priority).keyReleased(Minecraft.getInstance(), event.getScreen(), event.getKeyCode(), event.getScanCode(), event.getModifiers());
    }
    
    public static void eventInputEvent(InputEvent.MouseScrollingEvent event, EventPriority priority) {
        if (ClientRawInputEvent.MOUSE_SCROLLED.invoker(priority).mouseScrolled(Minecraft.getInstance(), event.getScrollDelta()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void eventInputEvent(InputEvent.MouseButton.Pre event, EventPriority priority) {
        if (ClientRawInputEvent.MOUSE_CLICKED_PRE.invoker(priority).mouseClicked(Minecraft.getInstance(), event.getButton(), event.getAction(), event.getModifiers()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void eventInputEvent(InputEvent.MouseButton.Post event, EventPriority priority) {
        ClientRawInputEvent.MOUSE_CLICKED_POST.invoker(priority).mouseClicked(Minecraft.getInstance(), event.getButton(), event.getAction(), event.getModifiers());
    }
    
    public static void eventInputEvent(InputEvent.Key event, EventPriority priority) {
        ClientRawInputEvent.KEY_PRESSED.invoker(priority).keyPressed(Minecraft.getInstance(), event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers());
    }
    
    private static <T extends Event> void registerWithPriority(EventPriority priority, Class<T> type, BiConsumer<T, EventPriority> callback) {
        net.minecraftforge.eventbus.api.EventPriority forgePriority = net.minecraftforge.eventbus.api.EventPriority.valueOf(priority.name());
        MinecraftForge.EVENT_BUS.addListener(forgePriority, false, type, event -> callback.accept(event, priority));
    }
    
    @OnlyIn(Dist.CLIENT)
    public static class ModBasedEventHandler {
        public static void init(IEventBus bus) {
            for (EventPriority priority : EventPriority.VALUES) {
                registerWithPriority(bus, priority, TextureStitchEvent.Post.class, ModBasedEventHandler::eventTextureStitchEvent);
                registerWithPriority(bus, priority, FMLClientSetupEvent.class, ModBasedEventHandler::event);
                registerWithPriority(bus, priority, RegisterShadersEvent.class, ModBasedEventHandler::event);
            }
        }
        
        public static void eventTextureStitchEvent(TextureStitchEvent.Post event, EventPriority priority) {
            // ClientTextureStitchEvent.POST.invoker(priority).stitch(event.getAtlas());
        }
        
        public static void event(FMLClientSetupEvent event, EventPriority priority) {
            ClientLifecycleEvent.CLIENT_SETUP.invoker(priority).stateChanged(Minecraft.getInstance());
        }
        
        public static void event(RegisterShadersEvent event, EventPriority priority) {
            ClientReloadShadersEvent.EVENT.invoker(priority).reload(event.getResourceProvider(), event::registerShader);
        }
        
        private static <T extends Event> void registerWithPriority(IEventBus bus, EventPriority priority, Class<T> type, BiConsumer<T, EventPriority> callback) {
            net.minecraftforge.eventbus.api.EventPriority forgePriority = net.minecraftforge.eventbus.api.EventPriority.valueOf(priority.name());
            bus.addListener(forgePriority, false, type, event -> callback.accept(event, priority));
        }
    }
}
