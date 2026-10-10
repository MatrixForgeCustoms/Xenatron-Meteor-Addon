package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;

/** Display options for local-only Xenatron fun commands. */
public class FunSettings extends Module {
    public enum Style { Xenatron, Wholesome, Chaotic }

    private static FunSettings current;
    private final SettingGroup sg = settings.getDefaultGroup();
    private final Setting<Style> style = sg.add(new EnumSetting.Builder<Style>()
        .name("style").description("Choose how fun-command replies are formatted.").defaultValue(Style.Xenatron).build());
    private final Setting<Boolean> emojis = sg.add(new BoolSetting.Builder()
        .name("emojis").description("Add a small emoji to command replies.").defaultValue(true).build());

    public FunSettings() {
        super(AddonTemplate.XENATRON_FUN, "fun-settings", "Controls the style of Xenatron's local fun-command replies.");
        current = this;
    }

    public static String format(String message) {
        if (current == null) return message;
        String result = switch (current.style.get()) {
            case Xenatron -> "[Xenatron Fun] " + message;
            case Wholesome -> "♡ " + message;
            case Chaotic -> "✨ " + message + " ✨";
        };
        return current.emojis.get() ? result + " 🎮" : result;
    }
}
