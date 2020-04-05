package me.gmx.purpleduels.objects;

import me.gmx.purpleduels.PurpleDuels;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

public class GuiMenu implements Listener {
    private Inventory inv;
    private ArrayList<GuiButton> buttons;
    public GuiMenu(int size){
         new GuiMenu(size,null);
    }

    public GuiMenu(int size, String name){
        if (size > 8 && size % 9 == 0)
            size /= 9;
        inv = Bukkit.createInventory(null,size*9,name);
        Bukkit.getPluginManager().registerEvents(this, PurpleDuels.getInstance());
        buttons = new ArrayList<GuiButton>();
    }

    public void addButton(GuiButton button){
        inv.setItem(button.loc,button.stack);

        buttons.add(button);
    }

    public void open(Player p){
        p.openInventory(inv);
    }

    public Inventory getInventory(){
        return this.inv;
    }


    @EventHandler
    public void InventoryClick(InventoryClickEvent e){
        if (e.getInventory().equals(this.inv))
            e.setCancelled(true);
        else return;
        if (e.getClickedInventory() == null) return;
        if (e.getClick() != ClickType.DROP && e.getClickedInventory().equals(this.inv))
        for (GuiButton b : buttons){
            if (e.getSlot() == b.loc){
                b.executeAction(this,(Player)e.getWhoClicked());
                return;
            }


        }



    }

}
