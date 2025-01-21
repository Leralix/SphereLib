package org.leralix.lib.utils;

import org.bukkit.entity.Player;
import org.leralix.lib.data.SoundData;
import org.leralix.lib.data.SoundEnum;
import org.leralix.lib.data.SoundStorage;
import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

/**
 * Utility class for playing sounds
 */
public class SoundUtil {

    private SoundUtil() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * Play a predefined sound to the player
     * @param player    The player to play the sound to
     * @param soundEnum The sound to play
     */
    public static void playSound(Player player, SoundEnum soundEnum){
        if(ConfigUtil.getCustomConfig(ConfigTag.MAIN).getBoolean("enableSounds",true)){
            SoundData soundData = SoundStorage.getSoundData(soundEnum);
            if(soundData != null)
                soundData.playSound(player);
        }
    }
}
