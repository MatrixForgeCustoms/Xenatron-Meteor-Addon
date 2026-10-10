package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PressurePlateBypass extends Module {
    private boolean wasOnPlate;

    public PressurePlateBypass() {
        super(AddonTemplate.XENATRON_REDSTONE, "pressure-plate-bypass", "Warns when you step on a pressure plate. It never alters your movement.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.level == null) return;

        BlockPos posUnderneath = mc.player.blockPosition().below();
        BlockState blockState = mc.level.getBlockState(posUnderneath);
        boolean onPlate = blockState.getBlock() instanceof BasePressurePlateBlock;
        if (onPlate && !wasOnPlate) warning("You stepped on a pressure plate.");
        wasOnPlate = onPlate;
    }

    @Override public void onActivate() { wasOnPlate = false; }
}
