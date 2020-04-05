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

public class CmdPurpleDuelsCreate extends BSubCommand {

    public CmdPurpleDuelsCreate(){
        this.aliases.add("createarena");
        this.aliases.add("addarena");
        this.aliases.add("create");
        this.permission = "purpleduels.create";
        this.correctUsage = "/purpleduels createarena [arena name] [worldguard region]";

    }

    @Override
    public void execute(){
        if (this.args.length != 2){
            sendCorrectUsage();
            return;
        }

        if (!PurpleDuels.getInstance().wgp.getRegionManager(player.getWorld()).hasRegion(args[1])){
            sender.sendMessage(Lang.PREFIX + "Region " + args[1] + " could not be found");
            return;
        }

        ProtectedRegion r = PurpleDuels.getInstance().wgp.getRegionManager(player.getWorld()).getRegion(args[1]);
        com.sk89q.worldedit.BlockVector bv = r.getMinimumPoint();
        Location bl, tr;

        bl = new Location(player.getWorld(),r.getMinimumPoint().getBlockX(),r.getMinimumPoint().getBlockY(),r.getMinimumPoint().getBlockZ());
        tr = new Location(player.getWorld(),r.getMaximumPoint().getBlockX(),r.getMaximumPoint().getBlockY(),r.getMaximumPoint().getBlockZ());

        Arena a = new Arena(args[0],bl,tr);
        FileUtils.writeArena(a);
        PurpleDuels.getInstance().duelManager.loadArenas();
        msg(Lang.ARENA_CREATE.toMsg());

    }
}
