package org.leralix.lib.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class CommandManager implements CommandExecutor, TabExecutor, TabCompleter {

    protected final HashMap<String, SubCommand> subCommands;

    protected CommandManager() {
        subCommands = new HashMap<>();
    }

    protected void addSubCommand(SubCommand subCommand){
        subCommands.put(subCommand.getName(), subCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p && args.length > 0){
            SubCommand subCommand = subCommands.get(args[0]);
            if(subCommand != null) {
                subCommand.perform(p, args);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command command,String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if(!(sender instanceof Player)){
            return Collections.emptyList();
        }

        if(args.length == 1) {
            for(SubCommand subCmd : subCommands.values()) {
                if(subCmd.getName().startsWith(args[0].toLowerCase())) {
                    suggestions.add(subCmd.getName());
                }
            }
            return suggestions;
        }
        SubCommand subCommand = subCommands.get(args[0]);
        if(subCommand == null) return suggestions;

        List<String> subCommandSuggestions = subCommand.getTabCompleteSuggestions((Player) sender, args[0].toLowerCase(), args);
        if (subCommandSuggestions == null)
            return suggestions;

        for (String suggestion : subCommandSuggestions) {
            if(suggestion.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                suggestions.add(suggestion);
            }
        }
        return suggestions;
    }

    public Collection<SubCommand> getSubCommands(){
        return subCommands.values();
    }
    public abstract String getName();
}
