package com.example.addon.mixin;

import com.example.addon.util.ClientScreenState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Tracks whether a Minecraft screen is open so automated placement can pause around menus. */
@Mixin(Minecraft.class)
public abstract class ScreenChangeMixin {
    @Inject(method = "setScreen", at = @At("TAIL"))
    private void xenatron$onScreenChanged(Screen screen, CallbackInfo ci) {
        ClientScreenState.update(screen != null);
    }
}
