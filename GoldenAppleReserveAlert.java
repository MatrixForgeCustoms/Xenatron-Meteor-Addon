package com.example.addon.modules;

import com.example.addon.AddonTemplate;

public class GoldenAppleReserveAlert extends ResourceAlert {
    public GoldenAppleReserveAlert() {
        super(AddonTemplate.XENATRON_SMP, "golden-apple-reserve", "Warns when your golden apple stock gets low.", Metric.GOLDEN_APPLE, 2);
    }
}
