package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class InventoryPanic extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    // 1. Min amount slider configuration
    @SuppressWarnings("all")
    private final Setting<Integer> minStackSize = sgGeneral.add(new IntSetting.Builder()
        .name("min-amount")
        .description("Only drop items if the stack size is greater or equal to this value.")
        .defaultValue(1)
        .min(1)
        .max(64)
        .sliderMin(1)
        .sliderMax(64)
        .build()
    );

    // 2. Toggle switches for specific item types
    @SuppressWarnings("all")
    private final Setting<Boolean> dropDiamonds = sgGeneral.add(new BoolSetting.Builder()
        .name("drop-diamonds")
        .description("Drop diamonds and diamond blocks.")
        .defaultValue(true)
        .build()
    );

    @SuppressWarnings("all")
    private final Setting<Boolean> dropNetherite = sgGeneral.add(new BoolSetting.Builder()
        .name("drop-netherite")
        .description("Drop netherite ingots and blocks.")
        .defaultValue(true)
        .build()
    );

    @SuppressWarnings("all")
    private final Setting<Boolean> dropIllegals = sgGeneral.add(new BoolSetting.Builder()
        .name("drop-illegals")
        .description("Drop illegal server items like TNT and Spawners.")
        .defaultValue(true)
        .build()
    );

    public InventoryPanic() {
        super(AddonTemplate.XENATRON_SECURITY, "inventory-panic", "Instantly drops configured suspicious items from your inventory.");
    }

    @Override
    public void onActivate() {
        if (mc.player == null) {
            this.toggle();
            return;
        }

        // Only click the player's 36 inventory slots; do not act on a container screen or armor slots.
        if (mc.player.containerMenu != mc.player.inventoryMenu) {
            warning("Close containers before using Inventory Panic.");
            toggle();
            return;
        }
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);

            // Check if the item matches the custom toggle filters and the min-amount slider
            if (isSuspicious(stack) && stack.getCount() >= minStackSize.get()) {
                // Instantly drop the item using Meteor's safe InvUtils
                InvUtils.drop().slot(i);
            }
        }

        info("§c[Xenatron] Inventory panic executed! Cleared items based on configuration.");
        this.toggle();
    }

    private boolean isSuspicious(ItemStack stack) {
        if (stack.isEmpty()) return false;
        
        // Filter diamonds based on toggle setting
        if (dropDiamonds.get() && (stack.is(Items.DIAMOND) || stack.is(Items.DIAMOND_BLOCK))) {
            return true;
        }

        // Filter netherite based on toggle setting
        if (dropNetherite.get() && (stack.is(Items.NETHERITE_INGOT) || stack.is(Items.NETHERITE_BLOCK))) {
            return true;
        }

        // Filter illegal/grief items based on toggle setting
        if (dropIllegals.get() && (stack.is(Items.TNT) || stack.is(Items.SPAWNER))) {
            return true;
        }

        return false;
    }
}
