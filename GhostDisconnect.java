package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.chat.Component;

public class GhostDisconnect extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    
    private int tickCounter = 0;
    private boolean isTriggered = false;

    // Custom configuration slider for the delay in seconds
    @SuppressWarnings("all")
    private final Setting<Integer> delaySeconds = sgGeneral.add(new IntSetting.Builder()
        .name("delay-seconds")
        .description("How many seconds to wait before simulating the timeout.")
        .defaultValue(3)
        .min(0)
        .max(10)
        .sliderMin(0)
        .sliderMax(10)
        .build()
    );

    public GhostDisconnect() {
        super(AddonTemplate.XENATRON_SECURITY, "ghost-disconnect", "Simulates a natural network timeout after a realistic time delay.");
    }

    @Override
    public void onActivate() {
        if (mc.getConnection() == null) {
            this.toggle();
            return;
        }
        
        // Start the countdown process
        isTriggered = true;
        tickCounter = 0;
        info("§e[Xenatron] Timeout triggered! Simulating lag spikes...");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (!isTriggered || mc.getConnection() == null) return;

        tickCounter++;
        
        // 20 Minecraft ticks equal exactly 1 second
        int targetTicks = delaySeconds.get() * 20;

        if (tickCounter >= targetTicks) {
            // Natural vanilla disconnect with timeout message once the timer hits zero
            mc.getConnection().getConnection().disconnect(Component.literal("Timed out"));
            
            // Clean up and disable the module loop
            isTriggered = false;
            this.toggle();
        }
    }

    @Override
    public void onDeactivate() {
        // Reset flags if the user turns off the module manually before timeout
        isTriggered = false;
        tickCounter = 0;
    }
}
