package me.gmx.purpleduels.objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlockStorage {

    private Location location;
    private BlockState data;
    public BlockStorage(Location location, BlockState data){
        this.location = location;
        this.data = data;
    }


    public void restore(){
        location.getBlock().setType(data.getType());

        location.getBlock().getState().setData(data.getData());
        data.update();
        location.getBlock().getState().update();


    }
    public Location getLocation(){
        return location;
    }

    public BlockState getState(){
        return data;
    }

}
