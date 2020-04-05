package me.gmx.purpleduels.handler;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.config.Settings;
import net.brcdev.gangs.GangsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {
    private PurpleDuels ins;
    public PlayerListener(PurpleDuels ins){
        this.ins = ins;
    }

    @EventHandler
    public void death(PlayerDeathEvent e){
        if (PurpleDuels.getInstance().duelManager.inDuel.contains(e.getEntity())){
            PurpleDuels.getInstance().duelManager.getDuel(e.getEntity()).end(e.getEntity());
        }else if (DuelManager.isInQueue(e.getEntity())){
            PurpleDuels.getInstance().duelManager.removeFromQueue(e.getEntity());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void entit(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player))
            return;
        /*if (GangsPlugin.getInstance().gangManager.isInGang(Bukkit.getOfflinePlayer(e.getEntity().getUniqueId()))){
            if (GangsPlugin.getInstance().gangManager.getPlayersGang(Bukkit.getOfflinePlayer(e.getEntity().getUniqueId()))
                    == GangsPlugin.getInstance().gangManager.getPlayersGang(Bukkit.getOfflinePlayer(e.getDamager().getUniqueId()))
            || GangsPlugin.getInstance().gangManager.getPlayersGang(Bukkit.getOfflinePlayer(e.getEntity().getUniqueId())).getAllyGangs()
                    .contains(GangsPlugin.getInstance().gangManager.getPlayersGang(Bukkit.getOfflinePlayer(e.getDamager().getUniqueId())))){
                Player p1 = (Player)e.getEntity();
                Player p2 = (Player)e.getDamager();
                if (PurpleDuels.getInstance().duelManager.inDuel.contains(p2) && PurpleDuels.getInstance().duelManager.inDuel.contains(p1))
                e.setCancelled(false);
            }
        }*/
        if (ins.duelManager.inDuel.contains(e.getEntity())){
            e.setCancelled(false);
        }else{
            ins.duelManager.addCombat((Player)e.getEntity());
            if (e.getDamager() instanceof Player){
                ins.duelManager.addCombat((Player)e.getDamager());

            }

        }


    }

    @EventHandler
    public void cmd(PlayerCommandPreprocessEvent e){
        if (PurpleDuels.getInstance().duelManager.inDuel.contains(e.getPlayer()) && !e.getPlayer().isOp() && !e.getMessage().startsWith("duel leave")){
            e.getPlayer().sendMessage(Lang.MSG_CMD_DISABLE.toMsg());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void join(PlayerJoinEvent e){
        if (PurpleDuels.getInstance().dataManager.clearInv.contains(e.getPlayer().getUniqueId())){
            e.getPlayer().getInventory().clear();
            e.getPlayer().damage(20);
            e.getPlayer().sendMessage("&4You left your previous duel. Your items have been lost");
            PurpleDuels.getInstance().dataManager.removeClearInv(e.getPlayer());
        }
    }

    @EventHandler
    public void leave(PlayerQuitEvent e){
        PurpleDuels.getInstance().duelManager.removeRequest(e.getPlayer());
        PurpleDuels.getInstance().duelManager.removeFromQueue(e.getPlayer());
        if (PurpleDuels.getInstance().duelManager.inDuel.contains(e.getPlayer())) {
            ItemStack[] items = e.getPlayer().getInventory().getContents();
            e.getPlayer().getInventory().clear();
            for (ItemStack stack : items) {
                if (stack != null)
                e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(),stack);
            }

            PurpleDuels.getInstance().dataManager.addClearInv(e.getPlayer());



                PurpleDuels.getInstance().duelManager.getDuel(e.getPlayer()).end(e.getPlayer());


            try {
                if (Settings.QUIT_DUEL_ACTIVE.getBoolean())
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Settings.QUIT_DUEL_CMD.getString());
            } catch (Exception ex) {
                PurpleDuels.getInstance().log("QUIT_DUEL_ACTIVE must be set to 'true' or 'false'!");
            }
        }
    }
}




