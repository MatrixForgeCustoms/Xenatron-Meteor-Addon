package com.example.addon.modules;

import com.example.addon.AddonTemplate;

public class LavaBucketReserveAlert extends ResourceAlert {
    public LavaBucketReserveAlert() {
        super(AddonTemplate.XENATRON_SMP, "lava-bucket-reserve", "Warns when your lava bucket stock gets low.", Metric.LAVA_BUCKET, 1);
    }
}
