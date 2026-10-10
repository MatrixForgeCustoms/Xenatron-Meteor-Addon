# Xenatron

A Meteor Client addon for Minecraft 1.21.11 with security watchlist alerts, redstone helpers, SMP utilities, and status warnings.

## Build and run

Open the project in an IDE with Java 21 support, then run the Gradle build task. The addon JAR is written to build/libs/. Place it in the Minecraft mods folder alongside Meteor Client.

## In-game command

Use .xenatron for help, or .xenatron coords, .xenatron nether, .xenatron health, and .xenatron armor for status information.

## Categories

- **Security:** watchlist proximity warnings, spectator notices, and chat phrase alerts.
- **Redstone:** redstone construction and inventory alerts.
- **SMP:** survival helpers and health, hunger, fall, fire, armor, inventory, and totem warnings.

The staff watchlist uses names you enter in the Staff Boundary module settings. It only detects matching player entities in your currently loaded area; it cannot verify a player's role on the server.
