package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class RedstoneSneakPulse extends Module {
    private boolean wasSneaking = false;

    public RedstoneSneakPulse() {
        super(AddonTemplate.XENATRON_REDSTONE, "redstone-sneak-pulse", "Right-clicks a button or lever below you once when you start sneaking.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.level == null || mc.gameMode == null) return;

        // Detect if player just started sneaking in this frame
        if (mc.player.isCrouching() && !wasSneaking) {
            BlockPos blockUnderneath = mc.player.blockPosition().below();
            var block = mc.level.getBlockState(blockUnderneath).getBlock();
            if (!(block instanceof ButtonBlock) && !(block instanceof LeverBlock)) {
                wasSneaking = true;
                return;
            }

            BlockHitResult hitResult = new BlockHitResult(
                new Vec3(blockUnderneath.getX() + 0.5, blockUnderneath.getY() + 1.0, blockUnderneath.getZ() + 0.5),
                Direction.UP, blockUnderneath, false
            );
            
            // Send interaction packet to trigger buttons, levers or pressure plates instantly
            mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, hitResult);
            mc.player.swing(InteractionHand.MAIN_HAND);
            info("Activated the button or lever below you.");
        }

        wasSneaking = mc.player.isCrouching();
    }
}
