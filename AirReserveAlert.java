package com.example.addon.modules;

import com.example.addon.AddonTemplate;

public class AirReserveAlert extends ResourceAlert {
    public AirReserveAlert() {
        super(AddonTemplate.XENATRON_SMP, "air-reserve", "Warns when your air supply gets low.", Metric.AIR, 100);
    }
}
