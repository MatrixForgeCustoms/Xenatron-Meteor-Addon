package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SafeEnderChest extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    // 1. Min amount slider
    @SuppressWarnings("all")
    private final Setting<Integer> minAmount = sgGeneral.add(new IntSetting.Builder()
        .name("min-amount")
        .description("Only store items if the stack size is greater or equal to this value.")
        .defaultValue(1)
        .min(1)
        .max(64)
        .sliderMin(1)
        .sliderMax(64)
        .build()
    );

    // 2. Toggles for specific high-value gear
    @SuppressWarnings("all")
    private final Setting<Boolean> storeTotems = sgGeneral.add(new BoolSetting.Builder()
        .name("store-totems")
        .description("Automatically secure Totems of Undying.")
        .defaultValue(true)
        .build()
    );

    @SuppressWarnings("all")
    private final Setting<Boolean> storeElytra = sgGeneral.add(new BoolSetting.Builder()
        .name("store-elytra")
        .description("Automatically secure Elytras.")
        .defaultValue(true)
        .build()
    );

    @SuppressWarnings("all")
    private final Setting<Boolean> storeValuables = sgGeneral.add(new BoolSetting.Builder()
        .name("store-valuables")
        .description("Automatically secure raw diamonds and netherite.")
        .defaultValue(false)
        .build()
    );

    public SafeEnderChest() {
        super(AddonTemplate.XENATRON_SMP, "safe-ender-chest", "Moves one configured valuable at a time, only while an Ender Chest screen is open.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.gameMode == null) return;
        if (!(mc.player.containerMenu instanceof ChestMenu menu) || mc.screen == null) return;
        String enderChestTitle = Component.translatable("container.enderchest").getString();
        if (!mc.screen.getTitle().getString().equals(enderChestTitle)) return;

        // Menu slots 27-53 correspond to inventory slots 9-35, and 54-62 to hotbar slots 0-8.
        for (int inventorySlot = 0; inventorySlot < 36; inventorySlot++) {
            ItemStack stack = mc.player.getInventory().getItem(inventorySlot);
            if (!isValuable(stack) || stack.getCount() < minAmount.get()) continue;
            int menuSlot = inventorySlot < 9 ? inventorySlot + 54 : inventorySlot + 18;
            mc.gameMode.handleInventoryMouseClick(menu.containerId, menuSlot, 0, ClickType.QUICK_MOVE, mc.player);
            return;
        }

        info("Valuables moved to the open Ender Chest.");
        toggle();
    }

    private boolean isValuable(ItemStack stack) {
        if (stack.isEmpty()) return false;

        if (storeTotems.get() && stack.is(Items.TOTEM_OF_UNDYING)) return true;
        if (storeElytra.get() && stack.is(Items.ELYTRA)) return true;
        
        if (storeValuables.get() && (
            stack.is(Items.DIAMOND) 
            || stack.is(Items.DIAMOND_BLOCK) 
            || stack.is(Items.NETHERITE_INGOT) 
            || stack.is(Items.NETHERITE_BLOCK)
        )) {
            return true;
        }

        return false;
    }
}
