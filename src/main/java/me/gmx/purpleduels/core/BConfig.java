package me.gmx.purpleduels.core;

import me.gmx.purpleduels.util.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class BConfig {


    private JavaPlugin main;
    private String name;
    private FileConfiguration fileConfiguration;
    private File file;

    public BConfig(JavaPlugin jp, String st){
        this.main = jp;
        this.name = st;
        load();
    }


    private void load(){
        if (!this.main.getDataFolder().exists()){
            FileUtils.mkDir(this.main.getDataFolder());
        }
        File f1 = new File(this.main.getDataFolder(),this.name);
        if (!f1.exists()){
            FileUtils.copy(this.main.getResource(this.name),f1);
        }
        this.file = f1;
        this.fileConfiguration = YamlConfiguration.loadConfiguration(f1);
        this.fileConfiguration.options().copyDefaults(true);
    }


    public void save(){
        try{
            this.fileConfiguration.options().copyDefaults(true);
            this.fileConfiguration.save(this.file);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    public FileConfiguration getConfig() { return this.fileConfiguration; }
}
