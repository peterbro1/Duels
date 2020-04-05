package me.gmx.purpleduels.objects;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

public class QueueEntry {
    private Player p;
    private QueueType type;
    private int elo;
    private long entry;
    public QueueEntry(Player p, QueueType type, int elo){
        this.p = p;
        this.type = type;
        this.elo = elo;
        this.entry = System.currentTimeMillis();
    }
    public Player getPlayer() {return p;}
    public int getElo(){return elo;}
    public QueueType getType(){return type;}
    public int getSecondsFromEntry(){return (int)((System.currentTimeMillis()-entry)/1000);}
    public int getFlex(){return getSecondsFromEntry()* Settings.QUEUE_TIME_FLEX.getNumber()/10;}

    public boolean suitable(QueueEntry e){
        //Bukkit.broadcastMessage("Suitable match of " + this.getPlayer().getName() +" and "+ e.getPlayer().getName() +":::" + String.valueOf(Math.pow(Math.abs(e.getElo() - getElo()) * (1/(this.getFlex()+1+e.getFlex()+1)),2)));
      /*  if (Math.pow(Math.abs(e.getElo() - getElo()) * (1/(this.getFlex()+1+e.getFlex()+1)),2) < Settings.ELO_FLEX.getNumber() ){
            return true;
        }*/
        if (Math.abs(this.getElo() - ((this.getElo() + e.getElo()) / 2)) <(30 + e.getFlex()*Settings.ELO_FLEX.getNumber() + (1/2)*this.getFlex()*Settings.ELO_FLEX.getNumber())){
            if (e.getPlayer().getAddress().equals(this.getPlayer().getAddress()))
                return false;
            return true;
        }

        return false;

    }

    public double test(QueueEntry e){
        return Math.pow(Math.abs(e.getElo() - getElo()) * (1/(this.getFlex()+1+e.getFlex()+1)),2);
    }



    public enum QueueType{
        OMEGA,
        FRIENDLY,
        KITPVP;
    }

}
