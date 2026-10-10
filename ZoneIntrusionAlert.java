package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** Alerts locally when another player enters the selected cuboid area. */
public class ZoneIntrusionAlert extends Module {
    private static BlockPos corner1;
    private static BlockPos corner2;
    private static ResourceKey<Level> zoneDimension;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Boolean> playSound = sgGeneral.add(new BoolSetting.Builder()
        .name("alarm-sound")
        .description("Play a short sound when a player enters the selected zone.")
        .defaultValue(true)
        .build());

    private final Set<UUID> inside = new HashSet<>();

    public ZoneIntrusionAlert() {
        super(AddonTemplate.XENATRON_SECURITY, "zone-intrusion-alert", "Warns locally when another player enters your selected area.");
    }

    public static boolean setCorner(int corner, BlockPos pos, ResourceKey<Level> dimension) {
        if (zoneDimension != null && !zoneDimension.equals(dimension)
            && ((corner == 1 && corner2 != null) || (corner == 2 && corner1 != null))) return false;

        zoneDimension = dimension;
        if (corner == 1) corner1 = pos.immutable();
        else corner2 = pos.immutable();
        return true;
    }

    public static void clearZone() {
        corner1 = null;
        corner2 = null;
        zoneDimension = null;
    }

    public static boolean isConfigured() {
        return corner1 != null && corner2 != null && zoneDimension != null;
    }

    public static String zoneDescription() {
        if (corner1 == null && corner2 == null) return "No zone selected. Set .xenatron zone pos1 and pos2.";
        String first = corner1 == null ? "unset" : corner1.toShortString();
        String second = corner2 == null ? "unset" : corner2.toShortString();
        String dimension = zoneDimension == null ? "unknown dimension" : zoneDimension.toString();
        return "Zone corners: " + first + " and " + second + " (" + dimension + ").";
    }

    private static boolean contains(BlockPos pos, ResourceKey<Level> dimension) {
        if (!isConfigured() || !zoneDimension.equals(dimension)) return false;
        int minX = Math.min(corner1.getX(), corner2.getX());
        int minY = Math.min(corner1.getY(), corner2.getY());
        int minZ = Math.min(corner1.getZ(), corner2.getZ());
        int maxX = Math.max(corner1.getX(), corner2.getX());
        int maxY = Math.max(corner1.getY(), corner2.getY());
        int maxZ = Math.max(corner1.getZ(), corner2.getZ());
        return pos.getX() >= minX && pos.getX() <= maxX
            && pos.getY() >= minY && pos.getY() <= maxY
            && pos.getZ() >= minZ && pos.getZ() <= maxZ;
    }

    @Override public void onActivate() { inside.clear(); }
    @Override public void onDeactivate() { inside.clear(); }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.level == null || !isConfigured()) return;

        Set<UUID> nowInside = new HashSet<>();
        for (Player player : mc.level.players()) {
            if (player == mc.player || !contains(player.blockPosition(), mc.level.dimension())) continue;
            UUID id = player.getUUID();
            nowInside.add(id);
            if (inside.add(id)) {
                warning(player.getGameProfile().name() + " entered your zone at " + player.blockPosition().toShortString() + ".");
                if (playSound.get()) mc.player.playSound(SoundEvents.NOTE_BLOCK_PLING.value(), 1.0f, 1.2f);
            }
        }
        inside.retainAll(nowInside);
    }
}
