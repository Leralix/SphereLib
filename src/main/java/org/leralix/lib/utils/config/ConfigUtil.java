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
    ConfigUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * This map is used to store the custom configs.
     */
    static final Map<ConfigTag, FileConfiguration> configs = new EnumMap<>(ConfigTag.class);

    /**
     * Get a custom config by its name.
     *
     * @param tag The tag of the config file.
     * @return The {@link FileConfiguration } object.
     */
    public static FileConfiguration getCustomConfig(final ConfigTag tag) {
        return configs.get(tag);
    }
    public static void addCustomConfig(Plugin plugin, String fileName, ConfigTag tag) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            plugin.getLogger().severe(() -> fileName + " does not exist!");
            return;
        }
        addCustomConfig(configFile, tag);
    }

    /**
     * Load a custom config file into the memory
     *
     * @param file The file to load
     * @param tag  The tag to associate with
     */
    public static void addCustomConfig(File file, ConfigTag tag) {
        configs.put(tag, YamlConfiguration.loadConfiguration(file));
    }



    static boolean containsKey(Collection<String> blackListedWords, String key) {
        for (String word : blackListedWords) {
            if (key.startsWith(word)) {
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
     *
     * @param fileName The name of the resource file.
     */
    public static void saveAndUpdateResource(Plugin plugin, final String fileName, Collection<String> sectionBlacklist) {
        File currentFile = new File(plugin.getDataFolder(), fileName);
        if (!currentFile.exists()) {
            plugin.saveResource(fileName, false);
            return;
        }

        InputStream baseFile = plugin.getResource(fileName);

        List<String> baseFileLines = loadFileAsList(baseFile);
        List<String> currentFileLines = loadFileAsList(currentFile);

        Optional<List<String>> test = mergeAndPreserveLines(baseFileLines, currentFileLines, sectionBlacklist);

        test.ifPresent(list -> {
            writeToFile(list, currentFile);
            plugin.getLogger().info(() -> "The file " + fileName + " has been updated with missing lines.");
        });
    }

    /**
     * Load a file as a list of lines.
     *
     * @param file The input file.
     * @return A list of lines, or null if an error occurs.
     */
    static List<String> loadFileAsList(InputStream file) {
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

    static List<String> loadFileAsList(File file) {
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
     *
     * @param pluginFileLines The lines from the base file.
     * @param actualFileLine  The lines from the current file.
     * @return A list of lines to write to the current file, or {@link Optional#empty()} if no update is needed.
     */
    static Optional<List<String>> mergeAndPreserveLines(List<String> pluginFileLines, List<String> actualFileLine) {
        return mergeAndPreserveLines(pluginFileLines, actualFileLine, Collections.emptyList());
    }

    /**
     * Merge the base file lines into the current file lines, preserving order and comments.
     *
     * @param pluginFileLines  The lines from the base file.
     * @param actualFileLine   The lines from the current file.
     * @param sectionBlacklist A list of sections to ignore.
     * @return A list of lines to write to the current file, or {@link Optional#empty()} if no update is needed.
     */
    static Optional<List<String>> mergeAndPreserveLines(List<String> pluginFileLines, List<String> actualFileLine, Collection<String> sectionBlacklist) {
        List<String> mergedLines = new ArrayList<>();


        boolean updated = false;
        int indexActual = 0;
        int bannedSectionIndentation = 0;
        boolean inBannedSection = false;

        PluginSideBlacklist pluginSideBlacklist = new PluginSideBlacklist(sectionBlacklist);


        for (String pluginFileLine : pluginFileLines) {

            if (inBannedSection) {

                while (actualFileLine.size() > indexActual  && inBannedSection) {
                    String actualLine = actualFileLine.get(indexActual);

                    if (getNbIndentation(actualLine) <= bannedSectionIndentation && !actualLine.isBlank()) {
                        inBannedSection = false;
                        continue;
                    }
                    mergedLines.add(actualLine);
                    indexActual++;
                }

            }

            if(pluginSideBlacklist.isInBackListPart(pluginFileLine)) {
                continue;
            }

            //If index is out of bonds, accept all incomming config lines
            if(indexActual >= actualFileLine.size()){
                mergedLines.add(pluginFileLine);
                updated = true;
                continue;
            }

            String currentLine = actualFileLine.get(indexActual);

            String pluginKey = extractKey(pluginFileLine);
            String currentKey = extractKey(currentLine);

            if (containsKey(sectionBlacklist, pluginKey)) {
                inBannedSection = true;
                bannedSectionIndentation = getNbIndentation(pluginFileLine);
                indexActual++;
                mergedLines.add(pluginFileLine);
                if (!currentKey.equals(pluginKey)) {
                    updated = true;
                }
                continue;
            }

            if (pluginFileLine.startsWith("#")) {
                if (currentLine.equals(pluginFileLine)) {
                    indexActual++;
                } else {
                    updated = true;
                }
                mergedLines.add(pluginFileLine);
                continue;
            }

            int existingIndex = findLineIndexWithKey(actualFileLine, indexActual, pluginKey);
            if (existingIndex != -1) {
                if (existingIndex != indexActual) {
                    updated = true;
                }
                mergedLines.add(actualFileLine.get(existingIndex));
                indexActual = existingIndex + 1;
                continue;
            }

            if (pluginKey.equals(currentKey)) {
                mergedLines.add(currentLine);
                indexActual++;
            } else {
                mergedLines.add(pluginFileLine);
                updated = true;
            }
        }

        return updated ? Optional.of(mergedLines) : Optional.empty();
    }

    private static int findLineIndexWithKey(List<String> lines, int startIndex, String key) {
        for (int i = startIndex; i < lines.size(); i++) {
            if (extractKey(lines.get(i)).equals(key)) {
                return i;
            }
        }
        return -1;
    }

    static int getNbIndentation(String pluginFileLine) {
        int i = 0;
        while (i < pluginFileLine.length() && pluginFileLine.charAt(i) == ' ') {
            i++;
        }
        return i;
    }

    /**
     * Extracts a key from a configuration line.
     *
     * @param line The line to process.
     * @return The key if found, or null otherwise.
     */
    static String extractKey(String line) {
        if (line == null)
            return "";
        line = line.trim();
        if (line.isEmpty()) {
            return "";
        }
        if (line.startsWith("#")) {
            return line;
        }
        if (line.contains(":")) {
            return line.split(":")[0].trim();
        }
        return line;
    }

    /**
     * Write a list of lines to a file.
     *
     * @param lines       The list of lines to write.
     * @param fileToWrite The file to write to.
     */
    static void writeToFile(List<String> lines, File fileToWrite) {

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
