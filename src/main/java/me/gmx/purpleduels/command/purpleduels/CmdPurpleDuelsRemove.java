package me.gmx.purpleduels.command.purpleduels;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.bukkit.listener.WorldGuardWeatherListener;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.core.BSubCommand;
import me.gmx.purpleduels.objects.Arena;
import me.gmx.purpleduels.util.FileUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.BlockVector;

public class CmdPurpleDuelsRemove extends BSubCommand {

    public CmdPurpleDuelsRemove(){
        this.aliases.add("remove");
        this.aliases.add("removearena");
        this.permission = "purpleduels.remove";
        this.correctUsage = "/duels remove [arena name]";

    }

    @Override
    public void execute(){
        if (this.args.length != 1){
            sendCorrectUsage();
            return;
        }
        try{
            Arena a = Arena.loadFromConfig(args[0]);
            FileUtils.removeArena(a);
            PurpleDuels.getInstance().duelManager.getArenas().remove(a);
            PurpleDuels.getInstance().duelManager.loadArenas();
            msg(Lang.ARENA_REMOVED.toMsg());
        }catch(Exception e){
            msg(Lang.ARENA_NOTFOUND.toMsg());
        }

    }
}
