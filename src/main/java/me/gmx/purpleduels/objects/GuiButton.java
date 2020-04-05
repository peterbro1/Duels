package me.gmx.purpleduels.objects;

import me.gmx.purpleduels.config.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiButton {

    public ItemStack stack;
    public int loc;
    public GuiButton(ItemStack stack, int location){
        this.stack = stack;
        this.loc = location;
    }
    public GuiButton(Material type, String name, int location, String... lore){
        this.stack = new ItemStack(type);
        this.loc = location;
        setLore(lore);
        setTitle(name);
    }

    public GuiButton(Material type, String name, int location, List<String> lore) {
        this.stack = new ItemStack(type);
        this.loc = location;
        setLore(lore);
        setTitle(name);
    }

    public void setMeta(ItemMeta meta){
        this.stack.setItemMeta(meta);
    }
    public void setTitle(String s){
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(Lang.fixColors(s));
        setMeta(meta);
    }
    public void setLore(String... s){
        ItemMeta meta = stack.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        for (String str : s){
            lore.add(Lang.fixColors(str));
        }
        meta.setLore(lore);
        setMeta(meta);
    }
    public void setLore(List<String> s){
        ItemMeta meta = stack.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        for (String str : s){
            lore.add(Lang.fixColors(str));
        }
        meta.setLore(lore);
        setMeta(meta);
    }


    public void executeAction(GuiMenu menu, Player clicker){

    }




}
