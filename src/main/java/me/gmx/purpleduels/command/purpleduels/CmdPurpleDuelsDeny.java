package me.gmx.purpleduels.command.purpleduels;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.core.BSubCommand;
import me.gmx.purpleduels.objects.Arena;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;

public class CmdPurpleDuelsDeny extends BSubCommand {

    public CmdPurpleDuelsDeny(){
        this.aliases.add("deny");
        this.aliases.add("d");
        this.permission = "purpleduels.deny";
        this.correctUsage = "/purpleduels deny";
        this.senderMustBePlayer = true;
    }
    @Override
    public void execute() {
        if (args.length != 0){
            sendCorrectUsage();
            return;
        }

        for (Map.Entry<Player,Player> entry : PurpleDuels.getInstance().duelManager.requests.entrySet()){
            if (entry.getValue().equals(player)){
                PurpleDuels.getInstance().duelManager.requests.remove(entry);
                entry.getKey().sendMessage(Lang.MSG_FRIENDLYDUEL_DENIED.toMsg().replace("%player%",entry.getValue().getName()));
                player.sendMessage(Lang.MSG_FRIENDLYDUEL_DENIER.toMsg().replace("%player%",entry.getKey().getName()));
            }
        }


    }
}
