/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.shedaniel.architectury.mixin.fabric;

import me.shedaniel.architectury.event.events.ChatEvent;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl {
    @Shadow public ServerPlayer player;
    
    @Shadow @Final private MinecraftServer server;
    
    @Shadow private int chatSpamTickCount;
    
    @Shadow
    public abstract void disconnect(Component component);
    
    @Inject(method = "handleChat", at = @At(value = "INVOKE",
                                            target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"),
            cancellable = true)
    private void handleChat(ServerboundChatPacket packet, CallbackInfo ci) {
        String string = StringUtils.normalizeSpace(packet.getMessage());
        Component component = new TranslatableComponent("chat.type.text", this.player.getDisplayName(), string);
        InteractionResultHolder<Component> process = ChatEvent.SERVER.invoker().process(this.player, string, component);
        if (process.getResult() == InteractionResult.FAIL)
            ci.cancel();
        else if (process.getObject() != null && !process.getObject().equals(component)) {
            this.server.getPlayerList().broadcastMessage(component, ChatType.CHAT, this.player.getUUID());
            
            this.chatSpamTickCount += 20;
            if (this.chatSpamTickCount > 200 && !this.server.getPlayerList().isOp(this.player.getGameProfile())) {
                this.disconnect(new TranslatableComponent("disconnect.spam"));
            }
            ci.cancel();
        }
    }
}
