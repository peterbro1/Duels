package me.gmx.purpleduels.objects;


import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditAPI;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.util.LocationUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import me.gmx.purpleduels.objects.BlockStorage;

import java.util.ArrayList;
import java.util.List;

public class Arena {

    private String name;
    private Location botLeft;
    private Location topRight;
    private World world;
    private List<BlockStorage> setEmeralds;



    public Arena(String name, Location botLeft, Location topRight){
        this.name = name;
        this.world = botLeft.getWorld();
        this.botLeft = botLeft;
        this.topRight = topRight;
        this.setEmeralds = new ArrayList<BlockStorage>();
        /*for (BlockStorage blst : getCorners()){
            setEmeralds.add(new BlockStorage(blst.getLocation().clone().subtract(0,1,0),blst.getLocation().clone().subtract(0,1,0).getBlock().getState()));
            for (BlockStorage b : LocationUtil.getSquareAround(blst.getLocation().clone().subtract(0,1,0),1)){
                setEmeralds.add(b);
            }
        }*/
    }

    public List<BlockStorage> getSetEmeralds(){
        return setEmeralds;
    }
    /*
     *@param b - Bottom left location
     * @param t - Top right location
     */
    public void setArea(Location b, Location t){
        this.botLeft = b;
        this.topRight = t;
    }

    public Location getBotLeft(){
        return this.botLeft;
    }
    public Location getTopRight(){
        return this.topRight;
    }

    private void debug(Location loc){
        if (botLeft.getBlockX() > loc.getBlockX()){Bukkit.broadcastMessage("move +X");}
        if (topRight.getBlockX() < loc.getBlockX()){Bukkit.broadcastMessage("move -X");}
        //if (botLeft.getBlockY() > loc.getBlockY()){}
        if (botLeft.getBlockZ() > loc.getBlockZ()){Bukkit.broadcastMessage("move +Z");}
        if ( topRight.getBlockZ() < loc.getBlockZ()){Bukkit.broadcastMessage("move -Z");}

        if (botLeft.getBlockY() > loc.getY()){Bukkit.broadcastMessage("move up"); Bukkit.broadcastMessage(String.valueOf(botLeft.getBlockY() + ">" + loc.getY()));}
        if (topRight.getBlockY() < loc.getY()){Bukkit.broadcastMessage("move down");Bukkit.broadcastMessage(String.valueOf(topRight.getBlockY() + "<" + loc.getY()));}

        Bukkit.broadcastMessage(String.format("topright: %1$s,%2$s,%3$s-- Botleft: %4$s,%5$s,%6$s",topRight.getX(),topRight.getY(),topRight.getZ(),botLeft.getX(), botLeft.getY(),botLeft.getZ()));
    }

    public boolean isInsideArena(Location loc){
        //debug(loc);
        return botLeft.getBlockX()-1 <= loc.getX() && topRight.getBlockX()+1 >= loc.getX() &&
                botLeft.getBlockY()-1 <= loc.getY() && topRight.getBlockY()+1 >= loc.getY() &&
                botLeft.getBlockZ()-1 <= loc.getZ() && topRight.getBlockZ()+1 >= loc.getZ();
    }

    public boolean isInArea(OfflinePlayer oPlayer) {
        if(oPlayer == null || !oPlayer.isOnline() || oPlayer.getPlayer() == null) {
            return false;
        }
        Player player = oPlayer.getPlayer();

        if (player.isDead()) {
            return false;
        }

        if (player.getWorld() != botLeft.getWorld()) {
            return false;
        }
        return isInsideArena(player.getLocation());
    }

    public static Arena loadFromConfig(String id)throws NullPointerException{
        if (id == null || id.isEmpty() || !me.gmx.purpleduels.util.FileUtils.sectionExists(id)){
            throw new NullPointerException("Could not find arena " + id + " in config!");
        }
        Location botLeft, topRight;
        Material blockType;
        int radius;
        try{
            ConfigurationSection sec = PurpleDuels.getInstance().getArenaConfig().getConfigurationSection(id);
            botLeft = new Location(Bukkit.getWorld(sec.getString("world")),sec.getInt("min-x"),sec.getInt("min-y"),sec.getInt("min-z"));
            topRight = new Location(Bukkit.getWorld(sec.getString("world")),sec.getInt("max-x"),sec.getInt("max-y"),sec.getInt("max-z"));

        }catch (Exception ex){
            throw new NullPointerException("Unable to grab locations from config!");
        }
        // BlockVector bl = new BlockVector(botLeft.getBlockX(),botLeft.getBlockY(),botLeft.getBlockZ());
        // BlockVector tr = new BlockVector(topRight.getBlockX(),topRight.getBlockY(),topRight.getBlockZ());
        return new Arena(id,botLeft,topRight);
    }
    public World getWorld(){
        return world;
    }

    public String getName(){
        return name;
    }

    public Location getMiddle() {
        Location t = LocationUtil.getMiddleBlock(getBotLeft(), getTopRight());
        return new Location(getWorld(),t.getX(),getBotLeft().getY(),t.getZ());
    }

    public void tick(Particle particle, Sound sound){
        getWorld().playSound(getMiddle(),sound,1,1);
        getWorld().spawnParticle(particle,getMiddle().add(0.5,2,0.5),2);
    }
    public void changeBorderType(Material type){
        for (Block b : LocationUtil.getBorders(getBotLeft(),getTopRight())){
            if (sameXY(b.getLocation(),getBotLeft()) || sameXY(b.getLocation(),getTopRight())
                    || (b.getLocation().getBlockX() == getTopRight().getBlockX() && b.getLocation().getBlockZ() == getBotLeft().getBlockZ())
                    || (b.getLocation().getBlockZ() == getTopRight().getBlockZ() && b.getLocation().getBlockX() == getBotLeft().getBlockX())){ //4 corners
                b.setType(Material.BEACON);
            }else
                b.setType(type);
        }
        for (BlockStorage blst : setEmeralds){
            blst.getLocation().getBlock().setType(type);
        }


    }
    public List<BlockStorage> getCorners(){
        List<BlockStorage> store = new ArrayList<BlockStorage>();
        for (Block b : LocationUtil.getBorders(getBotLeft(),getTopRight())){
            if (sameXY(b.getLocation(),getBotLeft()) || sameXY(b.getLocation(),getTopRight())
                    || (b.getLocation().getBlockX() == getTopRight().getBlockX() && b.getLocation().getBlockZ() == getBotLeft().getBlockZ())
                    || (b.getLocation().getBlockZ() == getTopRight().getBlockZ() && b.getLocation().getBlockX() == getBotLeft().getBlockX())){ //4 corners
                store.add(new BlockStorage(b.getLocation(),b.getState()));
            }
        }
        return store;
    }
    private boolean sameXY(Location loc1, Location loc2){
        if (
                loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockZ() == loc2.getBlockZ()
        )
            return true;
        else
            return false;
    }
}
