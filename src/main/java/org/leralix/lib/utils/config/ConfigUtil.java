package org.leralix.lib.utils.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.leralix.lib.SphereLib;

import java.io.*;
import java.util.*;

/**
 * This class is used for config related utilities.
 */
public class ConfigUtil {
    private ConfigUtil() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * This map is used to store the custom configs.
     */
    private static final Map<ConfigTag, FileConfiguration> configs = new EnumMap<>(ConfigTag.class);

    /**
     * Get a custom config by its name.
     * @param tag       The tag of the config file.
     * @return          The {@link FileConfiguration } object.
     */
    public static FileConfiguration getCustomConfig(final ConfigTag tag) {
        return configs.get(tag);
    }

    /**
     * Load a custom config file into the memory
     * @param fileName  The name of the file to load
     */
    public static void addCustomConfig(Plugin plugin, String fileName, ConfigTag tag) {

        File configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            plugin.getLogger().severe(() -> fileName + " does not exist!");
            return;
        }
        configs.put(tag, YamlConfiguration.loadConfiguration(configFile));
    }



    private static boolean containsKey(Collection<String> blackListedWords, String key) {
        for(String word : blackListedWords){
            if(key.startsWith(word)){
                return true;
            }
        }
        return false;
    }

    public static void saveAndUpdateResource(Plugin plugin, final String fileName) {
        saveAndUpdateResource(plugin, fileName, Collections.emptyList());
    }

        /**
         * Save and update a resource file. If some lines are missing in the current file, they will be added at the correct position.
         * @param fileName  The name of the resource file.
         */
    public static void saveAndUpdateResource(Plugin plugin,final String fileName, Collection<String> sectionBlacklist) {
        File currentFile = new File(plugin.getDataFolder(), fileName);
        if (!currentFile.exists()) {
            plugin.saveResource(fileName, false);
            return;
        }

        InputStream baseFile = plugin.getResource(fileName);

        List<String> baseFileLines = loadFileAsList(baseFile);
        List<String> currentFileLines = loadFileAsList(currentFile);

        boolean updated = mergeAndPreserveLines(plugin, currentFile, baseFileLines, currentFileLines, sectionBlacklist);

        if (updated) {
            plugin.getLogger().info(() -> "The file " + fileName + " has been updated with missing lines.");
        } else {
            plugin.getLogger().info(() -> "No updates were necessary for the file " + fileName + ".");
        }

    }

    /**
     * Load a file as a list of lines.
     * @param file  The input file.
     * @return      A list of lines, or null if an error occurs.
     */
    private static List<String> loadFileAsList(InputStream file) {
        if (file == null) return Collections.emptyList();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            SphereLib.getPlugin().getLogger().warning("Error while loading file as list : " + file);
            return Collections.emptyList();
        }
    }

    private static List<String> loadFileAsList(File file) {
        if (file == null) return Collections.emptyList();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            SphereLib.getPlugin().getLogger().warning("Error while loading file as list : " + file);
            return Collections.emptyList();
        }
    }

    /**
     * Merge the base file lines into the current file lines, preserving order and comments.
     * @param file              The file to update.
     * @param pluginFileLines     The lines from the base file.
     * @param actualFileLine  The lines from the current file.
     * @return                  True if updates were made, false otherwise.
     */
    private static boolean mergeAndPreserveLines(Plugin plugin, File file, List<String> pluginFileLines, List<String> actualFileLine, Collection<String> sectionBlacklist) {
        List<String> mergedLines = new ArrayList<>();


        boolean updated = false;
        int indexActual = 0;
        int bannedSectionIndentation = 0;
        boolean inBannedSection = false;

        for (String pluginFileLine : pluginFileLines) {

            if(inBannedSection){
                if(getNbIndentation(pluginFileLine) > bannedSectionIndentation){
                    continue; //Skip line until end of banned section | pluginConfigSide
                }
                //Then, update the actual file line scroller

                while(actualFileLine.size() > indexActual && inBannedSection){
                    String actualLine = actualFileLine.get(indexActual);

                    if(getNbIndentation(actualLine) <= bannedSectionIndentation){
                        inBannedSection = false;
                        continue;
                    }
                    mergedLines.add(actualLine);
                    indexActual++;
                }
            }

            if(actualFileLine.size() == indexActual){
                mergedLines.add(pluginFileLine);
                plugin.getLogger().info("Added new config line : " + pluginFileLine);
                updated = true;
                continue;
            }

            String currentLine = actualFileLine.get(indexActual);
            if(pluginFileLine.startsWith("#")){
                if(currentLine.equals(pluginFileLine)){
                    indexActual++;
                }
                else {
                    updated = true;
                }
                mergedLines.add(pluginFileLine);
                continue;
            }

            String pluginKey = extractKey(pluginFileLine);
            String currentKey = extractKey(currentLine);

            if(containsKey(sectionBlacklist, pluginKey)){
                inBannedSection = true;
                bannedSectionIndentation = getNbIndentation(pluginFileLine);
                indexActual++;

                mergedLines.add(pluginFileLine);

                if(!currentKey.equals(pluginKey)){
                    updated = true;
                }
                continue;
            }

            if(pluginKey.equals(currentKey)){
                mergedLines.add(currentLine);
                indexActual++;
                continue;
            }
            mergedLines.add(pluginFileLine);
            indexActual++;
            updated = true;
        }

        if (updated) {
            writeToFile(mergedLines, file);
        }

        return updated;
    }

    private static int getNbIndentation(String pluginFileLine) {
        int i = 0;
        while(i < pluginFileLine.length() && pluginFileLine.charAt(i) == ' '){
            i++;
        }
        return i;
    }

    /**
     * Extracts a key from a configuration line.
     * @param line  The line to process.
     * @return      The key if found, or null otherwise.
     */
    private static String extractKey(String line) {
        if(line == null)
            return "";
        line = line.trim();
        if (line.isEmpty()) {
            return "";
        }
        if(line.startsWith("#")){
            return line;
        }
        if (line.contains(":")) {
            return line.split(":")[0].trim();
        }
        return line;
    }

    /**
     * Write a list of lines to a file.
     * @param lines         The list of lines to write.
     * @param fileToWrite   The file to write to.
     */
    private static void writeToFile(List<String> lines, File fileToWrite) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWrite, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (Exception e) {
            SphereLib.getPlugin().getLogger().warning("Error while writing to file : " + fileToWrite);
        }
    }
}
