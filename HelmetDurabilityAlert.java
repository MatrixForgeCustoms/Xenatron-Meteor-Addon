package com.example.addon.modules;
import com.example.addon.AddonTemplate;
public class HelmetDurabilityAlert extends ResourceAlert {
    public HelmetDurabilityAlert() { super(AddonTemplate.XENATRON_SMP, "helmet-durability", "Warns when helmet durability gets low.", Metric.HELMET_DURABILITY, 80); }
}
