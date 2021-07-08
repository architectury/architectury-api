package me.shedaniel.architectury.test.networking;

import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.networking.simple.BaseC2SPacket;
import me.shedaniel.architectury.networking.simple.PacketID;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;

public class ButtonClickedPacket extends BaseC2SPacket {
    private final int buttonId;
    
    /**
     * To send this packet from client to server, call new ButtonClickedPacket(id).sendToServer()
     */
    public ButtonClickedPacket(int id) {
        buttonId = id;
    }
    
    public ButtonClickedPacket(FriendlyByteBuf buf) {
        buttonId = buf.readVarInt();
    }
    
    @Override
    public PacketID getId() {
        return TestModNet.BUTTON_CLICKED;
    }
    
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(buttonId);
    }
    
    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.getPlayer().sendMessage(new TextComponent("You clicked button #" + buttonId), Util.NIL_UUID);
    }
}
