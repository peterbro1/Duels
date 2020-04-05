package me.gmx.purpleduels.handler;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.objects.GuiButton;
import me.gmx.purpleduels.objects.GuiMenu;
import me.gmx.purpleduels.objects.QueueEntry;
import me.gmx.purpleduels.util.InvenUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiManager {


    public enum GuiList{
        DUEL_FIRST("duel0"),
        RANKED_FIRST("ranked0");

        private String s;
        GuiList(String s){
            this.s = s;
        }
    }
    private GuiMenu ranked0;
    public void init(){
        createRanked0();
       /* GuiButton elo_stats = new GuiButton(Material.BOOK,"Stats Placeholder",2,"If you see this, report it please.");

        duel0.addButton(elo_stats);*/


        ranked0 = new GuiMenu(1,"Ranked Queue");
        GuiButton omega = new GuiButton(Material.DIAMOND_AXE,Lang.TEXT_OMEGA_TITLE.toString(),0,Lang.TEXT_OMEGA_LORE.getStringList()){
            @Override
            public void executeAction(GuiMenu m, Player p){
                if(InvenUtil.hasOmega(p)) {
                    if (PurpleDuels.getInstance().duelManager.addToQueue(new QueueEntry(p, QueueEntry.QueueType.OMEGA, PurpleDuels.getInstance().dataManager.getElo(p)))) {
                        p.sendMessage(Lang.MSG_QUEUE_JOIN.toMsg());
                    } else if (!PurpleDuels.getInstance().duelManager.inDuel.contains(p)) {
                        PurpleDuels.getInstance().duelManager.removeFromQueue(p);
                        PurpleDuels.getInstance().duelManager.addToQueue(new QueueEntry(p, QueueEntry.QueueType.OMEGA, PurpleDuels.getInstance().dataManager.getElo(p)));
                        p.sendMessage(Lang.MSG_REJOIN_QUEUE.toMsg());
                    } else
                        p.sendMessage(Lang.MSG_NEED_OMEGA.toMsg());
                }else{
                    p.sendMessage(Lang.MSG_OMEGA_PLS.toMsg());
                }
            }
        };

        GuiButton kitpvp = new GuiButton(Material.DIAMOND_SWORD,Lang.TEXT_PVP_TITLE.toString(),1,Lang.TEXT_PVP_LORE.getStringList()){
            @Override
            public void executeAction(GuiMenu m, Player p) {
                if (InvenUtil.hasPvP(p)) {
                    if (PurpleDuels.getInstance().duelManager.addToQueue(new QueueEntry(p, QueueEntry.QueueType.KITPVP, PurpleDuels.getInstance().dataManager.getElo(p)))) {
                        p.sendMessage(Lang.MSG_QUEUE_JOIN.toMsg());
                    } else if (!PurpleDuels.getInstance().duelManager.inDuel.contains(p)) {
                        PurpleDuels.getInstance().duelManager.removeFromQueue(p);
                        PurpleDuels.getInstance().duelManager.addToQueue(new QueueEntry(p, QueueEntry.QueueType.KITPVP, PurpleDuels.getInstance().dataManager.getElo(p)));
                        p.sendMessage(Lang.MSG_REJOIN_QUEUE.toMsg());
                    } else
                        p.sendMessage(Lang.MSG_NEED_PVP.toMsg());
                }else{
                    p.sendMessage(Lang.MSG_KITPVP_PLS.toMsg());
                }

            }
        };
        ranked0.addButton(omega);
        ranked0.addButton(kitpvp);





    }

    public GuiMenu createRanked0(){
        GuiMenu
        duel0 = new GuiMenu(1,Lang.TEXT_DUEL_MENU.toString());
        GuiButton friendlyduel = new GuiButton(Material.WOOD_SWORD,Lang.TEXT_FRIENDLYGAME_TITLE.toString(),2,Lang.TEXT_FRIENDLYGAME_LORE.getStringList()){
            @Override
            public void executeAction(GuiMenu m, Player p){
                p.closeInventory();
                p.sendMessage(Lang.MSG_FRIENDLYDUEL_CLICK.toMsg());
            }
        };
        GuiButton ranked = new GuiButton(Material.DIAMOND_AXE,Lang.TEXT_RANKEDQUEUE_TITLE.toString(),6,Lang.TEXT_RANKEDQUEUE_LORE.getStringList()){
            @Override
            public void executeAction(GuiMenu m, Player p){
                PurpleDuels.getInstance().gui.open(p,GuiList.RANKED_FIRST);
            }
        };
        duel0.addButton(friendlyduel);
        duel0.addButton(ranked);
        return duel0;
    }

    public void open(Player p, GuiList inventory){
        switch (inventory){
            case DUEL_FIRST:
                GuiMenu m = createRanked0();
                GuiButton elo_stats2 = new GuiButton(Material.BOOK,"Stats Placeholder",4,"If you see this, report it please.");
                elo_stats2.setTitle(Lang.TEXT_STATS_TITLE.toString().replace("%player",p.getName()));
                List<String> lore = Lang.TEXT_STATS_LORE.getStringList();
                List<String> newLore = new ArrayList<String>();
                lore.forEach(s -> newLore.add(s
                        .replace("%elo%",String.valueOf(PurpleDuels.getInstance().dataManager.getElo(p)))
                        .replace("%wins%",String.valueOf(PurpleDuels.getInstance().dataManager.getWIns(p)))
                        .replace("%slash%","/")));
                elo_stats2.setLore(newLore);
                m.addButton(elo_stats2);
                m.open(p);
                break;
            case RANKED_FIRST:
                p.closeInventory();
                ranked0.open(p);
                break;
        }
    }

}
