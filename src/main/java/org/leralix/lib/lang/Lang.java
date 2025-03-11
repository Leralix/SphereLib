package org.leralix.lib.lang;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.leralix.lib.SphereLib;
import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

import java.io.File;
import java.util.EnumMap;

public enum Lang {
    LANGUAGE_SUCCESSFULLY_LOADED,
    HELP_DESC,
    CLICK_TO_GO_PREVIOUS_PAGE,
    CLICK_TO_GO_NEXT_PAGE,
    PAGE_NUMBER,
    PLEASE_ENTER_A_VALID_NUMBER,
    ;


    private static final EnumMap<Lang, String> translations = new EnumMap<>(Lang.class);

    public static void loadTranslations(String fileTag) {

        File langFolder = new File(SphereLib.getPlugin().getDataFolder(), "lang");

        if (!langFolder.exists()) {
            langFolder.mkdir();
        }

        File specificLangFolder = new File(langFolder, fileTag);
        if(!specificLangFolder.exists()) {
            specificLangFolder.mkdir();
        }

        File file = new File(specificLangFolder, "main.yml");

        boolean replace = ConfigUtil.getCustomConfig(ConfigTag.LANG).getBoolean("autoUpdateLangFiles",true);


        if(!file.exists() || replace) {
            SphereLib.getPlugin().saveResource("lang/" + fileTag + "/main.yml", true);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (Lang key : Lang.values()) {
            String message = config.getString("language." + key.name());
            if (message != null) {
                translations.put(key, message);
            }
        }
    }
    static final String MESSAGE_NOT_FOUND_FOR = "Message not found for ";
    static final String IN_THIS_LANGUAGE_FILE = " in this language file.";


    public String get() {
        String translation = translations.get(this);
        if (translation != null) {
            return ChatColor.translateAlternateColorCodes('§', translation);
        }
        return MESSAGE_NOT_FOUND_FOR + this.name() + IN_THIS_LANGUAGE_FILE;
    }

    public String get(Object... placeholders) {
        String translation = translations.get(this);
        if (translation != null) {
            translation = ChatColor.translateAlternateColorCodes('§', translation);
            for (int i = 0; i < placeholders.length; i++) {
                String val = placeholders[i] == null ? "null" : placeholders[i].toString();
                translation = translation.replace("{" + i + "}",val);
            }
            return translation;
        }
        return MESSAGE_NOT_FOUND_FOR + this.name() + IN_THIS_LANGUAGE_FILE;
    }
}