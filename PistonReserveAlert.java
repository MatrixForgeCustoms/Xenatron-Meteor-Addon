package com.example.addon.modules;
import com.example.addon.AddonTemplate;
public class PistonReserveAlert extends ResourceAlert {
    public PistonReserveAlert() { super(AddonTemplate.XENATRON_REDSTONE, "piston-reserve", "Warns when your piston stock gets low.", Metric.PISTON, 4); }
}
