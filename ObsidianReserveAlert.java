package com.example.addon.modules;

import com.example.addon.AddonTemplate;

public class ObsidianReserveAlert extends ResourceAlert {
    public ObsidianReserveAlert() {
        super(AddonTemplate.XENATRON_SMP, "obsidian-reserve", "Warns when your obsidian stock gets low.", Metric.OBSIDIAN, 8);
    }
}
