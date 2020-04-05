package me.gmx.purpleduels.objects;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.config.Settings;
import me.gmx.purpleduels.handler.DataManager;
import me.gmx.purpleduels.util.InvenUtil;
import me.gmx.purpleduels.util.LocationUtil;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

public class Duel {

    private QueueEntry player1,player2;
    private Arena arena;
    private QueueEntry.QueueType type;
    private int taskId;
    private Location p1,p2;
    private int seconds;
    private int max;
    public boolean active;
    private UUID id;
    public Duel(QueueEntry player1, QueueEntry player2, Arena arena){
        this.player1=player1;
        this.player2=player2;
        this.arena = arena;
        active = false;
        this.type = player1.getType();
        seconds = 0;
        id = UUID.randomUUID();
        max = Settings.MAX_DUEL_TIME_SECONDS.getNumber();
    }

    public Duel(Player p1, Player p2, Arena arena){
        this.player1 = new QueueEntry(p1, QueueEntry.QueueType.FRIENDLY,0);
        this.player2 = new QueueEntry(p2,QueueEntry.QueueType.FRIENDLY,0);
        this.arena = arena;
        active = false;
        this.id = UUID.randomUUID();
        this.type = QueueEntry.QueueType.FRIENDLY;
        seconds = 0;
        max = Settings.MAX_DUEL_TIME_SECONDS.getNumber();
    }

    public UUID getUUID(){
        return id;
    }

    public void start(){
        if (!getPlayer1().isOnline()) {
            getPlayer2().sendMessage("Other player disconnected! You win!");
            end(getPlayer1());
        }
        else if (!getPlayer2().isOnline()) {
            end(getPlayer1());
            getPlayer1().sendMessage("Other player disconnected! You win!");
            return;
        }

        switch(type){
            case OMEGA:
                if (!InvenUtil.hasOmega(getPlayer1()) || !InvenUtil.hasOmega(getPlayer2())){
                    getPlayer1().sendMessage(Lang.MSG_INVALID_ITEMS.toMsg());
                    getPlayer2().sendMessage(Lang.MSG_INVALID_ITEMS.toMsg());
                    endNoWinner();
                    return;

                }
                break;
            case KITPVP:
                if (!InvenUtil.hasPvP(getPlayer1()) || !InvenUtil.hasPvP(getPlayer2())){
                    getPlayer1().sendMessage(Lang.MSG_INVALID_ITEMS.toMsg());
                    getPlayer2().sendMessage(Lang.MSG_INVALID_ITEMS.toMsg());
                    endNoWinner();
                    return;
                }
                break;

        }
        teleportPlayers();
        active = true;
        taskId = new BukkitRunnable(){
            public void run(){
            checkPlayers();
            seconds++;

            if (seconds > max) {
                endNoWinner();
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }

        if (!active)
            cancel();

        }}.runTaskTimer(PurpleDuels.getInstance(),60,20).getTaskId();
    }

    public int getTime(){return seconds;}

    public Player getPlayer1(){return player1.getPlayer();}
    public Player getPlayer2(){return player2.getPlayer();}
    public Arena getArena(){return arena;}

    private void checkPlayers(){


        if (!active) return;
        if (!player1.getPlayer().isOnline()){
            end(player1.getPlayer());
            return;
        }else if (!player2.getPlayer().isOnline()) {
            end(player2.getPlayer());
            return;
        }



        if (!arena.isInsideArena(player1.getPlayer().getLocation())){
            player1.getPlayer().teleport(arena.getMiddle());
            player1.getPlayer().damage(500,player2.getPlayer());
            player1.getPlayer().sendMessage("You have left the arena.");
        }else if (!arena.isInsideArena(player2.getPlayer().getLocation())){
            player2.getPlayer().teleport(arena.getMiddle());
            player2.getPlayer().damage(500,player1.getPlayer());
            player2.getPlayer().sendMessage("You have left the arena.");
        }


    }

