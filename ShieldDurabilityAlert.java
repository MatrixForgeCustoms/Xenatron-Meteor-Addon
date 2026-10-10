package com.example.addon.modules;

import com.example.addon.AddonTemplate;

public class ShieldDurabilityAlert extends ResourceAlert {
    public ShieldDurabilityAlert() {
        super(AddonTemplate.XENATRON_SMP, "shield-durability", "Warns when offhand durability gets low.", Metric.SHIELD_DURABILITY, 80);
    }
}
