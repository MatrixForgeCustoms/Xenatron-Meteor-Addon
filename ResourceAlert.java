package com.example.addon.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** Shared threshold and one-shot/re-arm behavior for Xenatron's resource alerts. */
public abstract class ResourceAlert extends Module {
    public enum Metric {
        AIR, SATURATION, EXPERIENCE, FOOD, LOW_HEIGHT,
        TOTEM, PEARL, GOLDEN_APPLE, OBSIDIAN, WATER_BUCKET, LAVA_BUCKET,
        TORCH, ARROW, BUILDING_BLOCK, ENDER_CHEST, REDSTONE, REPEATER,
        ELYTRA_DURABILITY, SHIELD_DURABILITY, PICKAXE_DURABILITY,
        BONE, STRING, GUNPOWDER, SLIME_BALL, PISTON, HELMET_DURABILITY
    }

    private final Metric metric;
    private final Setting<Integer> threshold;
    private boolean warned;

    protected ResourceAlert(Category category, String name, String description, Metric metric, int defaultThreshold) {
        super(category, name, description);
        this.metric = metric;
        SettingGroup group = settings.getDefaultGroup();
        this.threshold = group.add(new IntSetting.Builder().name("threshold")
            .description("Warn when the measured value reaches this threshold.")
            .defaultValue(defaultThreshold).min(0).max(256).build());
    }

    @Override public void onActivate() { warned = false; }

    @EventHandler private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.level == null) return;
        int value = value();
        boolean low = value <= threshold.get();
        if (low && !warned) warning(label() + " is low: " + value + ".");
        warned = low;
    }

    private int value() {
        return switch (metric) {
            case AIR -> mc.player.getAirSupply();
            case SATURATION -> (int) mc.player.getFoodData().getSaturationLevel();
            case EXPERIENCE -> mc.player.experienceLevel;
            case FOOD -> mc.player.getFoodData().getFoodLevel();
            case LOW_HEIGHT -> mc.player.blockPosition().getY();
            case TOTEM -> count(Items.TOTEM_OF_UNDYING);
            case PEARL -> count(Items.ENDER_PEARL);
            case GOLDEN_APPLE -> count(Items.GOLDEN_APPLE);
            case OBSIDIAN -> count(Items.OBSIDIAN);
            case WATER_BUCKET -> count(Items.WATER_BUCKET);
            case LAVA_BUCKET -> count(Items.LAVA_BUCKET);
            case TORCH -> count(Items.TORCH);
            case ARROW -> count(Items.ARROW);
            case BUILDING_BLOCK -> countBlocks();
            case ENDER_CHEST -> count(Items.ENDER_CHEST);
            case REDSTONE -> count(Items.REDSTONE);
            case REPEATER -> count(Items.REPEATER);
            case ELYTRA_DURABILITY -> durability(EquipmentSlot.CHEST);
            case SHIELD_DURABILITY -> durability(EquipmentSlot.OFFHAND);
            case PICKAXE_DURABILITY -> bestPickaxeDurability();
            case BONE -> count(Items.BONE);
            case STRING -> count(Items.STRING);
            case GUNPOWDER -> count(Items.GUNPOWDER);
            case SLIME_BALL -> count(Items.SLIME_BALL);
            case PISTON -> count(Items.PISTON);
            case HELMET_DURABILITY -> durability(EquipmentSlot.HEAD);
        };
    }

    private int count(Item item) {
        int total = 0;
        for (int i = 0; i < mc.player.getInventory().getContainerSize(); i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.is(item)) total += stack.getCount();
        }
        return total;
    }

    private int countBlocks() {
        int total = 0;
        for (int i = 0; i < mc.player.getInventory().getContainerSize(); i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() instanceof net.minecraft.world.item.BlockItem) total += stack.getCount();
        }
        return total;
    }

    private int durability(EquipmentSlot slot) {
        ItemStack stack = mc.player.getItemBySlot(slot);
        if (stack.isEmpty() || !stack.isDamageableItem()) return 0;
        return Math.max(0, stack.getMaxDamage() - stack.getDamageValue());
    }

    private int bestPickaxeDurability() {
        int best = 0;
        for (int i = 0; i < mc.player.getInventory().getContainerSize(); i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (isPickaxe(stack) && stack.isDamageableItem()) {
                best = Math.max(best, stack.getMaxDamage() - stack.getDamageValue());
            }
        }
        return best;
    }

    private boolean isPickaxe(ItemStack stack) {
        return stack.is(Items.WOODEN_PICKAXE) || stack.is(Items.STONE_PICKAXE) || stack.is(Items.IRON_PICKAXE)
            || stack.is(Items.GOLDEN_PICKAXE) || stack.is(Items.DIAMOND_PICKAXE) || stack.is(Items.NETHERITE_PICKAXE);
    }

    private String label() {
        return switch (metric) {
            case AIR -> "Air supply"; case SATURATION -> "Saturation"; case EXPERIENCE -> "Experience levels";
            case FOOD -> "Food level"; case LOW_HEIGHT -> "Y level"; case TOTEM -> "Totem stock";
            case PEARL -> "Ender pearl stock"; case GOLDEN_APPLE -> "Golden apple stock"; case OBSIDIAN -> "Obsidian stock";
            case WATER_BUCKET -> "Water bucket stock"; case LAVA_BUCKET -> "Lava bucket stock"; case TORCH -> "Torch stock";
            case ARROW -> "Arrow stock"; case BUILDING_BLOCK -> "Building block stock"; case ENDER_CHEST -> "Ender chest stock";
            case REDSTONE -> "Redstone dust stock"; case REPEATER -> "Repeater stock"; case ELYTRA_DURABILITY -> "Chest equipment durability";
            case SHIELD_DURABILITY -> "Offhand durability"; case PICKAXE_DURABILITY -> "Best pickaxe durability";
            case BONE -> "Bone stock"; case STRING -> "String stock"; case GUNPOWDER -> "Gunpowder stock";
            case SLIME_BALL -> "Slimeball stock"; case PISTON -> "Piston stock"; case HELMET_DURABILITY -> "Helmet durability";
        };
    }
}
