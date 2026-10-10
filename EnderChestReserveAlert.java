package com.example.addon.modules;

import com.example.addon.AddonTemplate;

public class EnderChestReserveAlert extends ResourceAlert {
    public EnderChestReserveAlert() {
        super(AddonTemplate.XENATRON_SMP, "ender-chest-reserve", "Warns when your ender chest stock gets low.", Metric.ENDER_CHEST, 1);
    }
}
