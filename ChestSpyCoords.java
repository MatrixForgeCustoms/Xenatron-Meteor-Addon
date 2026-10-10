package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;

public class ChestSpyCoords extends Module {
    public ChestSpyCoords() {
        super(AddonTemplate.XENATRON_SMP, "container-open-alert", "Reports the title of a screen opened for you; it cannot identify other players' containers or block coordinates.");
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event) {
        if (mc.player == null || !(event.packet instanceof ClientboundOpenScreenPacket packet)) return;
        info("Screen opened for you: '" + packet.getTitle().getString() + "'. The packet contains no container coordinates.");
    }
}
