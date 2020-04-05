package me.gmx.purpleduels.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InvenUtil {
    public static boolean hasOmega(Player p){
            ItemStack[] stack = p.getInventory().getContents();

            for (ItemStack item : stack){
                if (item != null)
                    if (item.getEnchantments().containsKey(Enchantment.DAMAGE_ALL)){
                    if (item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) >= 83){
                        return true;
                    }
                }

            }
            return false;


    }
    public static boolean hasPvP(Player p){
        ItemStack[] stack = p.getInventory().getContents();

        for (ItemStack item : stack){
            if (item != null)
            if (item.getEnchantments().containsKey(Enchantment.DAMAGE_ALL)){
                if (item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > 10){
                    return false;
                }
            }
            if (item != null)
                if (item.getEnchantments().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL)){
                if (item.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) >= 10){
                    return false;
                }
            }
        }
        return true;

    }
}
