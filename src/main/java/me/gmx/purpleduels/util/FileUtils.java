package me.gmx.purpleduels.util;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.objects.Arena;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUtils {

    public static void copy(InputStream input, File file){

        try{
            FileOutputStream output = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int i;
            while ((i = input.read(b)) > 0) {
                output.write(b,0,i);
            }
            output.close();
            input.close();


        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void mkDir(File file){
        try{
            file.mkdir();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void test(List<ItemStack> list){
        resetLoot();
        FileConfiguration conf = PurpleDuels.getInstance().getStatConfig();
        for (int i = 0;i < list.size();i++){
            if (list.get(i) != null)
            conf.set(String.valueOf(i),list.get(i).serialize());
        }
        PurpleDuels.getInstance().saveStatConfig();
    }

    private static void resetLoot(){
        for(String key : PurpleDuels.getInstance().getStatConfig().getKeys(false)){
            PurpleDuels.getInstance().getStatConfig().set(key,null);
        }
        PurpleDuels.getInstance().saveStatConfig();


    }


    public static List<ItemStack> get(){
        List<ItemStack> list = new ArrayList<ItemStack>();
        FileConfiguration conf = PurpleDuels.getInstance().getStatConfig();
        PurpleDuels.getInstance().saveStatConfig();
        for (String o : PurpleDuels.getInstance().getStatConfig().getKeys(false)){
            if (conf.get(o) != null){
                try {
                    list.add(ItemStack.deserialize((Map<String, Object>) conf.get(o))); //line 73
                }catch(ClassCastException e) {
                    MemorySection sec = (MemorySection) conf.get(o);
                    list.add(ItemStack.deserialize((Map<String, Object>) sec.getValues(false)));
                }
            }
        }
        return list;
    }

    public static void storeItemList(List<ItemStack> list){
        try (BukkitObjectOutputStream output = new BukkitObjectOutputStream(new FileOutputStream(PurpleDuels.getInstance().statFile))) {
            output.writeInt(list.size());
            for (ItemStack stack : list){
                output.writeObject(stack);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static List<ItemStack> getItemList(){
        List<ItemStack> list = new ArrayList<ItemStack>();
        try (BukkitObjectInputStream input = new BukkitObjectInputStream(new FileInputStream(PurpleDuels.getInstance().statFile))) {
            final int size = input.readInt();
            for (int i = 0;i<=size;i++){
                list.add((ItemStack) input.readObject());
            }
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return list;
    }

    public static void writeArena(Arena a){
        if (!sectionExists(a.getName())){
            PurpleDuels.getInstance().getArenaConfig().createSection(a.getName());
        }
        ConfigurationSection as = PurpleDuels.getInstance().getArenaConfig().getConfigurationSection(a.getName());
        as.set("world",a.getWorld().getName());
        as.set("min-x",a.getBotLeft().getBlockX());
        as.set("min-y",a.getBotLeft().getBlockY());
        as.set("min-z",a.getBotLeft().getBlockZ());

        as.set("max-x",a.getTopRight().getBlockX());
        as.set("max-y",a.getTopRight().getBlockY());
        as.set("max-z",a.getTopRight().getBlockZ());
        PurpleDuels.getInstance().saveArenaConfig();


    }
    public static boolean sectionExists(String s){
        if (!PurpleDuels.getInstance().getArenaConfig().isConfigurationSection(s) || PurpleDuels.getInstance().getArenaConfig().getConfigurationSection(s) == null){
            return false;
        }
        return true;
    }
    public static boolean sectionExistsLoot(String s){
        if (!PurpleDuels.getInstance().getStatConfig().isConfigurationSection(s) || PurpleDuels.getInstance().getStatConfig().getConfigurationSection(s) == null){
            return false;
        }
        return true;
    }
    public static void removeArena(Arena a)throws NullPointerException{
        if (sectionExists(a.getName())){
            PurpleDuels.getInstance().getArenaConfig().set(a.getName(),null);


        }else{
            throw new NullPointerException("Arena could not be found in config");
        }
    }





}
