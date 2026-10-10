package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class HungerAlert extends Module {
    private final SettingGroup sg = settings.getDefaultGroup();
    private final Setting<Integer> threshold = sg.add(new IntSetting.Builder().name("food-level").description("Warn when food level is at or below this value.").defaultValue(6).min(1).max(20).build());
    private boolean warned;

    public HungerAlert() { super(AddonTemplate.XENATRON_SMP, "hunger-alert", "Warns when your hunger bar gets low."); }
    @Override public void onActivate() { warned = false; }

    @EventHandler private void onTick(TickEvent.Pre event) {
        if (mc.player == null) return;
        boolean low = mc.player.getFoodData().getFoodLevel() <= threshold.get();
        if (low && !warned) warning("Hunger is low: " + mc.player.getFoodData().getFoodLevel() + "/20.");
        warned = low;
    }
}
