package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class ArmorDurabilityPanic extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    @SuppressWarnings("all")
    private final Setting<Integer> durabilityPercent = sgGeneral.add(new IntSetting.Builder()
        .name("durability-trigger")
        .description("Unequip armor when its durability drops below this percentage.")
        .defaultValue(5)
        .min(1)
        .max(20)
        .sliderMin(1)
        .sliderMax(20)
        .build()
    );

    // Array containing all four vanilla armor slots
    private final EquipmentSlot[] slots = {
        EquipmentSlot.FEET,
        EquipmentSlot.LEGS,
        EquipmentSlot.CHEST,
        EquipmentSlot.HEAD
    };

    public ArmorDurabilityPanic() {
        super(AddonTemplate.XENATRON_SMP, "armor-panic", "Automatically unequips armor pieces before they break completely.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.player.containerMenu != mc.player.inventoryMenu) return;

        // Loop through all armor slots using vanilla EquipmentSlots
        for (int i = 0; i < slots.length; i++) {
            ItemStack armorStack = mc.player.getItemBySlot(slots[i]);

            if (!armorStack.isEmpty() && armorStack.isDamageableItem()) {
                int maxDamage = armorStack.getMaxDamage();
                int currentDamage = armorStack.getDamageValue();
                int remainingDurability = maxDamage - currentDamage;

                double percent = ((double) remainingDurability / maxDamage) * 100.0;

                if (percent <= durabilityPercent.get()) {
                    // Mappings for shifting armor slots to main inventory
                    int containerSlot = 8 - i; 
                    InvUtils.shiftClick().slot(containerSlot);
                    info("§c[Xenatron] Safety Panic! Unequipped armor slot to prevent breakage.");
                }
            }
        }
    }
}
