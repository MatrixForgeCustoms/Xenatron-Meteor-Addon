package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class FireSafetyAlert extends Module {
    private boolean warned;

    public FireSafetyAlert() { super(AddonTemplate.XENATRON_SMP, "fire-safety-alert", "Warns when you catch fire or enter lava."); }
    @Override public void onActivate() { warned = false; }

    @EventHandler private void onTick(TickEvent.Pre event) {
        if (mc.player == null) return;
        boolean danger = mc.player.isOnFire() || mc.player.isInLava();
        if (danger && !warned) warning(mc.player.isInLava() ? "You are in lava!" : "You are on fire!");
        warned = danger;
    }
}
