package com.example.addon.modules;
import com.example.addon.AddonTemplate;
public class BoneReserveAlert extends ResourceAlert {
    public BoneReserveAlert() { super(AddonTemplate.XENATRON_SMP, "bone-reserve", "Warns when your bone stock gets low.", Metric.BONE, 8); }
}
