package me.gmx.purpleduels.command.purpleduels;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.core.BSubCommand;
import me.gmx.purpleduels.objects.Arena;
import org.bukkit.ChatColor;

public class CmdPurpleDuelsList extends BSubCommand {

    public CmdPurpleDuelsList(){
        this.aliases.add("list");
        this.aliases.add("listarenas");
        this.permission = "purplekoth.list";
        this.correctUsage = "/purplekoth list";
        this.senderMustBePlayer = false;
    }
    @Override
    public void execute() {
        if (args.length != 0){
            sendCorrectUsage();
            return;
        }
        if (PurpleDuels.getInstance().duelManager.getArenas().isEmpty()){
            msg(Lang.PREFIX.toString() + "&4There are currently no loaded arenas.");
            return;
        }
        msg(Lang.PREFIX.toString() + "&2Current Arenas:");
        for (Arena a : PurpleDuels.getInstance().duelManager.getArenas()){
            msg(Lang.PREFIX.toString() + ChatColor.GREEN + a.getName());
        }
    }
}
