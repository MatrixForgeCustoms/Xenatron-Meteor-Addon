package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class InventoryFullAlert extends Module {
    private boolean warned;

    public InventoryFullAlert() { super(AddonTemplate.XENATRON_SMP, "inventory-full-alert", "Warns when your main inventory has no empty slots."); }
    @Override public void onActivate() { warned = false; }

    @EventHandler private void onTick(TickEvent.Pre event) {
        if (mc.player == null) return;
        boolean full = true;
        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getItem(i).isEmpty()) { full = false; break; }
        }
        if (full && !warned) warning("Your inventory is full.");
        warned = full;
    }
}

