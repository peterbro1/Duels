package me.gmx.purpleduels.handler;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Settings;
import me.gmx.purpleduels.objects.QueueEntry;
import me.gmx.purpleduels.util.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataManager {

    private PurpleDuels ins;
    public ArrayList<UUID> clearInv;
    public DataManager(PurpleDuels ins){

        this.ins = ins;
        clearInv = new ArrayList<UUID>();
        loadList();
    }

    private void loadList(){
        try {
            if (ins.getClearConfig().getString("clearInventory") != null)
            for (String s : ins.getClearConfig().getStringList("clearInventory")) {
                clearInv.add(UUID.fromString(s));
            }
        }catch(Exception e){
            System.out.println("Clear Inventory list not found");
            e.printStackTrace();
        }
    }

    public void addClearInv(Player p){
        List<String> a;
        try{
            a = ins.getClearConfig().getStringList("clearInventory");
        }catch (Exception e){
            a = new ArrayList<String>();
            e.printStackTrace();
        }
        a.add(p.getUniqueId().toString());
        clearInv.add(p.getUniqueId());
        ins.getClearConfig().set("clearInventory",a);
        ins.saveClearConfig();
    }

    public void removeClearInv(Player p){
        if (clearInv.contains(p.getUniqueId())){
            clearInv.remove(p.getUniqueId());
        }
        List<String> a;
        try{
            if (!ins.getClearConfig().getString("clearInventory").isEmpty())
            a = ins.getClearConfig().getStringList("clearInventory");
            else a = new ArrayList<String>();
        }catch (Exception e){
            a = new ArrayList<String>();
        }
        if (a.contains(p.getUniqueId().toString())){
            a.remove(p.getUniqueId().toString());
        }
        ins.getClearConfig().set("clearInventory",a);
        ins.saveClearConfig();


    }


    public int getElo(Player p){
        try {
            return (int) ins.getStatConfig().get(p.getUniqueId().toString()+".elo");
        }catch(NullPointerException e){
            ins.getStatConfig().set(p.getUniqueId().toString()+".elo", Settings.STARTING_ELO.getNumber());
            ins.saveStatConfig();
            return Settings.STARTING_ELO.getNumber();
        }catch(ClassCastException e){
            return (int) Math.round((double)ins.getStatConfig().get(p.getUniqueId().toString()+".elo"));
        }
    }

    public int getElo(OfflinePlayer p){
        try {
            return (int) ins.getStatConfig().get(p.getUniqueId().toString()+".elo");
        }catch(NullPointerException e){
            ins.getStatConfig().set(p.getUniqueId().toString()+".elo", Settings.STARTING_ELO.getNumber());
            ins.saveStatConfig();
            return Settings.STARTING_ELO.getNumber();
        }catch(ClassCastException e){
            return (int) Math.round((double)ins.getStatConfig().get(p.getUniqueId().toString()+".elo"));
        }
    }

    public void setElo(Player p, double elo){
        ins.getStatConfig().set(p.getUniqueId().toString()+".elo",elo);
        ins.saveStatConfig();

    }

    public void setWins(Player p, int wins){
        ins.getStatConfig().set(p.getUniqueId().toString()+".wins",wins);
        ins.saveStatConfig();
    }

    public int getWIns(Player p) {

        try {
            return (int) ins.getStatConfig().get(p.getUniqueId().toString() + ".wins");
        }catch(NullPointerException e){
            ins.getStatConfig().set(p.getUniqueId().toString()+".wins", 0);
            ins.saveStatConfig();
            return 0;
        }
    }

    //assumed the one that comes first gains elo, second loses
    public void calculateElo(QueueEntry q1, QueueEntry q2){
    double winner,loser;
    winner = q1.getElo();
    loser = q2.getElo();

        double n = 1000;
        double k = (2*n)/24;
        double amy_e = 1/(1+(Math.pow(10,-((winner-loser)/n))));
        double brad_e = 1/(1+(Math.pow(10,-((loser-winner)/n))));
        winner = winner + (k*(1-amy_e));
        loser = loser + (k*(0-brad_e));
        if (winner < 0) winner = 0;
        if (loser < 0) loser = 0;
        setElo(q1.getPlayer(),winner);
        setElo(q2.getPlayer(),loser);
    }
    public void testElo(int amy, int brad){
        double a,b;
        Bukkit.broadcastMessage(String.format("Before:  %1$s   %2$s ",amy,brad));
        double n = 400;
        double k = 40;
        double amy_e = 1/(1+(Math.pow(10,-((amy-brad)/n))));
        double brad_e = 1/(1+(Math.pow(10,-((brad-amy)/n))));
        a = amy + (k*(1-amy_e));
        b = brad + (k*(0-brad_e));
        if (a < 0) a = 0;
        if (b < 0) b = 0;
        Bukkit.broadcastMessage(String.format("After:  %1$s   %2$s ",a,b));
        Bukkit.broadcastMessage("");
    }



    static float Probability(float rating1,
                             float rating2)
    {
        return 1.0f * 1.0f / (1 + 1.0f *
                (float)(Math.pow(10, 1.0f *
                        (rating1 - rating2) / 400)));
    }

}
