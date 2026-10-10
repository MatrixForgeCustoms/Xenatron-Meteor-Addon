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
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class BedRadiusWarning extends Module {
    private final SettingGroup sg = settings.getDefaultGroup();
    private final Setting<Integer> radius = sg.add(new IntSetting.Builder().name("radius")
        .description("Horizontal scan radius.").defaultValue(16).min(4).max(30).build());
    private final Setting<Integer> verticalRange = sg.add(new IntSetting.Builder().name("vertical-range")
        .description("How many blocks above and below you to scan.").defaultValue(6).min(1).max(16).build());
    private final Setting<Integer> scanInterval = sg.add(new IntSetting.Builder().name("scan-interval")
        .description("Ticks between bed scans.").defaultValue(10).min(1).max(40).build());
    private final Setting<Integer> maxBeds = sg.add(new IntSetting.Builder().name("max-beds")
        .description("Maximum number of bed highlights cached per scan.").defaultValue(128).min(16).max(1024).build());
    private final Setting<SettingColor> bedColor = settings.createGroup("Render").add(new ColorSetting.Builder()
        .name("bed-color").description("Color used to highlight nearby beds.")
        .defaultValue(new SettingColor(0, 255, 255, 100)).build());
    private final List<BlockPos> beds = new ArrayList<>();
    private int ticks;

    public BedRadiusWarning() {
        super(AddonTemplate.XENATRON_SMP, "bed-radius-warning", "Highlights nearby beds through walls within the configured scan radius.");
    }

    @EventHandler private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.level == null || ++ticks < scanInterval.get()) return;
        ticks = 0;
        beds.clear();
        BlockPos center = mc.player.blockPosition();
        int r = radius.get(), h = verticalRange.get();
        scan: for (int x = -r; x <= r; x++) for (int y = -h; y <= h; y++) for (int z = -r; z <= r; z++) {
            BlockPos pos = center.offset(x, y, z);
            if (mc.level.getBlockState(pos).is(BlockTags.BEDS)) {
                beds.add(pos.immutable());
                if (beds.size() >= maxBeds.get()) break scan;
            }
        }
    }

    @EventHandler private void onRender(Render3DEvent event) {
        SettingColor color = bedColor.get();
        for (BlockPos pos : beds) event.renderer.box(new AABB(pos), color, color, ShapeMode.Both, 0);
    }

    @Override public void onActivate() { beds.clear(); ticks = 0; }
    @Override public void onDeactivate() { beds.clear(); }
}
