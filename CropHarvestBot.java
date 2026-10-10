package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CropHarvestBot extends Module {

    public CropHarvestBot() {
        super(AddonTemplate.XENATRON_SMP, "crop-harvest-bot", "Harvests the mature vanilla crop you are looking at, within five blocks.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.level == null || mc.gameMode == null) return;

        if (!(mc.hitResult instanceof BlockHitResult hit)) return;
        BlockPos cropPos = hit.getBlockPos();
        if (mc.player.distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(cropPos)) > 25) return;
        BlockState state = mc.level.getBlockState(cropPos);

        // If it's a valid crop block, check its age tag
        if (state.getBlock() instanceof CropBlock) {
            CropBlock crop = (CropBlock) state.getBlock();
            
            // If it's max age (ready for harvest), client-side break it
            if (crop.isMaxAge(state)) {
                if (mc.gameMode.destroyBlock(cropPos)) mc.player.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
            }
        }
    }
}
