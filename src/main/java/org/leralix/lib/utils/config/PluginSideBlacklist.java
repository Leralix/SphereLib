package org.leralix.lib.utils.config;

import java.util.Collection;

public class PluginSideBlacklist {

    private boolean enabled;
    private int indentation;
    private Collection<String> blacklist;

    public PluginSideBlacklist(Collection<String> blackList) {
        enabled = false;
        indentation = 0;
        this.blacklist = blackList;
    }

    public boolean isInBackListPart(String currentKey){

        if(enabled){
            if(getNbIndentation(currentKey) <= indentation && !currentKey.isBlank()){
                enabled = false;
            }
            return enabled;
        }

        if(containsKey(blacklist, currentKey)){
            enabled = true;
            indentation = getNbIndentation(currentKey);
            return false; //We allow the blacklisted word to be written. Only what comes after is passed
        }
        return false;
    }


    int getNbIndentation(String pluginFileLine) {
        return ConfigUtil.getNbIndentation(pluginFileLine);
    }

    static boolean containsKey(Collection<String> blackListedWords, String key) {
        return ConfigUtil.containsKey(blackListedWords, key);
    }

}
