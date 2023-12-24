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
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientChatEvent;
import dev.architectury.event.events.client.*;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.hooks.forgelike.ForgeLikeClientHooks;
import dev.architectury.impl.ScreenAccessImpl;
import dev.architectury.impl.TooltipEventColorContextImpl;
import dev.architectury.impl.TooltipEventPositionContextImpl;
import dev.architectury.registry.client.gui.forge.ClientTooltipComponentManagerImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class EventHandlerImplClient {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ItemTooltipEvent event) {
        ClientTooltipEvent.ITEM.invoker().append(event.getItemStack(), event.getToolTip(), event.getFlags());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(net.minecraftforge.event.TickEvent.ClientTickEvent event) {
        if (event.phase == net.minecraftforge.event.TickEvent.Phase.START)
            ClientTickEvent.CLIENT_PRE.invoker().tick(Minecraft.getInstance());
        else if (event.phase == net.minecraftforge.event.TickEvent.Phase.END)
            ClientTickEvent.CLIENT_POST.invoker().tick(Minecraft.getInstance());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventRenderGameOverlayEvent(RenderGuiEvent.Post event) {
        ClientGuiEvent.RENDER_HUD.invoker().renderHud(event.getGuiGraphics(), event.getPartialTick());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ClientPlayerNetworkEvent.LoggingIn event) {
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.invoker().join(event.getPlayer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.invoker().quit(event.getPlayer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ClientPlayerNetworkEvent.Clone event) {
        ClientPlayerEvent.CLIENT_PLAYER_RESPAWN.invoker().respawn(event.getOldPlayer(), event.getNewPlayer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventInitScreenEvent(ScreenEvent.Init.Pre event) {
        if (ClientGuiEvent.INIT_PRE.invoker().init(event.getScreen(), new ScreenAccessImpl(event.getScreen())).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventInitScreenEvent(ScreenEvent.Init.Post event) {
        ClientGuiEvent.INIT_POST.invoker().init(event.getScreen(), new ScreenAccessImpl(event.getScreen()));
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventRenderGameOverlayEvent(CustomizeGuiOverlayEvent.DebugText event) {
        if (Minecraft.getInstance().gui.getDebugOverlay().showDebugScreen()) {
            ClientGuiEvent.DEBUG_TEXT_LEFT.invoker().gatherText(ForgeLikeClientHooks.getLeft(event));
            ClientGuiEvent.DEBUG_TEXT_RIGHT.invoker().gatherText(ForgeLikeClientHooks.getRight(event));
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(net.minecraftforge.client.event.ClientChatEvent event) {
        EventResult process = ClientChatEvent.SEND.invoker().send(event.getMessage(), null);
        if (process.isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ClientChatReceivedEvent event) {
        CompoundEventResult<Component> process = ClientChatEvent.RECEIVED.invoker().process(event.getBoundChatType(), event.getMessage());
        if (process.isPresent()) {
            if (process.isFalse())
                event.setCanceled(true);
            else if (process.object() != null)
                event.setMessage(process.object());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventWorldEvent(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) {
            ClientLevel world = (ClientLevel) event.getLevel();
            ClientLifecycleEvent.CLIENT_LEVEL_LOAD.invoker().act(world);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.Opening event) {
        CompoundEventResult<Screen> result = ClientGuiEvent.SET_SCREEN.invoker().modifyScreen(event.getScreen());
        if (result.isPresent()) {
            if (result.isFalse())
                event.setCanceled(true);
            else if (result.object() != null)
                event.setNewScreen(result.object());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventDrawScreenEvent(ScreenEvent.Render.Pre event) {
        if (ClientGuiEvent.RENDER_PRE.invoker().render(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventDrawScreenEvent(ScreenEvent.Render.Post event) {
        ClientGuiEvent.RENDER_POST.invoker().render(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventContainerScreenEvent(ContainerScreenEvent.Render.Background event) {
        ClientGuiEvent.RENDER_CONTAINER_BACKGROUND.invoker().render(event.getContainerScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), Minecraft.getInstance().getDeltaFrameTime());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventContainerScreenEvent(ContainerScreenEvent.Render.Foreground event) {
        ClientGuiEvent.RENDER_CONTAINER_FOREGROUND.invoker().render(event.getContainerScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), Minecraft.getInstance().getDeltaFrameTime());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerInteractEvent(PlayerInteractEvent.RightClickEmpty event) {
        InteractionEvent.CLIENT_RIGHT_CLICK_AIR.invoker().click(event.getEntity(), event.getHand());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerInteractEvent(PlayerInteractEvent.LeftClickEmpty event) {
        InteractionEvent.CLIENT_LEFT_CLICK_AIR.invoker().click(event.getEntity(), event.getHand());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(RecipesUpdatedEvent event) {
        ClientRecipeUpdateEvent.EVENT.invoker().update(event.getRecipeManager());
    }
    
    private static final ThreadLocal<TooltipEventPositionContextImpl> tooltipPositionContext = ThreadLocal.withInitial(TooltipEventPositionContextImpl::new);
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventRenderTooltipEvent(RenderTooltipEvent.Pre event) {
        GuiGraphics graphics = event.getGraphics();
        ClientTooltipEvent.additionalContexts().setItem(event.getItemStack());
        
        try {
            if (ClientTooltipEvent.RENDER_PRE.invoker().renderTooltip(graphics, event.getComponents(), event.getX(), event.getY()).isFalse()) {
                event.setCanceled(true);
                return;
            }
            
            TooltipEventPositionContextImpl positionContext = tooltipPositionContext.get();
            positionContext.reset(event.getX(), event.getY());
            ClientTooltipEvent.RENDER_MODIFY_POSITION.invoker().renderTooltip(graphics, positionContext);
            event.setX(positionContext.getTooltipX());
            event.setY(positionContext.getTooltipY());
        } finally {
            ClientTooltipEvent.additionalContexts().setItem(null);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventRenderTooltipEvent(RenderTooltipEvent.Color event) {
        GuiGraphics graphics = event.getGraphics();
        ClientTooltipEvent.additionalContexts().setItem(event.getItemStack());
        
        try {
            TooltipEventColorContextImpl colorContext = TooltipEventColorContextImpl.CONTEXT.get();
            colorContext.reset();
            colorContext.setBackgroundColor(event.getBackgroundStart());
            colorContext.setOutlineGradientTopColor(event.getBorderStart());
            colorContext.setOutlineGradientBottomColor(event.getBorderEnd());
            ClientTooltipEvent.RENDER_MODIFY_COLOR.invoker().renderTooltip(graphics, event.getX(), event.getY(), colorContext);
            event.setBackground(colorContext.getBackgroundColor());
            event.setBorderEnd(colorContext.getOutlineGradientBottomColor());
            event.setBorderStart(colorContext.getOutlineGradientTopColor());
        } finally {
            ClientTooltipEvent.additionalContexts().setItem(null);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventMouseScrollEvent(ScreenEvent.MouseScrolled.Pre event) {
        ForgeLikeClientHooks.preMouseScroll(event);
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventMouseScrollEvent(ScreenEvent.MouseScrolled.Post event) {
        ForgeLikeClientHooks.postMouseScroll(event);
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventMouseClickedEvent(ScreenEvent.MouseButtonPressed.Pre event) {
        if (ClientScreenInputEvent.MOUSE_CLICKED_PRE.invoker().mouseClicked(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventMouseClickedEvent(ScreenEvent.MouseButtonPressed.Post event) {
        ClientScreenInputEvent.MOUSE_CLICKED_POST.invoker().mouseClicked(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventMouseDragEvent(ScreenEvent.MouseDragged.Pre event) {
        if (ClientScreenInputEvent.MOUSE_DRAGGED_PRE.invoker().mouseDragged(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventMouseDragEvent(ScreenEvent.MouseDragged.Post event) {
        ClientScreenInputEvent.MOUSE_DRAGGED_POST.invoker().mouseDragged(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventMouseReleasedEvent(ScreenEvent.MouseButtonReleased.Pre event) {
        if (ClientScreenInputEvent.MOUSE_RELEASED_PRE.invoker().mouseReleased(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventMouseReleasedEvent(ScreenEvent.MouseButtonReleased.Post event) {
        ClientScreenInputEvent.MOUSE_RELEASED_PRE.invoker().mouseReleased(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventKeyboardCharTypedEvent(ScreenEvent.CharacterTyped.Pre event) {
        if (ClientScreenInputEvent.CHAR_TYPED_PRE.invoker().charTyped(Minecraft.getInstance(), event.getScreen(), event.getCodePoint(), event.getModifiers()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventKeyboardCharTypedEvent(ScreenEvent.CharacterTyped.Post event) {
        ClientScreenInputEvent.CHAR_TYPED_POST.invoker().charTyped(Minecraft.getInstance(), event.getScreen(), event.getCodePoint(), event.getModifiers());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventKeyboardKeyPressedEvent(ScreenEvent.KeyPressed.Pre event) {
        if (ClientScreenInputEvent.KEY_PRESSED_PRE.invoker().keyPressed(Minecraft.getInstance(), event.getScreen(), event.getKeyCode(), event.getScanCode(), event.getModifiers()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventKeyboardKeyPressedEvent(ScreenEvent.KeyPressed.Post event) {
        ClientScreenInputEvent.KEY_PRESSED_POST.invoker().keyPressed(Minecraft.getInstance(), event.getScreen(), event.getKeyCode(), event.getScanCode(), event.getModifiers());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventKeyboardKeyReleasedEvent(ScreenEvent.KeyReleased.Pre event) {
        if (ClientScreenInputEvent.KEY_RELEASED_PRE.invoker().keyReleased(Minecraft.getInstance(), event.getScreen(), event.getKeyCode(), event.getScanCode(), event.getModifiers()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventKeyboardKeyReleasedEvent(ScreenEvent.KeyReleased.Post event) {
        ClientScreenInputEvent.KEY_RELEASED_POST.invoker().keyReleased(Minecraft.getInstance(), event.getScreen(), event.getKeyCode(), event.getScanCode(), event.getModifiers());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventInputEvent(InputEvent.MouseScrollingEvent event) {
        ForgeLikeClientHooks.inputMouseScroll(event);
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventInputEvent(InputEvent.MouseButton.Pre event) {
        if (ClientRawInputEvent.MOUSE_CLICKED_PRE.invoker().mouseClicked(Minecraft.getInstance(), event.getButton(), event.getAction(), event.getModifiers()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventInputEvent(InputEvent.MouseButton.Post event) {
        ClientRawInputEvent.MOUSE_CLICKED_POST.invoker().mouseClicked(Minecraft.getInstance(), event.getButton(), event.getAction(), event.getModifiers());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventInputEvent(InputEvent.Key event) {
        ClientRawInputEvent.KEY_PRESSED.invoker().keyPressed(Minecraft.getInstance(), event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers());
    }
    
    @OnlyIn(Dist.CLIENT)
    public static class ModBasedEventHandler {
        // @SubscribeEvent(priority = EventPriority.HIGH)
        // public static void eventTextureStitchEvent(TextureStitchEvent.Post event) {
        // ClientTextureStitchEvent.POST.invoker().stitch(event.getAtlas());
        // }
        
        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void event(FMLClientSetupEvent event) {
            ClientLifecycleEvent.CLIENT_SETUP.invoker().stateChanged(Minecraft.getInstance());
        }
        
        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void event(RegisterShadersEvent event) {
            ClientReloadShadersEvent.EVENT.invoker().reload(event.getResourceProvider(), event::registerShader);
        }
        
        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void event(RegisterClientTooltipComponentFactoriesEvent event) {
            ClientTooltipComponentManagerImpl.consume(factory -> registerTooltipComponent(factory, event));
        }
        
        private static <T extends TooltipComponent> void registerTooltipComponent(ClientTooltipComponentManagerImpl.Factory<T> factory, RegisterClientTooltipComponentFactoriesEvent event) {
            event.register(factory.clazz(), factory.factory());
        }
    }
}
