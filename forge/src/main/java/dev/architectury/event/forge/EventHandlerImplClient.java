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

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.events.client.ClientChatEvent;
import dev.architectury.event.events.client.*;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.impl.ScreenAccessImpl;
import dev.architectury.impl.TooltipEventColorContextImpl;
import dev.architectury.impl.TooltipEventPositionContextImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
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
    public static void event(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
            ClientGuiEvent.RENDER_HUD.invoker().renderHud(event.getMatrixStack(), event.getPartialTicks());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ClientPlayerNetworkEvent.LoggedInEvent event) {
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.invoker().join(event.getPlayer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.invoker().quit(event.getPlayer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ClientPlayerNetworkEvent.RespawnEvent event) {
        ClientPlayerEvent.CLIENT_PLAYER_RESPAWN.invoker().respawn(event.getOldPlayer(), event.getNewPlayer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.InitScreenEvent.Pre event) {
        if (ClientGuiEvent.INIT_PRE.invoker().init(event.getScreen(), new ScreenAccessImpl(event.getScreen())).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.InitScreenEvent.Post event) {
        ClientGuiEvent.INIT_POST.invoker().init(event.getScreen(), new ScreenAccessImpl(event.getScreen()));
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(RenderGameOverlayEvent.Text event) {
        if (Minecraft.getInstance().options.renderDebug) {
            ClientGuiEvent.DEBUG_TEXT_LEFT.invoker().gatherText(event.getLeft());
            ClientGuiEvent.DEBUG_TEXT_RIGHT.invoker().gatherText(event.getRight());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(net.minecraftforge.client.event.ClientChatEvent event) {
        CompoundEventResult<String> process = ClientChatEvent.PROCESS.invoker().process(event.getMessage());
        if (process.isPresent()) {
            if (process.isFalse())
                event.setCanceled(true);
            else if (process.object() != null)
                event.setMessage(process.object());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ClientChatReceivedEvent event) {
        CompoundEventResult<Component> process = ClientChatEvent.RECEIVED.invoker().process(event.getType(), event.getMessage(), event.getSenderUUID());
        if (process.isPresent()) {
            if (process.isFalse())
                event.setCanceled(true);
            else if (process.object() != null)
                event.setMessage(process.object());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(WorldEvent.Load event) {
        if (event.getWorld().isClientSide()) {
            ClientLevel world = (ClientLevel) event.getWorld();
            ClientLifecycleEvent.CLIENT_LEVEL_LOAD.invoker().act(world);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenOpenEvent event) {
        CompoundEventResult<Screen> result = ClientGuiEvent.SET_SCREEN.invoker().modifyScreen(event.getScreen());
        if (result.isPresent()) {
            if (result.isFalse())
                event.setCanceled(true);
            else if (result.object() != null)
                event.setScreen(result.object());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.DrawScreenEvent.Pre event) {
        if (ClientGuiEvent.RENDER_PRE.invoker().render(event.getScreen(), event.getPoseStack(), event.getMouseX(), event.getMouseY(), event.getPartialTicks()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.DrawScreenEvent.Post event) {
        ClientGuiEvent.RENDER_POST.invoker().render(event.getScreen(), event.getPoseStack(), event.getMouseX(), event.getMouseY(), event.getPartialTicks());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ContainerScreenEvent.DrawBackground event) {
        ClientGuiEvent.RENDER_CONTAINER_BACKGROUND.invoker().render(event.getContainerScreen(), event.getPoseStack(), event.getMouseX(), event.getMouseY(), Minecraft.getInstance().getDeltaFrameTime());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ContainerScreenEvent.DrawForeground event) {
        ClientGuiEvent.RENDER_CONTAINER_FOREGROUND.invoker().render(event.getContainerScreen(), event.getPoseStack(), event.getMouseX(), event.getMouseY(), Minecraft.getInstance().getDeltaFrameTime());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerInteractEvent.RightClickEmpty event) {
        InteractionEvent.CLIENT_RIGHT_CLICK_AIR.invoker().click(event.getPlayer(), event.getHand());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerInteractEvent.LeftClickEmpty event) {
        InteractionEvent.CLIENT_LEFT_CLICK_AIR.invoker().click(event.getPlayer(), event.getHand());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(RecipesUpdatedEvent event) {
        ClientRecipeUpdateEvent.EVENT.invoker().update(event.getRecipeManager());
    }
    
    private static final ThreadLocal<TooltipEventColorContextImpl> tooltipColorContext = ThreadLocal.withInitial(TooltipEventColorContextImpl::new);
    private static final ThreadLocal<TooltipEventPositionContextImpl> tooltipPositionContext = ThreadLocal.withInitial(TooltipEventPositionContextImpl::new);
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(RenderTooltipEvent.Pre event) {
        PoseStack stack = event.getPoseStack();
        ClientTooltipEvent.additionalContexts().setItem(event.getItemStack());
        
        try {
            if (ClientTooltipEvent.RENDER_PRE.invoker().renderTooltip(stack, event.getComponents(), event.getX(), event.getY()).isFalse()) {
                event.setCanceled(true);
                return;
            }
            
            TooltipEventPositionContextImpl positionContext = tooltipPositionContext.get();
            positionContext.reset(event.getX(), event.getY());
            ClientTooltipEvent.RENDER_MODIFY_POSITION.invoker().renderTooltip(stack, positionContext);
            event.setX(positionContext.getTooltipX());
            event.setY(positionContext.getTooltipY());
        } finally {
            ClientTooltipEvent.additionalContexts().setItem(null);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(RenderTooltipEvent.Color event) {
        PoseStack stack = event.getPoseStack();
        ClientTooltipEvent.additionalContexts().setItem(event.getItemStack());
        
        try {
            TooltipEventColorContextImpl colorContext = tooltipColorContext.get();
            colorContext.reset();
            colorContext.setBackgroundColor(event.getBackgroundStart());
            colorContext.setOutlineGradientTopColor(event.getBorderStart());
            colorContext.setOutlineGradientBottomColor(event.getBorderEnd());
            ClientTooltipEvent.RENDER_MODIFY_COLOR.invoker().renderTooltip(stack, event.getX(), event.getY(), colorContext);
            event.setBackground(colorContext.getBackgroundColor());
            event.setBorderEnd(colorContext.getOutlineGradientBottomColor());
            event.setBorderStart(colorContext.getOutlineGradientTopColor());
        } finally {
            ClientTooltipEvent.additionalContexts().setItem(null);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.MouseScrollEvent.Pre event) {
        if (ClientScreenInputEvent.MOUSE_SCROLLED_PRE.invoker().mouseScrolled(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getScrollDelta()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.MouseScrollEvent.Post event) {
        ClientScreenInputEvent.MOUSE_SCROLLED_POST.invoker().mouseScrolled(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getScrollDelta());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.MouseClickedEvent.Pre event) {
        if (ClientScreenInputEvent.MOUSE_CLICKED_PRE.invoker().mouseClicked(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.MouseClickedEvent.Post event) {
        ClientScreenInputEvent.MOUSE_CLICKED_POST.invoker().mouseClicked(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.MouseDragEvent.Pre event) {
        if (ClientScreenInputEvent.MOUSE_DRAGGED_PRE.invoker().mouseDragged(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.MouseDragEvent.Post event) {
        ClientScreenInputEvent.MOUSE_DRAGGED_POST.invoker().mouseDragged(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.MouseReleasedEvent.Pre event) {
        if (ClientScreenInputEvent.MOUSE_RELEASED_PRE.invoker().mouseReleased(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.MouseReleasedEvent.Post event) {
        ClientScreenInputEvent.MOUSE_RELEASED_PRE.invoker().mouseReleased(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.KeyboardCharTypedEvent.Pre event) {
        if (ClientScreenInputEvent.CHAR_TYPED_PRE.invoker().charTyped(Minecraft.getInstance(), event.getScreen(), event.getCodePoint(), event.getModifiers()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.KeyboardCharTypedEvent.Post event) {
        ClientScreenInputEvent.CHAR_TYPED_POST.invoker().charTyped(Minecraft.getInstance(), event.getScreen(), event.getCodePoint(), event.getModifiers());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.KeyboardKeyPressedEvent.Pre event) {
        if (ClientScreenInputEvent.KEY_PRESSED_PRE.invoker().keyPressed(Minecraft.getInstance(), event.getScreen(), event.getKeyCode(), event.getScanCode(), event.getModifiers()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.KeyboardKeyPressedEvent.Post event) {
        ClientScreenInputEvent.KEY_PRESSED_POST.invoker().keyPressed(Minecraft.getInstance(), event.getScreen(), event.getKeyCode(), event.getScanCode(), event.getModifiers());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.KeyboardKeyReleasedEvent.Pre event) {
        if (ClientScreenInputEvent.KEY_RELEASED_PRE.invoker().keyReleased(Minecraft.getInstance(), event.getScreen(), event.getKeyCode(), event.getScanCode(), event.getModifiers()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ScreenEvent.KeyboardKeyReleasedEvent.Post event) {
        ClientScreenInputEvent.KEY_RELEASED_POST.invoker().keyReleased(Minecraft.getInstance(), event.getScreen(), event.getKeyCode(), event.getScanCode(), event.getModifiers());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(InputEvent.MouseScrollEvent event) {
        if (ClientRawInputEvent.MOUSE_SCROLLED.invoker().mouseScrolled(Minecraft.getInstance(), event.getScrollDelta()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(InputEvent.RawMouseEvent event) {
        if (ClientRawInputEvent.MOUSE_CLICKED_PRE.invoker().mouseClicked(Minecraft.getInstance(), event.getButton(), event.getAction(), event.getModifiers()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(InputEvent.MouseInputEvent event) {
        ClientRawInputEvent.MOUSE_CLICKED_POST.invoker().mouseClicked(Minecraft.getInstance(), event.getButton(), event.getAction(), event.getModifiers());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(InputEvent.KeyInputEvent event) {
        ClientRawInputEvent.KEY_PRESSED.invoker().keyPressed(Minecraft.getInstance(), event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(RegisterClientCommandsEvent event) {
        ClientCommandRegistrationEvent.EVENT.invoker().register((CommandDispatcher<ClientCommandRegistrationEvent.ClientCommandSourceStack>)
                (CommandDispatcher<?>) event.getDispatcher());
    }
    
    @OnlyIn(Dist.CLIENT)
    public static class ModBasedEventHandler {
        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void event(TextureStitchEvent.Pre event) {
            ClientTextureStitchEvent.PRE.invoker().stitch(event.getAtlas(), event::addSprite);
        }
        
        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void event(TextureStitchEvent.Post event) {
            ClientTextureStitchEvent.POST.invoker().stitch(event.getAtlas());
        }
        
        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void event(FMLClientSetupEvent event) {
            ClientLifecycleEvent.CLIENT_SETUP.invoker().stateChanged(Minecraft.getInstance());
        }
        
        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void event(RegisterShadersEvent event) {
            ClientReloadShadersEvent.EVENT.invoker().reload(event.getResourceManager(), event::registerShader);
        }
    }
}
