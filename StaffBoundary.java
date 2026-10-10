package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.entity.player.Player;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StaffBoundary extends Module {
    private final Set<UUID> warnedPlayers = new HashSet<>();
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    @SuppressWarnings("all")
    private final Setting<Double> safetyDistance = sgGeneral.add(new DoubleSetting.Builder()
        .name("safety-distance")
        .description("The distance within which nearby spectators trigger a warning.")
        .defaultValue(6.0d)
        .min(2.0d)
        .max(10.0d)
        .sliderMin(2.0d)
        .sliderMax(10.0d)
        .build()
    );

    public StaffBoundary() {
        super(AddonTemplate.XENATRON_SECURITY, "staff-boundary", "Warns once when a nearby player is reported as a spectator; this cannot verify staff status or invisibility.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.level == null) return;

        Set<UUID> nearby = new HashSet<>();

        for (Player targetPlayer : mc.level.players()) {
            if (targetPlayer == mc.player) continue;

            double distance = mc.player.distanceTo(targetPlayer);
            if (distance <= safetyDistance.get() && targetPlayer.isSpectator()) {
                UUID id = targetPlayer.getUUID();
                nearby.add(id);
                if (warnedPlayers.add(id)) warning("Nearby spectator: " + targetPlayer.getGameProfile().name());
            }
        }
        warnedPlayers.retainAll(nearby);
    }

    @Override public void onActivate() { warnedPlayers.clear(); }
    @Override public void onDeactivate() { warnedPlayers.clear(); }
}
