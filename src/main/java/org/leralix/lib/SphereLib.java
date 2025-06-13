package org.leralix.lib;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.leralix.lib.data.PluginVersion;
import org.leralix.lib.data.SoundStorage;
import org.leralix.lib.lang.Lang;
import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class SphereLib extends JavaPlugin {

    public static Plugin plugin;

    public static final PluginVersion pluginVersion = new PluginVersion(0,4,6);

    @Override
    public void onEnable() {
        plugin = this;
        Logger logger = getLogger();
        logger.log(Level.INFO, "[SphereLib] -Loading library");

        ConfigUtil.saveAndUpdateResource(this, "lang.yml");
        ConfigUtil.addCustomConfig(this, "lang.yml", ConfigTag.LANG);
        String lang = ConfigUtil.getCustomConfig(ConfigTag.LANG).getString("language");

        Lang.loadTranslations(lang);
        logger.info(Lang.LANGUAGE_SUCCESSFULLY_LOADED.get());



        logger.log(Level.INFO, "[TaN] -Loading Configs");
        ConfigUtil.saveAndUpdateResource(this, "config.yml");
        ConfigUtil.addCustomConfig(this, "config.yml", ConfigTag.MAIN);


        SoundStorage.init();
        logger.log(Level.INFO, "[SphereLib] -Library loaded successfully");
    }

    @Override
    public void onDisable() {

    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static PluginVersion getPluginVersion() {
        return pluginVersion;
    }
}
