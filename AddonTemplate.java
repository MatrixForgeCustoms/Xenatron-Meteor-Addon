package com.example.addon;

// Security Modules (5 Modules)
import com.example.addon.modules.AdminRadar;
import com.example.addon.modules.StaffBoundary;
import com.example.addon.modules.InventoryPanic;
import com.example.addon.modules.ChatSilentMode;
import com.example.addon.modules.GhostDisconnect;
import com.example.addon.modules.ZoneIntrusionAlert;

// Redstone Modules (5 Modules)
import com.example.addon.modules.RedstoneBuilder;
import com.example.addon.modules.RedstoneVisualizer;
import com.example.addon.modules.AutoClockTuner;
import com.example.addon.modules.RedstoneSneakPulse;
import com.example.addon.modules.PressurePlateBypass;

// SMP Modules (Exactly your 3 favorite customized modules!)
import com.example.addon.modules.ChestSpyCoords;
import com.example.addon.modules.BedRadiusWarning;
import com.example.addon.modules.ArmorDurabilityPanic; // Das neue Modul importieren
import com.example.addon.modules.SafeEnderChest;
import com.example.addon.modules.CropHarvestBot;
import com.example.addon.modules.AutoPearlSaver;
import com.example.addon.modules.LowHealthAlert;
import com.example.addon.modules.HungerAlert;
import com.example.addon.modules.FallDistanceAlert;
import com.example.addon.modules.FireSafetyAlert;
import com.example.addon.modules.ArmorDurabilityAlert;
import com.example.addon.modules.InventoryFullAlert;
import com.example.addon.modules.TotemStockAlert;
import com.example.addon.modules.LowRedstoneStockAlert;
import com.example.addon.modules.LowRepeaterStockAlert;
import com.example.addon.modules.PlayerActivityAlert;
import com.example.addon.modules.AirReserveAlert;
import com.example.addon.modules.SaturationReserveAlert;
import com.example.addon.modules.ExperienceReserveAlert;
import com.example.addon.modules.LowAltitudeAlert;
import com.example.addon.modules.GoldenAppleReserveAlert;
import com.example.addon.modules.ObsidianReserveAlert;
import com.example.addon.modules.WaterBucketReserveAlert;
import com.example.addon.modules.LavaBucketReserveAlert;
import com.example.addon.modules.TorchReserveAlert;
import com.example.addon.modules.ArrowReserveAlert;
import com.example.addon.modules.BuildingBlockReserveAlert;
import com.example.addon.modules.EnderChestReserveAlert;
import com.example.addon.modules.ShieldDurabilityAlert;
import com.example.addon.modules.PickaxeDurabilityAlert;
import com.example.addon.modules.BoneReserveAlert;
import com.example.addon.modules.StringReserveAlert;
import com.example.addon.modules.GunpowderReserveAlert;
import com.example.addon.modules.SlimeballReserveAlert;
import com.example.addon.modules.PistonReserveAlert;
import com.example.addon.modules.HelmetDurabilityAlert;
import com.example.addon.hud.HudExample;
import com.example.addon.commands.XenatronCommand;
import meteordevelopment.meteorclient.commands.Commands;

import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class AddonTemplate extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    
    public static final Category XENATRON_REDSTONE = new Category("Xenatron: Redstone");
    public static final Category XENATRON_SECURITY = new Category("Xenatron: Security");
    public static final Category XENATRON_SMP = new Category("Xenatron: Smp");
    public static final Category XENATRON_FUN = new Category("Xenatron: Fun");

    public static final HudGroup HUD_GROUP = new HudGroup("Xenatron: HUD");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Xenatron...");

        // Security modules
        Modules.get().add(new AdminRadar());
        Modules.get().add(new StaffBoundary());
        Modules.get().add(new InventoryPanic());
        Modules.get().add(new ChatSilentMode());
        Modules.get().add(new GhostDisconnect());
        Modules.get().add(new ZoneIntrusionAlert());

        // Redstone modules
        Modules.get().add(new RedstoneBuilder());
        Modules.get().add(new RedstoneVisualizer());
        Modules.get().add(new AutoClockTuner());
        Modules.get().add(new RedstoneSneakPulse());
        Modules.get().add(new PressurePlateBypass());

        // SMP modules
        Modules.get().add(new ChestSpyCoords());
        Modules.get().add(new BedRadiusWarning());
        Modules.get().add(new SafeEnderChest());
        Modules.get().add(new CropHarvestBot());
        Modules.get().add(new ArmorDurabilityPanic());
        Modules.get().add(new AutoPearlSaver());

        // New passive safety and stock alerts
        Modules.get().add(new LowHealthAlert());
        Modules.get().add(new HungerAlert());
        Modules.get().add(new FallDistanceAlert());
        Modules.get().add(new FireSafetyAlert());
        Modules.get().add(new ArmorDurabilityAlert());
        Modules.get().add(new InventoryFullAlert());
        Modules.get().add(new TotemStockAlert());
        Modules.get().add(new LowRedstoneStockAlert());
        Modules.get().add(new LowRepeaterStockAlert());
        Modules.get().add(new PlayerActivityAlert());

        // Additional configurable resource and durability alerts.
        Modules.get().add(new AirReserveAlert());
        Modules.get().add(new SaturationReserveAlert());
        Modules.get().add(new ExperienceReserveAlert());
        Modules.get().add(new LowAltitudeAlert());
        Modules.get().add(new GoldenAppleReserveAlert());
        Modules.get().add(new ObsidianReserveAlert());
        Modules.get().add(new WaterBucketReserveAlert());
        Modules.get().add(new LavaBucketReserveAlert());
        Modules.get().add(new TorchReserveAlert());
        Modules.get().add(new ArrowReserveAlert());
        Modules.get().add(new BuildingBlockReserveAlert());
        Modules.get().add(new EnderChestReserveAlert());
        Modules.get().add(new ShieldDurabilityAlert());
        Modules.get().add(new PickaxeDurabilityAlert());
        Modules.get().add(new BoneReserveAlert());
        Modules.get().add(new StringReserveAlert());
        Modules.get().add(new GunpowderReserveAlert());
        Modules.get().add(new SlimeballReserveAlert());
        Modules.get().add(new PistonReserveAlert());
        Modules.get().add(new HelmetDurabilityAlert());

        // Fun utilities and their display settings.
        Modules.get().add(new com.example.addon.modules.FunSettings());
        
        Commands.add(new XenatronCommand());
        Commands.add(new com.example.addon.commands.XenatronFunCommand());

        // HUD
        Hud.get().register(HudExample.INFO);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(XENATRON_REDSTONE);
        Modules.registerCategory(XENATRON_SECURITY);
        Modules.registerCategory(XENATRON_SMP);
        Modules.registerCategory(XENATRON_FUN);
    }

    @Override
    public String getPackage() {
        return "com.example.addon";
    }

}



