package com.example.addon.modules;

import com.example.addon.AddonTemplate;

public class WaterBucketReserveAlert extends ResourceAlert {
    public WaterBucketReserveAlert() {
        super(AddonTemplate.XENATRON_SMP, "water-bucket-reserve", "Warns when your water bucket stock gets low.", Metric.WATER_BUCKET, 1);
    }
}
