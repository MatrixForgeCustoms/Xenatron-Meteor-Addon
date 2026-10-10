package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerActivityAlert extends Module {
    private final Set<UUID> knownPlayers = new HashSet<>();

    public PlayerActivityAlert() { super(AddonTemplate.XENATRON_SMP, "player-activity-alert", "Notifies when another player entity enters your currently loaded area."); }

    @Override public void onActivate() {
        knownPlayers.clear();
        if (mc.level != null) for (Player player : mc.level.players()) {
            if (player != mc.player) knownPlayers.add(player.getUUID());
        }
    }

    @Override public void onDeactivate() { knownPlayers.clear(); }

    @EventHandler private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.level == null) return;
        Set<UUID> currentPlayers = new HashSet<>();
        for (Player player : mc.level.players()) {
            if (player == mc.player) continue;
            UUID id = player.getUUID();
            currentPlayers.add(id);
            if (knownPlayers.add(id)) info(player.getGameProfile().name() + " appeared in your loaded area.");
        }
        for (UUID id : new HashSet<>(knownPlayers)) {
            if (!currentPlayers.contains(id)) knownPlayers.remove(id);
        }
    }
}


