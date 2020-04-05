package me.gmx.purpleduels.handler;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.config.Settings;
import me.gmx.purpleduels.objects.Arena;
import me.gmx.purpleduels.objects.Duel;
import me.gmx.purpleduels.objects.QueueEntry;
import me.gmx.purpleduels.util.InvenUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class DuelManager {
    public ArrayList<Player> inDuel;
    private PurpleDuels ins;
    private static LinkedList<QueueEntry> queue_omega;
    private List<Arena> arenaList;
    private List<Duel> duels;
    private static LinkedList<QueueEntry> queue_kitpvp;
    private HashMap<Player,AtomicInteger> combat;
    public HashMap<Player,Player> requests;
    public boolean active;
    private int timer;
    public DuelManager(PurpleDuels ins){
        this.ins = ins;
        arenaList = new ArrayList<Arena>();
        this.queue_omega = new LinkedList<QueueEntry>();
        this.queue_kitpvp = new LinkedList<QueueEntry>();
        this.duels = new ArrayList<Duel>();
        inDuel = new ArrayList<Player>();
        combat = new HashMap<Player, AtomicInteger>();
        requests = new HashMap<Player,Player>();
        active = false;
        timer = Settings.COMBAT_TAG_TIMER.getNumber();
    }



    public boolean removeRequest(Player p){
        if (requests.containsKey(p)){
            requests.remove(p);
            return true;
        }
        return false;
    }

    public void init(){
        loadArenas();
        if (getArenas().isEmpty()){
            return;
        }
        active = true;
        startTimer();
        startCombatChecker();

    }

    public void reload(){
        this.timer = Settings.COMBAT_TAG_TIMER.getNumber();
        loadArenas();
    }


    private void startCombatChecker(){
        new BukkitRunnable(){
            public void run(){

                combat.entrySet().removeIf(e -> e.getValue().decrementAndGet() < 0);

            }
        }.runTaskTimer(ins,20,20);
    }

    public void addCombat(Player p){
        combat.put(p,new AtomicInteger(timer));
    }



    public Duel getDuel(Player p) throws NullPointerException{
        if (inDuel.contains(p)){
            for (Duel d : duels){
                if (d.getPlayer1().getName().equals(p.getName()) || d.getPlayer2().getName().equals(p.getName())){
                    return d;
                }
            }
        }
        return null;
    }


    public void startTimer(){
        new BukkitRunnable(){
            public void run(){

                Collections.sort(queue_omega,new Comparator<QueueEntry>(){
                    public int compare(QueueEntry q1, QueueEntry q2){
                        if (q1.getElo()+q1.getFlex() > q2.getElo()+q2.getFlex()){
                            return 1;
                        }else if (q1.getElo()+q1.getFlex() < q2.getElo()+q2.getFlex()){
                            return -1;
                        }

                        return 0;
                    }
                });
                Collections.sort(queue_kitpvp,new Comparator<QueueEntry>(){
                    public int compare(QueueEntry q1, QueueEntry q2){
                        if (q1.getElo()+q1.getFlex() > q2.getElo()+q2.getFlex()){
                            return 1;
                        }else if (q1.getElo()+q1.getFlex() < q2.getElo()+q2.getFlex()){
                            return -1;
                        }

                        return 0;
                    }
                });

                for (QueueEntry e : queue_omega){
                    if (e.getPlayer() == null || combat.containsKey(e.getPlayer()))
                        queue_omega.remove(e);
                    else if (!InvenUtil.hasOmega(e.getPlayer())) {
                        e.getPlayer().sendMessage(Lang.MSG_REMOVED_QUEUE.toMsg());
                        queue_omega.remove(e);
                    }
                }
                for (QueueEntry e : queue_kitpvp){
                    if (e.getPlayer() == null || combat.containsKey(e.getPlayer()))
                        queue_kitpvp.remove(e);
                    else if (!InvenUtil.hasPvP(e.getPlayer())) {
                        e.getPlayer().sendMessage(Lang.MSG_REMOVED_QUEUE.toMsg());
                        queue_kitpvp.remove(e);
                    }
                }

                queueTrain(queue_kitpvp);
                queueTrain(queue_omega);
            }
        }.runTaskTimer(ins,60,60);
    }

    private void queueTrain(LinkedList<QueueEntry> queue){
        for (QueueEntry looker : queue){
            if (looker.getSecondsFromEntry() % 2 == 0)
            looker.getPlayer().sendMessage(Lang.MSG_QUEUE_WAIT.toMsg());
           /* Bukkit.broadcastMessage(looker.getPlayer().getName()+"'s queue: ");
            Bukkit.broadcastMessage("Elo: " + looker.getElo());
            Bukkit.broadcastMessage("Flex: " + looker.getFlex());
            Bukkit.broadcastMessage("Time: " + looker.getSecondsFromEntry());
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("");*/
          /*  System.out.println("---duels debug---");
           for (Duel d : duels){
               System.out.println(d.getArena().getName() + ": arena");
               System.out.println(d.getPlayer1().getName() + ": player1");
               System.out.println(d.getPlayer2().getName() + ": player2");
           }
           for (Player p : inDuel){
               System.out.println("Player in duel: " + p.getName());
           }
            System.out.println("---duels debug---");
*/
            for (QueueEntry match : queue){
                if (looker != match)
                if (looker.suitable(match)){
                        if (!getUnoccupiedArenas().isEmpty()) {
                            tryStartDuel(looker, match);
                        }else{
                            looker.getPlayer().sendMessage(Lang.MSG_MATCH_FOUND.toMsg());
                            match.getPlayer().sendMessage(Lang.MSG_MATCH_FOUND.toMsg());
                        }

                }
            }
        }
    }

    public void removeDuel(UUID id){
        for (Iterator<Duel> it = duels.iterator(); it.hasNext();) {
           if (id.equals(it.next().getUUID())) it.remove();
        }
    }

    public List<Arena> getArenas(){return this.arenaList;}


    public String tryStartDuel(QueueEntry q1, QueueEntry q2){
        if (getUnoccupiedArenas().size() > 0){
            Random r = new Random();
            Duel d = new Duel(q1,q2,getUnoccupiedArenas().get(r.nextInt(getUnoccupiedArenas().size())));
            if (!q1.getPlayer().isOnline() || !q2.getPlayer().isOnline())
                return Lang.MSG_PLAYER_OFFLINE.toMsg();



            duels.add(d);
            removeFromQueue(q1.getPlayer());
            removeFromQueue(q2.getPlayer());
            inDuel.add(q1.getPlayer());
            inDuel.add(q2.getPlayer());
            d.start();

            return Lang.MSG_DUEL_START.toMsg();
        }else return Lang.MSG_ARENA_FULL.toMsg();
    }

    public String tryStartFriendlyDuel(Player p1, Player p2){
        if (getUnoccupiedArenas().size() > 0){

            if (!p1.isOnline() || !p2.isOnline()){
                if (requests.containsKey(p1))
                    requests.remove(p1);
                if (requests.containsKey(p2))
                    requests.remove(p2);
                return (Lang.MSG_PLAYER_OFFLINE.toMsg());
            }

            if (combat.containsKey(p1) || combat.containsKey(p2)){
                return (Lang.MSG_CANNOT_START_IN_COMBAT.toMsg());
            }
            Random r = new Random();
            Duel d = new Duel(p1,p2,getUnoccupiedArenas().get(r.nextInt(getUnoccupiedArenas().size())));

            duels.add(d);
            inDuel.add(p1);
            inDuel.add(p2);
            d.start();
            if (requests.containsKey(p1))
                requests.remove(p1);
            if (requests.containsKey(p2))
                requests.remove(p2);

            return Lang.MSG_DUEL_START.toMsg();
        }
        return Lang.MSG_ARENA_FULL.toMsg();
    }


    public boolean addToQueue(QueueEntry p){
        if (!isInQueue(p.getPlayer()) && !inDuel.contains(p.getPlayer()) && !combat.containsKey(p.getPlayer())){
            switch(p.getType()){
                case OMEGA:
                    queue_omega.addLast(p);
                    return true;
                case KITPVP:
                    queue_kitpvp.addLast(p);
                    return true;
            }
        }
        return false;
    }


    public List<Arena> getUnoccupiedArenas(){
        List<Arena> empty = new ArrayList<Arena>(getArenas());
        for (Duel d : duels)
            if (empty.contains(d.getArena()))
                empty.remove(d.getArena());


         return empty;

    }

    public void removeFromQueue(QueueEntry p){
        if (isInQueue(p)){
            switch(p.getType()){
                case OMEGA:
                    queue_omega.remove(p);
                    break;
                case KITPVP:
                    queue_kitpvp.remove(p);
                    break;
            }
        }
    }

    public void removeFromQueue(Player p){
        if (!isInQueue(p)){
            return;
        }
        removeFromOmega(p);
        removeFromKitpvp(p);

    }
    private void removeFromKitpvp(Player p){
        if (!queue_kitpvp.isEmpty())
        for (int i = queue_kitpvp.size() - 1; i >= 0; i--){
            if (queue_kitpvp.get(i).getPlayer().getName().equals(p.getName())) {
                queue_kitpvp.remove(i);
            }
        }


    }

    private void removeFromOmega(Player p){
        if (!queue_omega.isEmpty())
            for (int i = queue_omega.size() - 1; i >= 0; i--){
                if (queue_omega.get(i).getPlayer().getName().equals(p.getName())) {
                    queue_omega.remove(i);
                }
            }
    }

    public static boolean isInQueue(QueueEntry p){
        return (queue_omega.contains(p) || queue_kitpvp.contains(p));
    }
    public static boolean isInQueue(Player p){
        for(QueueEntry e : queue_omega){
            if (e.getPlayer().getUniqueId().equals(p.getUniqueId())){
                return true;
            }
        }
        for(QueueEntry e : queue_kitpvp){
            if (e.getPlayer().getUniqueId().equals(p.getUniqueId())){
                return true;
            }
        }
        return false;
    }

    public void loadArenas(){
        arenaList = new ArrayList<Arena>();
        FileConfiguration config = ins.getArenaConfig();
        List<String>  ar = config.getStringList("arenas");
        for (String s : PurpleDuels.getInstance().getArenaConfig().getKeys(false)){
            try {
                loadArena(s);
            }catch(Exception e){
                ins.getLogger().log(Level.WARNING,"Failed to load arena " + s);
                e.printStackTrace();
            }
        }
    }

    public void loadArena(String id){
        try{
            getArenas().add(Arena.loadFromConfig(id));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean inCombat(Player player) {
        return combat.containsKey(player);
    }
}
