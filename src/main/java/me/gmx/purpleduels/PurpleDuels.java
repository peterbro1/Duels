package me.gmx.purpleduels;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.gmx.purpleduels.command.CmdFriendlyDuel;
import me.gmx.purpleduels.command.CmdPurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.config.Settings;
import me.gmx.purpleduels.core.BConfig;
import me.gmx.purpleduels.handler.DataManager;
import me.gmx.purpleduels.handler.DuelManager;
import me.gmx.purpleduels.handler.GuiManager;
import me.gmx.purpleduels.handler.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PurpleDuels extends JavaPlugin {
    Logger logger;
    private static PurpleDuels ins;
    public File arenaFile;
    public FileConfiguration arenaConfig;
    public File statFile;
    public FileConfiguration statConfig;
    public File clearFile;
    public FileConfiguration clearConfig;
    public WorldGuardPlugin wgp;
    public BConfig langConfig;
    public BConfig mainConfig;

    public DuelManager duelManager;
    public DataManager dataManager;
    public GuiManager gui;

    @Override
    public void onEnable(){
        ins = this;
        logger = getLogger();
        gui = new GuiManager();

        initConfig();

        logger.log(Level.INFO, String.format("[%s] Successfully enabled version %s!", new Object[] { getDescription().getName(), getDescription().getVersion() }));

        if (getServer().getPluginManager().getPlugin("Worldguard") == null){
            logger.log(Level.SEVERE,"Worldguard not detected. Disabling");
            getServer().getPluginManager().disablePlugin(this);
        }else{
            this.wgp = WorldGuardPlugin.inst();
        }
        this.langConfig = new BConfig(this,"Lang.yml");
        this.mainConfig = new BConfig(this,"Settings.yml");
        Lang.setConfig(this.langConfig);
        Settings.setConfig(this.mainConfig);
        duelManager = new DuelManager(getInstance());
        dataManager = new DataManager(getInstance());
        gui.init();
        registerCommands();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(getInstance()),getInstance());
        duelManager.init();
    }



    @Override
    public void onDisable(){
//        kothManager.saveCountdown();
        Bukkit.getScheduler().cancelTasks(this);
        saveArenaConfig();
        saveStatConfig();
//        kothManager.tryStopCountdown();
//        kothManager.silentEnd();
        langConfig.save();
        mainConfig.save();
    }
    public void reloadConfig() {
        this.langConfig = new BConfig(this,"Lang.yml");
        Lang.setConfig(this.langConfig);
        this.mainConfig = new BConfig(this,"Settings.yml");
        Settings.setConfig(this.mainConfig);
    }

    public void log(String message){
        logger.log(Level.INFO,"["+getDescription().getName()+"] " + message);
    }

    public FileConfiguration getArenaConfig(){
        return this.arenaConfig;
    }
    private void registerCommands(){
        getCommand("duels").setExecutor(new CmdPurpleDuels(getInstance()));
        getCommand("friendlyduel").setExecutor(new CmdFriendlyDuel(getInstance()));

    }
    public FileConfiguration getStatConfig(){
        return this.statConfig;
    }
    public FileConfiguration getClearConfig(){return this.clearConfig;}
    public void saveArenaConfig(){
        try{
            getArenaConfig().save(arenaFile);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void saveStatConfig(){
        try{
            getStatConfig().save(statFile);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void saveClearConfig(){
        try{
            getClearConfig().save(clearFile);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void reloadClearConfig(){
        try {
            clearConfig.load(new File(getDataFolder(), "Leavers.yml"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void reloadArenaConfig(){
        try {
            duelManager.reload();
            arenaConfig.load(new File(getDataFolder(), "arenas.yml"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void reloadstatConfig(){
        try {
            statConfig.load(new File(getDataFolder(), "Stats.yml"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void initConfig() {

        try {
            arenaFile = new File(getDataFolder(), "arenas.yml");
            if (!arenaFile.exists()) {
                arenaFile.getParentFile().mkdirs();
                saveResource("arenas.yml", false);
            }
            arenaConfig= new YamlConfiguration();

            arenaConfig.load(arenaFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        try {
            statFile = new File(getDataFolder(), "Stats.yml");
            if (!statFile.exists()) {
                statFile.getParentFile().mkdirs();
                saveResource("Stats.yml", false);
            }
            statConfig= new YamlConfiguration();

            statConfig.load(statFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            clearFile = new File(getDataFolder(), "Leavers.yml");
            if (!clearFile.exists()) {
                clearFile.getParentFile().mkdirs();
                saveResource("Leavers.yml", false);
            }
            clearConfig= new YamlConfiguration();

            clearConfig.load(clearFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveLang(){
        this.langConfig.save();
    }
    public void saveMain(){
        this.mainConfig.save();
    }
    public BConfig getMain(){
        return this.mainConfig;
    }
    public static PurpleDuels getInstance(){
        return ins;
    }
}
