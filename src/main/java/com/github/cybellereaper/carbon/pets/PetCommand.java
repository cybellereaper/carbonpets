package com.github.cybellereaper.carbon.pets;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class PetCommand implements CommandExecutor, TabCompleter {

    private final PetService petService;

    public PetCommand(PetService petService) {
        this.petService = petService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use pet commands.");
            return true;
        }

        if (args.length == 0) {
            sendUsage(player);
            return true;
        }

        String subCommand = args[0].toLowerCase(Locale.ROOT);

        if (subCommand.equals("list")) {
            player.sendMessage("Available pets: " + String.join(", ", petService.knownPetIds()));
            return true;
        }

        if (subCommand.equals("dismiss")) {
            petService.despawnPet(player.getUniqueId());
            player.sendMessage("Pet dismissed.");
            return true;
        }

        if (!subCommand.equals("summon")) {
            sendUsage(player);
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("Usage: /pet summon <id>");
            return true;
        }

        PetDefinition definition = petService.findDefinition(args[1]).orElse(null);
        if (definition == null) {
            player.sendMessage("Unknown pet. Use /pet list.");
            return true;
        }

        petService.summon(player, definition);
        player.sendMessage("Summoned " + definition.displayName() + ".");
        return true;
    }

    private void sendUsage(Player player) {
        player.sendMessage("/pet list");
        player.sendMessage("/pet summon <id>");
        player.sendMessage("/pet dismiss");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return filter(List.of("list", "summon", "dismiss"), args[0]);
        }

        if (args.length == 2 && "summon".equalsIgnoreCase(args[0])) {
            return filter(petService.knownPetIds().stream().toList(), args[1]);
        }

        return List.of();
    }

    private List<String> filter(List<String> values, String input) {
        String loweredInput = input.toLowerCase(Locale.ROOT);
        return values.stream()
                .filter(value -> value.toLowerCase(Locale.ROOT).startsWith(loweredInput))
                .sorted()
                .collect(Collectors.toList());
    }
}
