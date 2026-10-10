package com.example.addon.modules;

import com.example.addon.AddonTemplate;

public class BuildingBlockReserveAlert extends ResourceAlert {
    public BuildingBlockReserveAlert() {
        super(AddonTemplate.XENATRON_SMP, "building-block-reserve", "Warns when your placeable block stock gets low.", Metric.BUILDING_BLOCK, 32);
    }
}
