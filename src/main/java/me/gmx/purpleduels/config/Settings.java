package me.gmx.purpleduels.config;


import me.gmx.purpleduels.core.BConfig;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public enum Settings {


    STARTING_ELO("350"),
    DUEL_RETURN_DELAY("5"),
    QUEUE_TIME_FLEX("3"),
    ELO_FLEX("5"),
    QUIT_DUEL_ACTIVE("false"),
    MAX_DUEL_TIME_SECONDS("300"),
    COMBAT_TAG_TIMER("10"),
    QUIT_DUEL_CMD("tempban %player% 30m Leaving Duel"),
    HEAD_RATE("10"),
    ELO_GAIN_RATE("30");


    private String defaultValue;
    private static BConfig config;
    private String prefix = ChatColor.DARK_RED + "" + ChatColor.BLACK;
    Settings(String str){
        defaultValue = str;
    }


    public String getPath() { return name(); }

    public String getDefaultValue() { return this.defaultValue; }


    public static void setConfig(BConfig paramBConfig) {
        config = paramBConfig;
        load();
    }
    public String getEncodeString(){
        return prefix + ChatColor.translateAlternateColorCodes('&',config.getConfig().getString(getPath()));

    }
    public int getNumber() {
        return Integer.parseInt(config.getConfig().getString(getPath()));
    }

    public List<String> getStringList(){
        return Arrays.asList(getString().split("/"));
    }


    public boolean getBoolean() throws Exception{

        try {
            return Boolean.valueOf(config.getConfig().getString(getPath()));
        }catch(NullPointerException e) {
            throw new Exception("Value could not be converted to a boolean");
        }

    }
    public String getString(){
        return ChatColor.translateAlternateColorCodes('&',config.getConfig().getString(getPath()));
    }
    public void set( String o){
        config.getConfig().set(getPath(),o);
    }

    private static void load() {
        for (Settings lang : values()) {
            if (config.getConfig().getString(lang.getPath()) == null) {
                config.getConfig().set(lang.getPath(), lang.getDefaultValue());
            }
        }
        config.save();
    }
}
