package com.example.addon.modules;

import com.example.addon.AddonTemplate;

public class PickaxeDurabilityAlert extends ResourceAlert {
    public PickaxeDurabilityAlert() {
        super(AddonTemplate.XENATRON_SMP, "pickaxe-durability", "Warns when the best pickaxe durability gets low.", Metric.PICKAXE_DURABILITY, 100);
    }
}
