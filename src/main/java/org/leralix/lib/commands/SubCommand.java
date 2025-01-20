package org.leralix.lib.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand {

    public abstract String getName();

    public abstract String getDescription();
    public abstract int getArguments();

    public abstract String getSyntax();
    public abstract List<String> getTabCompleteSuggestions(CommandSender player, String currentMessage, String[] args);

    public abstract void perform(CommandSender commandSender, String[] args);

    public static List<String> payPlayerSuggestion(String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 2) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                suggestions.add(p.getName());
            }
        }
        if (args.length == 3) {
            suggestions.add("<amount>");
        }
        return suggestions;
    }
}
