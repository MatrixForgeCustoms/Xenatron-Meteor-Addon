package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils; // Added Meteor InvUtils
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;

public class AutoPearlSaver extends Module {
    private int previousSlot = -1;
    private float previousPitch;
    private boolean changedPitch;

    public AutoPearlSaver() {
        super(AddonTemplate.XENATRON_SMP, "auto-pearl-saver", "Automatically throws an ender pearl upwards if you fall into danger.");
    }

    @Override public void onActivate() {
        previousSlot = -1;
        changedPitch = false;
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.gameMode == null || mc.player.containerMenu != mc.player.inventoryMenu) return;

        // Trigger if player is falling rapidly or inside dangerous fluids like lava
        if (mc.player.fallDistance > 3.0f || mc.player.isInLava()) {
            for (int i = 0; i < 9; i++) {
                if (mc.player.getInventory().getItem(i).is(Items.ENDER_PEARL)) {
                    
                    // Modern 1.21.11 Fix: Safe slot swap using Meteor's native utility
                    previousSlot = mc.player.getInventory().getSelectedSlot();
                    previousPitch = mc.player.getXRot();
                    changedPitch = true;
                    InvUtils.swap(i, false);

                    // Force look upwards to escape pits safely
                    mc.player.setXRot(-90.0f);

                    // Send the interaction right-click packet to throw it
                    mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
                    mc.player.swing(InteractionHand.MAIN_HAND);
                    
                    info("§c[Xenatron] Emergency! Threw Pearl to save you.");
                    this.toggle(); // Turn off to prevent spamming all pearls
                    break;
                }
            }
        }
    }

    @Override public void onDeactivate() {
        if (mc.player != null) {
            if (previousSlot >= 0) InvUtils.swap(previousSlot, false);
            if (changedPitch) mc.player.setXRot(previousPitch);
        }
        previousSlot = -1;
        changedPitch = false;
    }
}
