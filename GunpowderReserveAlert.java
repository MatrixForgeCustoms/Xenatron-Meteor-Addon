package com.example.addon.modules;
import com.example.addon.AddonTemplate;
public class GunpowderReserveAlert extends ResourceAlert {
    public GunpowderReserveAlert() { super(AddonTemplate.XENATRON_SMP, "gunpowder-reserve", "Warns when your gunpowder stock gets low.", Metric.GUNPOWDER, 8); }
}
