package com.example.addon.modules;

import com.example.addon.AddonTemplate;

public class SaturationReserveAlert extends ResourceAlert {
    public SaturationReserveAlert() {
        super(AddonTemplate.XENATRON_SMP, "saturation-reserve", "Warns when your saturation gets low.", Metric.SATURATION, 3);
    }
}
