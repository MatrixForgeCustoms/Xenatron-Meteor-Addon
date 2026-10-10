package com.example.addon.modules;

import com.example.addon.AddonTemplate;

public class ArrowReserveAlert extends ResourceAlert {
    public ArrowReserveAlert() {
        super(AddonTemplate.XENATRON_SMP, "arrow-reserve", "Warns when your arrow stock gets low.", Metric.ARROW, 16);
    }
}
