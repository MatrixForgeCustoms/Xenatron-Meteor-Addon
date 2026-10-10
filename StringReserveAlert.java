package com.example.addon.modules;
import com.example.addon.AddonTemplate;
public class StringReserveAlert extends ResourceAlert {
    public StringReserveAlert() { super(AddonTemplate.XENATRON_SMP, "string-reserve", "Warns when your string stock gets low.", Metric.STRING, 8); }
}