    public void end(Player loser){
        try{
            Bukkit.getScheduler().cancelTask(taskId);
        }catch(Exception e){

        }

        active = false;

        if (player1.getPlayer().getName().equals(loser.getName())){
            PurpleDuels.getInstance().dataManager.setWins(getPlayer2(),PurpleDuels.getInstance().dataManager.getWIns(getPlayer2())+1);
            Bukkit.broadcastMessage(Lang.MSG_DEFEAT_BROADCAST.toString()
                    .replace("%winner%",getPlayer2().getName())
                    .replace("%loser%",loser.getName()));
            if (type != QueueEntry.QueueType.FRIENDLY) {
                PurpleDuels.getInstance().dataManager.calculateElo(player2, player1);
                if (dropHead(loser,player2.getPlayer())){
                    player2.getPlayer().sendMessage(Lang.MSG_HEAD_DROP.toMsg());
                    player2.getPlayer().playSound(player2.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                }
            }

        }else{
            PurpleDuels.getInstance().dataManager.setWins(getPlayer1(),PurpleDuels.getInstance().dataManager.getWIns(getPlayer1())+1);
            Bukkit.broadcastMessage(Lang.MSG_DEFEAT_BROADCAST.toString()
                    .replace("%winner%",getPlayer1().getName())
                    .replace("%loser%",loser.getName()));
            PurpleDuels.getInstance().duelManager.inDuel.remove(getPlayer2());
            if (type != QueueEntry.QueueType.FRIENDLY) {
                PurpleDuels.getInstance().dataManager.calculateElo(player1, player2);
                if (dropHead(loser,player1.getPlayer())){
                    player1.getPlayer().sendMessage(Lang.MSG_HEAD_DROP.toMsg());
                    player1.getPlayer().playSound(player2.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                }
            }
        }
        returnPlayers(loser);

        new BukkitRunnable(){
            public void run(){
                PurpleDuels.getInstance().duelManager.removeDuel(id);
                Player pl = getPlayer1().getName().equals(loser.getName()) ? getPlayer2() : getPlayer1();
                returnPlayers(pl);
            }

        }.runTaskLater(PurpleDuels.getInstance(), Settings.DUEL_RETURN_DELAY.getNumber()*20);
    }

    public void endNoWinner(){
        active = false;
        PurpleDuels.getInstance().duelManager.inDuel.remove(getPlayer1());
        PurpleDuels.getInstance().duelManager.inDuel.remove(getPlayer2());
        returnPlayers();
        PurpleDuels.getInstance().duelManager.removeDuel(id);
    }

    public void teleportPlayers(){

        for(Player p : Bukkit.getOnlinePlayers()){
            if(getArena().isInArea(p) && !p.isOp()){
                Bukkit.dispatchCommand(p,"spawn");
            }
        }
        try {
            for (Entity e : getArena().getWorld().getNearbyEntities(getArena().getMiddle(), 100, 100, 100)) {
                if (getArena().isInsideArena(e.getLocation()) && (e instanceof Item || e instanceof ItemStack))
                    e.remove();
            }
        }catch(Exception e){
            PurpleDuels.getInstance().log("Failed to remove entities in arena!");
        }
        this.p1 = player1.getPlayer().getLocation();
        this.p2 = player2.getPlayer().getLocation();

        Location l1 = new Location(getArena().getWorld(),getArena().getBotLeft().getX()+2,getArena().getBotLeft().getY()+2,getArena().getBotLeft().getZ()+2);
        Location l2 = new Location(getArena().getWorld(),getArena().getTopRight().getX()-2,getArena().getBotLeft().getY()+2,getArena().getTopRight().getZ()-2);
        Vector dir = l2.clone().subtract(l1).toVector();
        l1.setDirection(dir);
        Vector dir2 = l1.clone().subtract(l2).toVector();
        l2.setDirection(dir2);
        getPlayer1().teleport(l1); //double these to fix the world issue
        getPlayer1().teleport(l1);
        getPlayer2().teleport(l2);
        getPlayer2().teleport(l2);







        getPlayer1().sendMessage(Lang.MSG_DUEL_START.toMsg());
        getPlayer2().sendMessage(Lang.MSG_DUEL_START.toMsg());

    }

    public boolean dropHead(Player loser,Player winner){
            Random r = new Random();
            if (r.nextInt(101)+1 < Settings.HEAD_RATE.getNumber()){
                ItemStack skull = new ItemStack(Material.SKULL_ITEM,1, (byte)SkullType.PLAYER.ordinal());
                SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(loser.getUniqueId()));
                meta.setLore(Arrays.asList(Lang.LORE_HEAD_DROP.toString()
                        .replace("%winner%",winner.getName())
                        .replace("%loser%",loser.getName())
                        .replace("%arena%",getArena().getName())
                        .replace("%hearts%",String.valueOf(winner.getHealth()))
                        .replace("%seconds%",String.valueOf(getTime()))));
                skull.setItemMeta(meta);
                loser.getWorld().dropItemNaturally(loser.getLocation(),skull);
                return true;
            }
            return false;
    }

    public void returnPlayers(){
       // getPlayer1().teleport(p1);
       // getPlayer2().teleport(p2);
        Bukkit.dispatchCommand(getPlayer1(),"spawn");
        Bukkit.dispatchCommand(getPlayer2(),"spawn");

    }
    public void returnPlayers(Player p){
        Bukkit.dispatchCommand(p,"spawn");
        PurpleDuels.getInstance().duelManager.inDuel.remove(p);
//        PurpleDuels.getInstance().duelManager.inDuel.remove(getPlayer2());
    }



}
