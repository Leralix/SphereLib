package org.leralix.lib.utils.config;

import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ConfigUtilTest {

    private static String PATH = "utils/config/";

    /**
     * Test of the {@link ConfigUtil#mergeAndPreserveLines(List, List)} method.
     * <p>
     * 2 similar text files should return an empty optional.
     */
    @Test
    void sameTextLinesTest() {

        ClassLoader classLoader = getClass().getClassLoader();
        List<String> textInFolder = ConfigUtil.loadFileAsList(classLoader.getResourceAsStream(PATH + "input-townUpgrade.txt"));

        Optional<List<String>> fileToWrite = ConfigUtil.mergeAndPreserveLines(textInFolder, textInFolder);

        assertTrue(fileToWrite.isEmpty());
    }

    /**
     * Test of the {@link ConfigUtil#mergeAndPreserveLines(List, List)} method.
     * <p>
     * 2 different text files should return the current text with the missing lines from the reference text.
     */
    @Test
    void missingCommentTest() {

        ClassLoader classLoader = getClass().getClassLoader();
        List<String> currentText = ConfigUtil.loadFileAsList(classLoader.getResourceAsStream(PATH + "input-townUpgrade.txt"));
        List<String> referenceText = ConfigUtil.loadFileAsList(classLoader.getResourceAsStream(PATH + "input-townUpgradeReference.txt"));

        Optional<List<String>> fileToWrite = ConfigUtil.mergeAndPreserveLines(referenceText, currentText);

        assertTrue(fileToWrite.isPresent());
        assertEquals(referenceText.get(0), fileToWrite.get().get(0));
    }


    /**
     * Test of the {@link ConfigUtil#mergeAndPreserveLines(List, List)} method.
     * <p>
     * 2 different text files should return the current text with the missing lines from the reference text.
     * Added lines should be overwritten because there is no blacklisted word.
     */
    @Test
    void modifyingValueTest() {

        ClassLoader classLoader = getClass().getClassLoader();
        List<String> currentText = ConfigUtil.loadFileAsList(classLoader.getResourceAsStream(PATH + "input-townUpgrade.txt"));
        List<String> referenceText = ConfigUtil.loadFileAsList(classLoader.getResourceAsStream(PATH + "input-townUpgradeReference.txt"));

        Optional<List<String>> fileToWrite = ConfigUtil.mergeAndPreserveLines(referenceText, currentText);

        assertTrue(fileToWrite.isPresent());
        assertNotEquals(referenceText.get(referenceText.size() -1), fileToWrite.get().get(fileToWrite.get().size() - 1));
        assertEquals(referenceText.size(), fileToWrite.get().size());
    }

    /**
     * Test of the {@link ConfigUtil#mergeAndPreserveLines(List, List)} method.
     * <p>
     * 2 different text files should return the current text with the missing lines from the reference text.
     * Added lines should be overwritten because there is no blacklisted word.
     */
    @Test
    void blackListAllowingMoreData() {

        ClassLoader classLoader = getClass().getClassLoader();
        List<String> blackList = List.of("upgrades");
        List<String> currentText = ConfigUtil.loadFileAsList(classLoader.getResourceAsStream(PATH + "input-townUpgrade.txt"));
        List<String> referenceText = ConfigUtil.loadFileAsList(classLoader.getResourceAsStream(PATH + "input-townUpgradeReference.txt"));

        Optional<List<String>> fileToWrite = ConfigUtil.mergeAndPreserveLines(referenceText, currentText, blackList);

        assertTrue(fileToWrite.isPresent());
        assertNotEquals(referenceText.get(referenceText.size() -1), fileToWrite.get().get(fileToWrite.get().size() - 1));
        assertEquals(referenceText.size() + 1, fileToWrite.get().size());
    }

    /**
     * Test of the {@link ConfigUtil#mergeAndPreserveLines(List, List)} method.
     * <p>
     * 2 different text files should return the current text with the missing lines from the reference text.
     * Added lines should be overwritten because there is no blacklisted word.
     */
    @Test
    void blackListWithMissingLineAtTheEnd() {

        ClassLoader classLoader = getClass().getClassLoader();
        List<String> blackList = List.of("upgrades");
        List<String> currentText = ConfigUtil.loadFileAsList(classLoader.getResourceAsStream(PATH + "input-townUpgrade.txt"));
        List<String> referenceText = ConfigUtil.loadFileAsList(classLoader.getResourceAsStream(PATH + "input-townUpgradeReferenceWithOneMissingLine.txt"));

        Optional<List<String>> fileToWrite = ConfigUtil.mergeAndPreserveLines(referenceText, currentText, blackList);

        assertTrue(fileToWrite.isPresent());
        assertEquals(referenceText.get(referenceText.size() -1), fileToWrite.get().get(fileToWrite.get().size() - 1));
        assertEquals(referenceText.size(), fileToWrite.get().size());
    }


    /**
     * Test on the configuration file of ExoticTrades.
     */
    @Test
    void exoticTradesConfigTest() {

        ClassLoader classLoader = getClass().getClassLoader();
        List<String> currentText = ConfigUtil.loadFileAsList(classLoader.getResourceAsStream(PATH + "exotictrades/firstInput.yml"));
        List<String> referenceText = ConfigUtil.loadFileAsList(classLoader.getResourceAsStream(PATH + "exotictrades/modifiedInput.yml"));

        List<String> blackList = new ArrayList<>();
        blackList.add("rareRessources");
        blackList.add("stockMarket");
        blackList.add("marketItem");
        Optional<List<String>> fileToWrite = ConfigUtil.mergeAndPreserveLines(referenceText, currentText, blackList);

        assertTrue(fileToWrite.isPresent());
        assertTrue(fileToWrite.get().contains("    enabled: true"));
    }
}
