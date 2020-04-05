package me.gmx.purpleduels.command.purpleduels;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.core.BSubCommand;
import me.gmx.purpleduels.objects.Duel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.entity.EntityDamageEvent;

public class CmdPurpleDuelsLeave extends BSubCommand {

    public CmdPurpleDuelsLeave(){
        this.aliases.add("leave");
        this.aliases.add("leaveduel");
        this.correctUsage = "/purpleduels leave";
        this.permission = "purpleduels.leave";
    }

    @Override
    public void execute() {
        if (PurpleDuels.getInstance().duelManager.inDuel.contains(player)){
            Duel d = PurpleDuels.getInstance().duelManager.getDuel(player);
            player.damage(Integer.MAX_VALUE, d.getPlayer2());
            msg(Lang.MSG_LEAVE_DUEL.toMsg());
        }else{
            msg(Lang.MSG_NOT_IN_DUEL.toMsg());
            return;
        }

       /* for (String s : Lang.EXPLANATION.getStringList()){
            msg(Lang.PREFIX.toString() + s);
        }*/


    }
}
