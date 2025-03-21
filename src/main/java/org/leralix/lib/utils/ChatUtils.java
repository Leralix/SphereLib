package org.leralix.lib.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

/**
 * This class is used for chat related utilities.
 */
public class ChatUtils {

    private ChatUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * This method is used to send a clickable message to a player.
     * @param player    The player to send the message to.
     * @param message   The message to send.
     * @param command   The command to run when the message is clicked without the "/" (exemple : tp 0 0 0)
     */
    public static void sendClickableCommand(final Player player, final String message, final String command) {
        TextComponent component = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));
        player.spigot().sendMessage(component);
    }

}
