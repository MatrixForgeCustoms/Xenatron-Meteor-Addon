package com.example.addon.modules;

import com.example.addon.AddonTemplate;

public class TorchReserveAlert extends ResourceAlert {
    public TorchReserveAlert() {
        super(AddonTemplate.XENATRON_SMP, "torch-reserve", "Warns when your torch stock gets low.", Metric.TORCH, 16);
    }
}
