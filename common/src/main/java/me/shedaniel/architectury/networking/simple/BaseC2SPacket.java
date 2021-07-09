package me.shedaniel.architectury.networking.simple;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

/**
 * The base class for client -&gt; server packets managed by a {@link SimpleNetworkManager}.
 *
 * @author LatvianModder
 */
public abstract class BaseC2SPacket extends BasePacket {
    /**
     * Sends this packet to the server.
     */
    @Environment(EnvType.CLIENT)
    public final void sendToServer() {
        if (Minecraft.getInstance().getConnection() != null) {
            Minecraft.getInstance().getConnection().send(toPacket());
        } else {
            throw new IllegalStateException("Unable to send packet to the server while not in game!");
        }
    }
}
