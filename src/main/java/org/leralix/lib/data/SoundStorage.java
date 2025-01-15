package org.leralix.lib.data;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SoundStorage {

    private SoundStorage() {
        throw new IllegalStateException("Utility class");
    }

    private static final Map<SoundEnum, SoundData> soundMap = new EnumMap<>(SoundEnum.class);

    public static void init(){
        ConfigurationSection soundsSection = ConfigUtil.getCustomConfig(ConfigTag.LIB).getConfigurationSection("sounds");
        if (soundsSection != null) {
            for (String key : soundsSection.getKeys(false)) {
                List<String> soundValues = soundsSection.getStringList(key);

                Sound soundName = Sound.valueOf(soundValues.get(0));
                int volume = Integer.parseInt(soundValues.get(1));
                float pitch = Float.parseFloat(soundValues.get(2));

                soundMap.put(SoundEnum.valueOf(key), new SoundData(soundName, volume, pitch));
            }
        }
    }

    public static SoundData getSoundData(SoundEnum soundName){
        return soundMap.get(soundName);
    }
}
