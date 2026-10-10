package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import com.example.addon.util.ClientScreenState;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/** Places a straight, supported row of a selected redstone component, one block at a time. */
public class RedstoneBuilder extends Module {
    public enum Component { RedstoneBlock, Repeater, Comparator, RedstoneDust, RedstoneTorch }

    private final SettingGroup sg = settings.getDefaultGroup();
    private final Setting<Component> component = sg.add(new EnumSetting.Builder<Component>()
        .name("component").description("Redstone component to place in the row.").defaultValue(Component.RedstoneDust).build());
    private final Setting<Integer> length = sg.add(new IntSetting.Builder()
        .name("length").description("Number of positions to build.").defaultValue(8).min(1).max(64).build());
    private final Setting<Integer> delay = sg.add(new IntSetting.Builder()
        .name("delay").description("Ticks between placement attempts.").defaultValue(4).min(1).max(40).build());
    private final Setting<Integer> startDistance = sg.add(new IntSetting.Builder()
        .name("start-distance").description("Distance in front of you to start the row.").defaultValue(2).min(1).max(5).build());
    private final Setting<Boolean> pauseForScreens = sg.add(new BoolSetting.Builder()
        .name("pause-for-screens").description("Pause while a menu or container screen is open.").defaultValue(true).build());
    private final Setting<Boolean> stopIfUnsupported = sg.add(new BoolSetting.Builder()
        .name("stop-if-unsupported").description("Stop instead of trying to place without a sturdy neighboring block.").defaultValue(true).build());

    private int placed;
    private int tickWait;
    private int previousSlot = -1;
    private int pendingWait;
    private int failedAttempts;
    private boolean outOfReachWarned;
    private BlockPos pending;
    private BlockPos origin;
    private Direction buildDirection;

    public RedstoneBuilder() {
        super(AddonTemplate.XENATRON_REDSTONE, "redstone-builder", "Builds a straight row of a selected redstone component, with inventory and support checks.");
    }

    @Override public void onActivate() {
        placed = 0;
        tickWait = 0;
        pending = null;
        pendingWait = 0;
        failedAttempts = 0;
        outOfReachWarned = false;
        origin = mc.player == null ? null : mc.player.blockPosition();
        buildDirection = mc.player == null ? null : mc.player.getDirection();
        previousSlot = mc.player == null ? -1 : mc.player.getInventory().getSelectedSlot();
    }

    @Override public void onDeactivate() {
        if (mc.player != null && previousSlot >= 0) InvUtils.swap(previousSlot, false);
        previousSlot = -1;
        pending = null;
    }

    @EventHandler private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.level == null || mc.gameMode == null) return;
        if (origin == null) {
            origin = mc.player.blockPosition();
            buildDirection = mc.player.getDirection();
        }
        if (pauseForScreens.get() && ClientScreenState.isOpen()) return;
        if (pending != null) {
            if (!mc.level.getBlockState(pending).isAir()) {
                placed++;
                failedAttempts = 0;
                pending = null;
                pendingWait = 0;
                if (placed >= length.get()) {
                    info("Build complete: " + placed + " blocks.");
                    toggle();
                }
            } else if (++pendingWait >= delay.get() * 3) {
                // Servers may reject a placement; retry after a short timeout instead of stalling forever.
                pending = null;
                pendingWait = 0;
                if (++failedAttempts >= 5) {
                    warning("Five placement attempts were rejected; builder stopped.");
                    toggle();
                }
            }
            return;
        }
        if (placed >= length.get()) { toggle(); return; }
        if (++tickWait < delay.get()) return;
        tickWait = 0;

        Item item = itemFor(component.get());
        int slot = findHotbarItem(item);
        if (slot < 0) {
            warning("Missing " + component.get().name().toLowerCase() + " in hotbar; builder stopped.");
            toggle();
            return;
        }
        InvUtils.swap(slot, false);

        BlockPos target = origin.relative(buildDirection, startDistance.get() + placed);
        if (mc.player.distanceToSqr(Vec3.atCenterOf(target)) > 20.25) {
            if (!outOfReachWarned) warning("Next block is out of reach; move closer and the builder will continue.");
            outOfReachWarned = true;
            return;
        }
        outOfReachWarned = false;
        if (!mc.level.getBlockState(target).isAir()) {
            // Existing blocks count as completed positions; the bot never replaces them.
            placed++;
            failedAttempts = 0;
            return;
        }

        BlockHitResult hit = findSupport(target);
        if (hit == null) {
            if (stopIfUnsupported.get()) {
                warning("No sturdy support next to " + target.toShortString() + "; builder stopped safely.");
                toggle();
            }
            return;
        }

        mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, hit);
        mc.player.swing(InteractionHand.MAIN_HAND);
        pending = target;
    }

    private int findHotbarItem(Item item) {
        for (int i = 0; i < 9; i++) if (mc.player.getInventory().getItem(i).is(item)) return i;
        return -1;
    }

    private BlockHitResult findSupport(BlockPos target) {
        for (Direction face : Direction.values()) {
            BlockPos neighbor = target.relative(face);
            if (mc.level.getBlockState(neighbor).isFaceSturdy(mc.level, neighbor, face.getOpposite())) {
                Direction clickedFace = face.getOpposite();
                Vec3 hitPos = Vec3.atCenterOf(neighbor).add(clickedFace.getStepX() * 0.5, clickedFace.getStepY() * 0.5, clickedFace.getStepZ() * 0.5);
                return new BlockHitResult(hitPos, clickedFace, neighbor, false);
            }
        }
        return null;
    }

    private Item itemFor(Component value) {
        return switch (value) {
            case RedstoneBlock -> Items.REDSTONE_BLOCK;
            case Repeater -> Items.REPEATER;
            case Comparator -> Items.COMPARATOR;
            case RedstoneDust -> Items.REDSTONE;
            case RedstoneTorch -> Items.REDSTONE_TORCH;
        };
    }
}
