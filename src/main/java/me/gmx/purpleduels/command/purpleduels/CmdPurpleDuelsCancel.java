package me.gmx.purpleduels.command.purpleduels;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.core.BSubCommand;
import me.gmx.purpleduels.handler.DuelManager;
import me.gmx.purpleduels.objects.Arena;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;

public class CmdPurpleDuelsCancel extends BSubCommand {

    public CmdPurpleDuelsCancel(){
        this.aliases.add("cancel");
        this.aliases.add("c");
        this.permission = "purpleduels.cancel";
        this.correctUsage = "/purpleduels cancel";
        this.senderMustBePlayer = true;
    }
    @Override
    public void execute() {
        if (args.length != 0){
            sendCorrectUsage();
            return;
        }
        if (DuelManager.isInQueue(player) ){
            PurpleDuels.getInstance().duelManager.removeFromQueue(player);

            msg("&cYou have been successfully removed from the queue!");
            return;
        }else
        if(PurpleDuels.getInstance().duelManager.removeRequest(player)) {
            msg("&4&lYou have cancelled your current request.");
            return;
        }else{msg("&cYou must be in a queue or have an active request to cancel.");}

    }
}
