package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;

public class ChatSilentMode extends Module {

    public ChatSilentMode() {
        super(AddonTemplate.XENATRON_SECURITY, "chat-silent-mode", "Blocks suspicious or hidden staff chat packets to prevent cheat detection.");
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event) {
        if (mc.player == null) return;

        if (event.packet instanceof ClientboundSystemChatPacket packet) {
            String chatMessage = packet.content().getString();

            if (chatMessage.contains("§d") || chatMessage.contains("[Staff]") || chatMessage.contains("Screenshare")) {
                event.cancel();
                info("§c[Xenatron] Blocked a suspicious server chat packet!");
            }
        }
    }
}
