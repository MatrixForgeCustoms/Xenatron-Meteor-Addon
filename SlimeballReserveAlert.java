package com.example.addon.modules;
import com.example.addon.AddonTemplate;
public class SlimeballReserveAlert extends ResourceAlert {
    public SlimeballReserveAlert() { super(AddonTemplate.XENATRON_REDSTONE, "slimeball-reserve", "Warns when your slimeball stock gets low.", Metric.SLIME_BALL, 4); }
}
