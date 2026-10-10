package com.example.addon.util;

/** Client-side screen state shared by the mixin and modules. */
public final class ClientScreenState {
    private static volatile boolean open;

    private ClientScreenState() {}

    public static void update(boolean isOpen) { open = isOpen; }
    public static boolean isOpen() { return open; }
}
