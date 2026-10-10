package com.example.addon.commands;

import com.example.addon.modules.FunSettings;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.concurrent.ThreadLocalRandom;

/** Fifty local-only novelty subcommands; no text is sent to the multiplayer server. */
public class XenatronFunCommand extends Command {
    private static final String[][] ACTIONS = {
        {"joke", "Why did the creeper cross the road? It wanted to make a blast entrance."},
        {"coin", "A coin flips through the air and lands "},
        {"dice", "The dice roll: "},
        {"eightball", "The all-knowing cube says: "},
        {"fortune", "Your Minecraft fortune: "},
        {"compliment", "You make even a dirt hut look legendary."},
        {"mood", "Current biome-independent mood: cozy."},
        {"dance", "Your character performs a tiny victory dance."},
        {"shrug", "¯\\_(ツ)_/¯"},
        {"wave", "A friendly wave from Xenatron!"},
        {"laugh", "Heh. The villagers are pretending they didn't hear that."},
        {"cat", "A pixel cat has decided you are acceptable."},
        {"dog", "A loyal blocky dog wags its tail."},
        {"wolf", "The wolf gives you a respectful nod."},
        {"fox", "The fox steals exactly one imaginary emerald."},
        {"bee", "Bee calm. You are doing great."},
        {"creeper", "Sss... just kidding. No explosions today."},
        {"skeleton", "The skeleton misses its shot. Classic."},
        {"zombie", "The zombie says: uuuuuh... nice build."},
        {"villager", "The villager offers you one premium hmm."},
        {"enderman", "The Enderman returned your block. Probably."},
        {"warden", "The Warden heard that joke and chose silence."},
        {"ghast", "A distant ghast sings the world's saddest note."},
        {"slime", "Boing! A tiny slime bounces past."},
        {"chicken", "The chicken has no plan, but excellent confidence."},
        {"pig", "A pig approves of your life choices."},
        {"goat", "The goat headbutts your worries away."},
        {"axolotl", "An axolotl wishes you a splashy day."},
        {"mining", "Achievement unlocked: looked at a block while holding a pickaxe."},
        {"diamond", "The real diamonds were the friends we mined along the way."},
        {"dirt", "Dirt: still the most dependable block in the game."},
        {"grass", "Touch grass. In Minecraft, that counts."},
        {"redstone", "Redstone: dust with a very important job."},
        {"lava", "Lava is just spicy water. Do not test this."},
        {"water", "Hydration check: your Minecraft character is probably fine."},
        {"nether", "Nether travel: 8 times the distance, 80 times the drama."},
        {"end", "The End is a beginning with more floating islands."},
        {"bed", "Your bed is obstructed. Emotionally, you are still valid."},
        {"sleep", "You can only sleep at night. You can daydream anytime."},
        {"crafting", "Recipe unlocked: one good mood + a crafting table."},
        {"pickaxe", "Your pickaxe believes in you. Keep swinging (at stone)."},
        {"shovel", "A shovel is just a dirt-removal specialist."},
        {"axe", "The axe would like to branch out into new hobbies."},
        {"sword", "The sword says: please use me only on hostile mobs."},
        {"totem", "The totem offers a tiny imaginary high-five."},
        {"pearl", "Ender pearl delivery estimate: one dramatic teleport."},
        {"bucket", "A bucket can hold water, lava, fish, and your ambitions."},
        {"boat", "The boat is ready for a calm 200-block trip on land."},
        {"horse", "Your imaginary horse has excellent pathfinding."},
        {"fishing", "You caught... a moment of peace."}
    };

    public XenatronFunCommand() { super("xenatronfun", "Fifty local-only fun replies. Try .xenatronfun help."); }

    @Override
    public void build(LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.executes(context -> {
            info("Try .xenatronfun help to see all 50 fun actions.");
            return SINGLE_SUCCESS;
        });
        builder.then(literal("help").executes(context -> {
            for (int start = 0; start < ACTIONS.length; start += 10) {
                StringBuilder list = new StringBuilder("Fun actions: ");
                for (int i = start; i < Math.min(start + 10, ACTIONS.length); i++) list.append(ACTIONS[i][0]).append("  ");
                info(FunSettings.format(list.toString().trim()));
            }
            return SINGLE_SUCCESS;
        }));
        for (String[] action : ACTIONS) {
            builder.then(literal(action[0]).executes(context -> {
                info(FunSettings.format(response(action[0], action[1])));
                return SINGLE_SUCCESS;
            }));
        }
    }

    private String response(String name, String base) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return switch (name) {
            case "coin" -> base + (random.nextBoolean() ? "heads." : "tails.");
            case "dice" -> base + random.nextInt(1, 7) + ".";
            case "eightball" -> base + new String[]{"Absolutely.", "Ask again after this mining trip.", "Signs point to yes.", "Not today.", "The server tick decides."}[random.nextInt(5)];
            case "fortune" -> base + new String[]{"A surprise chicken will cross your path.", "You will find a useful block soon.", "Remember to bring extra torches.", "Your next build will be oddly square."}[random.nextInt(4)];
            default -> base;
        };
    }
}
