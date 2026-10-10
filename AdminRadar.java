package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.world.level.GameType;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AdminRadar extends Module {
    private final Set<UUID> knownSpectators = new HashSet<>();

    public AdminRadar() {
        super(AddonTemplate.XENATRON_SECURITY, "admin-radar", "Reports player-list updates that identify another player as a spectator. It cannot detect staff or vanish reliably.");
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event) {
        if (mc.player == null || mc.level == null) return;

        if (event.packet instanceof ClientboundPlayerInfoUpdatePacket) {
            ClientboundPlayerInfoUpdatePacket packet = (ClientboundPlayerInfoUpdatePacket) event.packet;
            
            for (ClientboundPlayerInfoUpdatePacket.Entry entry : packet.entries()) {
                // Prüfen, ob der Spieler im Spectator-Modus ist
                if (entry.gameMode() == GameType.SPECTATOR) {
                    
                    // Moderner 1.21.11 Fix: Name über .profile().name() auslesen
                    if (entry.profile() != null) {
                        String playername = entry.profile().name();
                        
                        // Moderner 1.21.11 Fix: Eigener Name über .name() auslesen
                        String myName = mc.player.getGameProfile().name();
                        
                        UUID id = entry.profile().id();
                        if (playername != null && !playername.equalsIgnoreCase(myName) && knownSpectators.add(id)) {
                            info(playername + " was reported in spectator mode by the player list.");
                        }
                    }
                }
            }
        }
    }

    @Override public void onActivate() { knownSpectators.clear(); }
    @Override public void onDeactivate() { knownSpectators.clear(); }
}


