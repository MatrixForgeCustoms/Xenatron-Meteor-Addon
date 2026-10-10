package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class AutoClockTuner extends Module {
    private final Setting<Integer> tickDelay = settings.getDefaultGroup().add(new IntSetting.Builder()
        .name("repeater-delay").description("Desired repeater delay, in redstone ticks.")
        .defaultValue(2).min(1).max(4).sliderMin(1).sliderMax(4).build());
    private BlockPos target;
    private int waitTicks;
    private int attempts;

    public AutoClockTuner() {
        super(AddonTemplate.XENATRON_REDSTONE, "auto-clock-tuner", "Sets the repeater you are looking at to the selected delay.");
    }

    @Override public void onActivate() {
        waitTicks = 0;
        attempts = 0;
        if (mc.player == null || mc.level == null || !(mc.hitResult instanceof BlockHitResult hit)
            || !mc.level.getBlockState(hit.getBlockPos()).is(Blocks.REPEATER)) {
            warning("Look at a placed repeater before enabling Auto Clock Tuner.");
            toggle();
            return;
        }
        target = hit.getBlockPos().immutable();
    }

    @EventHandler private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.level == null || mc.gameMode == null || target == null) return;
        if (!mc.level.getBlockState(target).is(Blocks.REPEATER)) {
            warning("The targeted repeater is no longer there.");
            toggle();
            return;
        }
        int current = mc.level.getBlockState(target).getValue(RepeaterBlock.DELAY);
        if (current == tickDelay.get()) {
            info("Repeater set to " + current + " redstone ticks.");
            toggle();
            return;
        }
        if (waitTicks-- > 0) return;
        if (attempts++ >= 12) {
            warning("Repeater did not update; check reach and server permissions.");
            toggle();
            return;
        }
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(target), Direction.UP, target, false);
        mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, hit);
        mc.player.swing(InteractionHand.MAIN_HAND);
        waitTicks = 2;
    }

    @Override public void onDeactivate() { target = null; waitTicks = 0; attempts = 0; }
}
