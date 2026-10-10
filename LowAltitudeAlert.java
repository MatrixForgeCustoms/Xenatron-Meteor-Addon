package com.example.addon.modules;

import com.example.addon.AddonTemplate;

public class LowAltitudeAlert extends ResourceAlert {
    public LowAltitudeAlert() {
        super(AddonTemplate.XENATRON_SMP, "low-altitude-alert", "Warns when your Y level reaches the configured low height.", Metric.LOW_HEIGHT, 5);
    }
}
