package org.leralix.lib.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;


/**
 * This class is used to add custom NBT tags to items
 */
public class CustomNBT {

    private CustomNBT() {
        throw new IllegalStateException("Utility class");
    }

    /** Add a custom String tag to an item
     *
     * @param item      {@link ItemStack} to add the tag to.
     * @param tagName   Name of the tag.
     * @param tagValue  {@link String} value of the tag.
     */
    public static void addCustomStringTag(final Plugin pluginToRegister, final ItemStack item, final String tagName, final String tagValue) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(
                    new NamespacedKey(pluginToRegister, tagName),
                    PersistentDataType.STRING,
                    tagValue
            );
            item.setItemMeta(meta);
        }
    }

    /**
     * Get a custom String tag from an item
     * @param item      The {@link ItemStack} to get the tag from.
     * @param tagName   The name of the tag.
     * @return          The value of the tag, or null if the tag does not exist.
     */
    public static String getCustomStringTag(Plugin plugin, final ItemStack item, final String tagName) {
        if(item.getItemMeta() == null) return null;
        if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, tagName), PersistentDataType.STRING)) {
            return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, tagName), PersistentDataType.STRING);
        }
        return null;
    }

    public static void setBockMetaData(Plugin plugin, final Block block, final String metaData, final String value){
        block.setMetadata(metaData,
                new FixedMetadataValue(plugin,value));
    }
    public static String getBockMetaData(Block block, String metaData){
        if(!block.hasMetadata(metaData))
            return null;
        return block.getMetadata(metaData).get(0).asString();
    }
}
