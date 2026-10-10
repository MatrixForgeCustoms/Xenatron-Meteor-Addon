package com.example.addon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import com.example.addon.modules.ZoneIntrusionAlert;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class XenatronCommand extends Command {
    public XenatronCommand() {
        super("xenatron", "Shows Xenatron help and player status.");
    }

    @Override
    public void build(LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.executes(context -> {
            info("Use .xenatron coords, nether, health, or armor.");
            return SINGLE_SUCCESS;
        });

        builder.then(literal("coords").executes(context -> {
            if (mc.player == null || mc.level == null) {
                warning("Join a world first.");
                return SINGLE_SUCCESS;
            }

            BlockPos pos = mc.player.blockPosition();
            info("Position: X " + pos.getX() + ", Y " + pos.getY() + ", Z " + pos.getZ());
            return SINGLE_SUCCESS;
        }));

        builder.then(literal("nether").executes(context -> {
            if (mc.player == null) {
                warning("Join a world first.");
                return SINGLE_SUCCESS;
            }

            BlockPos pos = mc.player.blockPosition();
            boolean inNether = mc.level.dimension().equals(Level.NETHER);
            int x = inNether ? pos.getX() * 8 : Math.floorDiv(pos.getX(), 8);
            int z = inNether ? pos.getZ() * 8 : Math.floorDiv(pos.getZ(), 8);
            info((inNether ? "Overworld" : "Nether") + " coordinates: X " + x + ", Z " + z);
            return SINGLE_SUCCESS;
        }));

        builder.then(literal("health").executes(context -> {
            if (mc.player == null) {
                warning("Join a world first.");
                return SINGLE_SUCCESS;
            }

            info("Health: " + mc.player.getHealth() / 2.0f + " hearts | Food: " + mc.player.getFoodData().getFoodLevel() + "/20");
            return SINGLE_SUCCESS;
        }));

        builder.then(literal("armor").executes(context -> {
            if (mc.player == null) {
                warning("Join a world first.");
                return SINGLE_SUCCESS;
            }

            info("Head " + durability(EquipmentSlot.HEAD)
                + " | Chest " + durability(EquipmentSlot.CHEST)
                + " | Legs " + durability(EquipmentSlot.LEGS)
                + " | Feet " + durability(EquipmentSlot.FEET));
            return SINGLE_SUCCESS;
        }));

        builder.then(literal("zone")
            .then(literal("pos1").executes(context -> setZoneCorner(1)))
            .then(literal("pos2").executes(context -> setZoneCorner(2)))
            .then(literal("status").executes(context -> {
                info(ZoneIntrusionAlert.zoneDescription());
                return SINGLE_SUCCESS;
            }))
            .then(literal("clear").executes(context -> {
                ZoneIntrusionAlert.clearZone();
                info("Zone cleared.");
                return SINGLE_SUCCESS;
            }))
        );
    }

    private int setZoneCorner(int corner) {
        if (mc.player == null || mc.level == null) {
            warning("Join a world first.");
            return SINGLE_SUCCESS;
        }

        if (!ZoneIntrusionAlert.setCorner(corner, mc.player.blockPosition(), mc.level.dimension())) {
            warning("Both zone corners must be in the same dimension. Clear the old zone first if needed.");
            return SINGLE_SUCCESS;
        }

        info("Zone corner " + corner + " set to " + mc.player.blockPosition().toShortString() + ".");
        return SINGLE_SUCCESS;
    }

    private String durability(EquipmentSlot slot) {
        ItemStack stack = mc.player.getItemBySlot(slot);
        if (stack.isEmpty()) return "empty";
        if (!stack.isDamageableItem()) return "ok";
        int remaining = stack.getMaxDamage() - stack.getDamageValue();
        return Math.round(remaining * 100.0 / stack.getMaxDamage()) + "%";
    }
}


