package org.leralix.lib.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class CommandManager implements CommandExecutor, TabExecutor, TabCompleter {

    protected final HashMap<String, SubCommand> subCommands;
    protected final String permissionBase;

    protected CommandManager(String permissionBase){
        this.permissionBase = permissionBase;
        this.subCommands = new HashMap<>();
    }

    protected void addSubCommand(SubCommand subCommand){
        subCommands.put(subCommand.getName(), subCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0){
            SubCommand subCommand = subCommands.get(args[0]);
            if(subCommand != null && sender.hasPermission(getFullPermission(subCommand.getName()))){
                    subCommand.perform(sender, args);
                    return true;
                }
        }
        if(sender instanceof Player){
            new MainHelpCommand(this).perform(sender, args);
            return true;
        }
        return false;
    }

    private String getFullPermission(String subCommand){
        return permissionBase + "." + subCommand;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if(args.length == 1) {
            for(SubCommand subCmd : subCommands.values()) {
                if(sender.hasPermission(getFullPermission(subCmd.getName())) && subCmd.getName().startsWith(args[0].toLowerCase())) {
                    suggestions.add(subCmd.getName());
                }
            }
            return suggestions;
        }
        SubCommand subCommand = subCommands.get(args[0]);
        if(subCommand == null) return suggestions;

        List<String> subCommandSuggestions = subCommand.getTabCompleteSuggestions(sender, args[0].toLowerCase(), args);
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
