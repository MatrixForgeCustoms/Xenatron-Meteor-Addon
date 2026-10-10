package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.item.Items;

public class LowRedstoneStockAlert extends Module {
    private final SettingGroup sg = settings.getDefaultGroup();
    private final Setting<Integer> threshold = sg.add(new IntSetting.Builder().name("minimum-dust").description("Warn when redstone dust count is at or below this amount.").defaultValue(16).min(0).max(64).build());
    private boolean warned;

    public LowRedstoneStockAlert() { super(AddonTemplate.XENATRON_REDSTONE, "low-redstone-stock-alert", "Warns when your redstone dust supply runs low."); }
    @Override public void onActivate() { warned = false; }

    @EventHandler private void onTick(TickEvent.Pre event) {
        if (mc.player == null) return;
        int count = InvUtils.find(Items.REDSTONE).count();
        boolean low = count <= threshold.get();
        if (low && !warned) warning("Low redstone dust: " + count + ".");
        warned = low;
    }
}
