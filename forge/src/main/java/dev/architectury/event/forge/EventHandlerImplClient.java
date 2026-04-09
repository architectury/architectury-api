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

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientChatEvent;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientScreenInputEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.impl.ScreenAccessImpl;
import dev.architectury.impl.TooltipEventPositionContextImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.listener.Priority;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class EventHandlerImplClient {
    private static final ThreadLocal<TooltipEventPositionContextImpl> TOOLTIP_POSITION_CONTEXT = ThreadLocal.withInitial(TooltipEventPositionContextImpl::new);

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(ItemTooltipEvent event) {
        var player = event.getEntity();
        Item.TooltipContext context = player != null ? Item.TooltipContext.of(player.level()) : Item.TooltipContext.EMPTY;
        ClientTooltipEvent.ITEM.invoker().append(event.getItemStack(), event.getToolTip(), context, event.getFlags());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(net.minecraftforge.event.TickEvent.ClientTickEvent.Pre event) {
        dev.architectury.event.events.client.ClientTickEvent.CLIENT_PRE.invoker().tick(Minecraft.getInstance());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(net.minecraftforge.event.TickEvent.ClientTickEvent.Post event) {
        dev.architectury.event.events.client.ClientTickEvent.CLIENT_POST.invoker().tick(Minecraft.getInstance());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(ClientPlayerNetworkEvent.LoggingIn event) {
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.invoker().join(event.getPlayer());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.invoker().quit(event.getPlayer());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(ClientPlayerNetworkEvent.Clone event) {
        ClientPlayerEvent.CLIENT_PLAYER_RESPAWN.invoker().respawn(event.getOldPlayer(), event.getNewPlayer());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventInitScreenEvent(ScreenEvent.Init.Pre event) {
        return ClientGuiEvent.INIT_PRE.invoker().init(event.getScreen(), new ScreenAccessImpl(event.getScreen())).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventInitScreenEvent(ScreenEvent.Init.Post event) {
        ClientGuiEvent.INIT_POST.invoker().init(event.getScreen(), new ScreenAccessImpl(event.getScreen()));
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean event(net.minecraftforge.client.event.ClientChatEvent event) {
        return ClientChatEvent.SEND.invoker().send(event.getMessage(), null).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean event(ClientChatReceivedEvent event) {
        CompoundEventResult<Component> process = ClientChatEvent.RECEIVED.invoker().process(event.getBoundChatType(), event.getMessage());
        if (process.isPresent()) {
            if (process.isFalse()) {
                return true;
            }
            if (process.object() != null) {
                event.setMessage(process.object());
            }
        }
        return false;
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventWorldEvent(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) {
            ClientLevel world = (ClientLevel) event.getLevel();
            ClientLifecycleEvent.CLIENT_LEVEL_LOAD.invoker().act(world);
        }
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean event(ScreenEvent.Opening event) {
        CompoundEventResult<Screen> result = ClientGuiEvent.SET_SCREEN.invoker().modifyScreen(event.getNewScreen());
        if (result.isPresent()) {
            if (result.isFalse()) {
                return true;
            }
            if (result.object() != null) {
                event.setNewScreen(result.object());
            }
        }
        return false;
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventDrawScreenEvent(ScreenEvent.Render.Pre event) {
        return ClientGuiEvent.RENDER_PRE.invoker().render(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventDrawScreenEvent(ScreenEvent.Render.Post event) {
        ClientGuiEvent.RENDER_POST.invoker().render(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventContainerScreenEvent(ContainerScreenEvent.Render.Foreground event) {
        ClientGuiEvent.RENDER_CONTAINER_FOREGROUND.invoker().render(event.getContainerScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), Minecraft.getInstance().getDeltaTracker().getRealtimeDeltaTicks());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventPlayerInteractEvent(PlayerInteractEvent.RightClickEmpty event) {
        InteractionEvent.CLIENT_RIGHT_CLICK_AIR.invoker().click(event.getEntity(), event.getHand());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventPlayerInteractEvent(PlayerInteractEvent.LeftClickEmpty event) {
        InteractionEvent.CLIENT_LEFT_CLICK_AIR.invoker().click(event.getEntity(), event.getHand());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventRenderTooltipEvent(RenderTooltipEvent.Pre event) {
        GuiGraphicsExtractor graphics = event.getGraphics();
        ClientTooltipEvent.additionalContexts().setItem(event.getItemStack());

        try {
            if (ClientTooltipEvent.RENDER_PRE.invoker().renderTooltip(graphics, event.getComponents(), event.getX(), event.getY()).isFalse()) {
                return true;
            }

            TooltipEventPositionContextImpl positionContext = TOOLTIP_POSITION_CONTEXT.get();
            positionContext.reset(event.getX(), event.getY());
            ClientTooltipEvent.RENDER_MODIFY_POSITION.invoker().renderTooltip(graphics, positionContext);
            event.setX(positionContext.getTooltipX());
            event.setY(positionContext.getTooltipY());
        } finally {
            ClientTooltipEvent.additionalContexts().setItem(null);
        }

        return false;
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventMouseScrollEvent(ScreenEvent.MouseScrolled.Pre event) {
        return ClientScreenInputEvent.MOUSE_SCROLLED_PRE.invoker().mouseScrolled(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getDeltaX(), event.getDeltaY()).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventMouseScrollEvent(ScreenEvent.MouseScrolled.Post event) {
        ClientScreenInputEvent.MOUSE_SCROLLED_POST.invoker().mouseScrolled(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getDeltaX(), event.getDeltaY());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventMouseClickedEvent(ScreenEvent.MouseButtonPressed.Pre event) {
        return ClientScreenInputEvent.MOUSE_CLICKED_PRE.invoker().mouseClicked(Minecraft.getInstance(), event.getScreen(), event.getInfo(), false).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventMouseClickedEvent(ScreenEvent.MouseButtonPressed.Post event) {
        ClientScreenInputEvent.MOUSE_CLICKED_POST.invoker().mouseClicked(Minecraft.getInstance(), event.getScreen(), event.getInfo(), false);
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventMouseDragEvent(ScreenEvent.MouseDragged.Pre event) {
        return ClientScreenInputEvent.MOUSE_DRAGGED_PRE.invoker().mouseDragged(Minecraft.getInstance(), event.getScreen(), mouseButtonEvent(event.getMouseX(), event.getMouseY(), event.getMouseButton()), event.getDragX(), event.getDragY()).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventMouseDragEvent(ScreenEvent.MouseDragged.Post event) {
        ClientScreenInputEvent.MOUSE_DRAGGED_POST.invoker().mouseDragged(Minecraft.getInstance(), event.getScreen(), mouseButtonEvent(event.getMouseX(), event.getMouseY(), event.getMouseButton()), event.getDragX(), event.getDragY());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventMouseReleasedEvent(ScreenEvent.MouseButtonReleased.Pre event) {
        return ClientScreenInputEvent.MOUSE_RELEASED_PRE.invoker().mouseReleased(Minecraft.getInstance(), event.getScreen(), mouseButtonEvent(event.getMouseX(), event.getMouseY(), event.getButton())).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventMouseReleasedEvent(ScreenEvent.MouseButtonReleased.Post event) {
        ClientScreenInputEvent.MOUSE_RELEASED_POST.invoker().mouseReleased(Minecraft.getInstance(), event.getScreen(), mouseButtonEvent(event.getMouseX(), event.getMouseY(), event.getButton()));
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventKeyboardCharTypedEvent(ScreenEvent.CharacterTyped.Pre event) {
        return ClientScreenInputEvent.CHAR_TYPED_PRE.invoker().charTyped(Minecraft.getInstance(), event.getScreen(), event.getInfo()).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventKeyboardCharTypedEvent(ScreenEvent.CharacterTyped.Post event) {
        ClientScreenInputEvent.CHAR_TYPED_POST.invoker().charTyped(Minecraft.getInstance(), event.getScreen(), event.getInfo());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventKeyboardKeyPressedEvent(ScreenEvent.KeyPressed.Pre event) {
        return ClientScreenInputEvent.KEY_PRESSED_PRE.invoker().keyPressed(Minecraft.getInstance(), event.getScreen(), event.getInfo()).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventKeyboardKeyPressedEvent(ScreenEvent.KeyPressed.Post event) {
        return ClientScreenInputEvent.KEY_PRESSED_POST.invoker().keyPressed(Minecraft.getInstance(), event.getScreen(), event.getInfo()).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventKeyboardKeyReleasedEvent(ScreenEvent.KeyReleased.Pre event) {
        return ClientScreenInputEvent.KEY_RELEASED_PRE.invoker().keyReleased(Minecraft.getInstance(), event.getScreen(), event.getInfo()).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventKeyboardKeyReleasedEvent(ScreenEvent.KeyReleased.Post event) {
        return ClientScreenInputEvent.KEY_RELEASED_POST.invoker().keyReleased(Minecraft.getInstance(), event.getScreen(), event.getInfo()).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventInputEvent(InputEvent.MouseScrollingEvent event) {
        return ClientRawInputEvent.MOUSE_SCROLLED.invoker().mouseScrolled(Minecraft.getInstance(), event.getDeltaX(), event.getDeltaY()).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static boolean eventInputEvent(InputEvent.MouseButton.Pre event) {
        return ClientRawInputEvent.MOUSE_CLICKED_PRE.invoker().mouseClicked(Minecraft.getInstance(), event.getInfo(), event.getAction()).isFalse();
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventInputEvent(InputEvent.MouseButton.Post event) {
        ClientRawInputEvent.MOUSE_CLICKED_POST.invoker().mouseClicked(Minecraft.getInstance(), event.getInfo(), event.getAction());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void eventInputEvent(InputEvent.Key event) {
        ClientRawInputEvent.KEY_PRESSED.invoker().keyPressed(Minecraft.getInstance(), event.getAction(), event.getInfo());
    }

    @SubscribeEvent(priority = Priority.HIGH)
    public static void event(RegisterClientCommandsEvent event) {
        ClientCommandRegistrationEvent.EVENT.invoker().register((CommandDispatcher<ClientCommandRegistrationEvent.ClientCommandSourceStack>)
                (CommandDispatcher<?>) event.getDispatcher(), event.getBuildContext());
    }

    @OnlyIn(Dist.CLIENT)
    public static class ModBasedEventHandler {
        @SubscribeEvent(priority = Priority.HIGH)
        public static void event(FMLClientSetupEvent event) {
            ClientLifecycleEvent.CLIENT_SETUP.invoker().stateChanged(Minecraft.getInstance());
        }
    }

    private static MouseButtonEvent mouseButtonEvent(double x, double y, int button) {
        return new MouseButtonEvent(x, y, new MouseButtonInfo(button, 0));
    }
}
