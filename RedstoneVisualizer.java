package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class RedstoneVisualizer extends Module {
    private final SettingGroup sg = settings.getDefaultGroup();
    private final Setting<Integer> radius = sg.add(new IntSetting.Builder().name("radius")
        .description("Horizontal and vertical scan radius.").defaultValue(8).min(2).max(16).build());
    private final Setting<Integer> scanInterval = sg.add(new IntSetting.Builder().name("scan-interval")
        .description("Ticks between world scans.").defaultValue(10).min(1).max(40).build());
    private final Setting<Integer> maxBlocks = sg.add(new IntSetting.Builder().name("max-blocks")
        .description("Maximum number of highlights cached per scan.").defaultValue(128).min(16).max(1024).build());
    private final Setting<SettingColor> activeColor = settings.createGroup("Render").add(new ColorSetting.Builder()
        .name("active-color").description("Color used for redstone wire and redstone blocks.")
        .defaultValue(new SettingColor(255, 0, 0, 100)).build());
    private final List<BlockPos> found = new ArrayList<>();
    private int ticks;

    public RedstoneVisualizer() {
        super(AddonTemplate.XENATRON_REDSTONE, "redstone-visualizer", "Highlights nearby redstone wire and redstone blocks through walls.");
    }

    @EventHandler private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.level == null || ++ticks < scanInterval.get()) return;
        ticks = 0;
        found.clear();
        BlockPos center = mc.player.blockPosition();
        int r = radius.get();
        scan: for (int x = -r; x <= r; x++) for (int y = -r; y <= r; y++) for (int z = -r; z <= r; z++) {
            BlockPos pos = center.offset(x, y, z);
            var state = mc.level.getBlockState(pos);
            if (state.is(Blocks.REDSTONE_BLOCK) || state.is(Blocks.REDSTONE_WIRE)) {
                found.add(pos.immutable());
                if (found.size() >= maxBlocks.get()) break scan;
            }
        }
    }

    @EventHandler private void onRender(Render3DEvent event) {
        SettingColor color = activeColor.get();
        for (BlockPos pos : found) event.renderer.box(new AABB(pos), color, color, ShapeMode.Both, 0);
    }

    @Override public void onActivate() { found.clear(); ticks = 0; }
    @Override public void onDeactivate() { found.clear(); }
}
