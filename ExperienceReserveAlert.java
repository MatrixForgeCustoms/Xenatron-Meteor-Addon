package com.example.addon.modules;

import com.example.addon.AddonTemplate;

public class ExperienceReserveAlert extends ResourceAlert {
    public ExperienceReserveAlert() {
        super(AddonTemplate.XENATRON_SMP, "experience-reserve", "Warns when your experience levels get low.", Metric.EXPERIENCE, 5);
    }
}
