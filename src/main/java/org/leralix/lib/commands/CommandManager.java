package org.leralix.lib.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public abstract class CommandManager implements CommandExecutor, TabExecutor, TabCompleter {

    /**
     * List of all subcommands for this command manager.
     * The key is the name of the subcommand, and the value is the subcommand itself.
     */
    protected final HashMap<String, SubCommand> subCommands;
    /**
     * The base permission for this command manager.
     */
    protected final String permissionBase;
    /**
     * The default command to execute when no subcommand is provided and when the sender is a player.
     */
    protected final PlayerSubCommand defaultCommand;

    protected CommandManager(String permissionBase) {
        this.permissionBase = permissionBase;
        this.subCommands = new HashMap<>();
        this.defaultCommand = new MainHelpCommand(this);
    }

    protected CommandManager(String permissionBase, PlayerSubCommand defaultCommand) {
        this.permissionBase = permissionBase;
        this.subCommands = new HashMap<>();
        this.defaultCommand = defaultCommand;
    }

    protected void addSubCommand(SubCommand subCommand) {
        subCommands.put(subCommand.getName(), subCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            SubCommand subCommand = subCommands.get(args[0]);
            if (subCommand != null && sender.hasPermission(getFullPermission(subCommand.getName()))) {
                subCommand.perform(sender, args);
                return true;
            }
        }
        if (sender instanceof Player) {
            defaultCommand.perform(sender, args);
            return true;
        }
        return false;
    }

    private String getFullPermission(String subCommand) {
        return permissionBase + "." + subCommand;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            for (SubCommand subCmd : subCommands.values()) {
                if (sender.hasPermission(getFullPermission(subCmd.getName())) && subCmd.getName().startsWith(args[0].toLowerCase())) {
                    suggestions.add(subCmd.getName());
                }
            }
            return suggestions;
        }
        SubCommand subCommand = subCommands.get(args[0]);
        if (subCommand == null) return suggestions;

        List<String> subCommandSuggestions = subCommand.getTabCompleteSuggestions(sender, args[0].toLowerCase(), args);
        if (subCommandSuggestions == null)
            return suggestions;

        for (String suggestion : subCommandSuggestions) {
            if (suggestion.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                suggestions.add(suggestion);
            }
        }
        return suggestions;
    }

    public Collection<SubCommand> getSubCommands() {
        return subCommands.values();
    }

    public abstract String getName();
}
