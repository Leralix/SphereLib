package org.leralix.lib.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public abstract class PlayerSubCommand extends SubCommand{

    public abstract String getName();

    public abstract String getDescription();
    public abstract int getArguments();

    public abstract String getSyntax();
    public List<String> getTabCompleteSuggestions(CommandSender commandSender, String currentMessage, String[] args){
        if(commandSender instanceof Player player){
            return getTabCompleteSuggestions(player, currentMessage, args);
        }
        commandSender.sendMessage("This command can only be executed by a player");
        return Collections.emptyList();
    }
    public abstract List<String> getTabCompleteSuggestions(Player player, String currentMessage, String[] args);

    public void perform(CommandSender commandSender, String[] args) {
        if(commandSender instanceof Player player){
            perform(player, args);
            return;
        }
        commandSender.sendMessage("This command can only be executed by a player");
    }

    public abstract void perform(Player player, String[] args);


}
