package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class LowHealthAlert extends Module {
    private final SettingGroup sg = settings.getDefaultGroup();
    private final Setting<Integer> threshold = sg.add(new IntSetting.Builder().name("health").description("Warn when health reaches this many hearts.").defaultValue(4).min(1).max(10).build());
    private boolean warned;

    public LowHealthAlert() { super(AddonTemplate.XENATRON_SMP, "low-health-alert", "Warns once when your health drops below the selected number of hearts."); }
    @Override public void onActivate() { warned = false; }

    @EventHandler private void onTick(TickEvent.Pre event) {
        if (mc.player == null) return;
        boolean low = mc.player.getHealth() <= threshold.get() * 2.0f;
        if (low && !warned) warning("Health is low: " + mc.player.getHealth() / 2.0f + " hearts.");
        warned = low;
    }
}
