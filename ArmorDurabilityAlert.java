package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class ArmorDurabilityAlert extends Module {
    private final SettingGroup sg = settings.getDefaultGroup();
    private final Setting<Integer> threshold = sg.add(new IntSetting.Builder().name("durability-percent").description("Warn when any armor piece has this percentage of durability or less.").defaultValue(15).min(1).max(50).build());
    private boolean warned;
    private final EquipmentSlot[] slots = { EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD };

    public ArmorDurabilityAlert() { super(AddonTemplate.XENATRON_SMP, "armor-durability-alert", "Warns before worn armor or an Elytra is close to breaking."); }
    @Override public void onActivate() { warned = false; }

    @EventHandler private void onTick(TickEvent.Pre event) {
        if (mc.player == null) return;
        boolean low = false;
        for (EquipmentSlot slot : slots) {
            ItemStack stack = mc.player.getItemBySlot(slot);
            if (!stack.isEmpty() && stack.isDamageableItem()) {
                int remaining = stack.getMaxDamage() - stack.getDamageValue();
                if (remaining * 100.0 / stack.getMaxDamage() <= threshold.get()) { low = true; break; }
            }
        }
        if (low && !warned) warning("Armor or Elytra durability is low.");
        warned = low;
    }
}
