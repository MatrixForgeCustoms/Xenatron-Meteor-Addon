package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class FallDistanceAlert extends Module {
    private final SettingGroup sg = settings.getDefaultGroup();
    private final Setting<Double> threshold = sg.add(new DoubleSetting.Builder().name("fall-distance").description("Warn when accumulated fall distance reaches this many blocks.").defaultValue(8.0).min(2.0).max(40.0).sliderMin(2.0).sliderMax(20.0).build());
    private boolean warned;

    public FallDistanceAlert() { super(AddonTemplate.XENATRON_SMP, "fall-distance-alert", "Warns once when a fall becomes dangerous."); }
    @Override public void onActivate() { warned = false; }

    @EventHandler private void onTick(TickEvent.Pre event) {
        if (mc.player == null) return;
        boolean falling = mc.player.fallDistance >= threshold.get();
        if (falling && !warned) warning("Dangerous fall: " + (int) mc.player.fallDistance + " blocks.");
        warned = falling;
    }
}
