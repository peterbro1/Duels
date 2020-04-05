package me.gmx.purpleduels.command.purpleduels;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.core.BSubCommand;
import me.gmx.purpleduels.objects.Arena;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;

public class CmdPurpleDuelsAccept extends BSubCommand {

    public CmdPurpleDuelsAccept(){
        this.aliases.add("accept");
        this.aliases.add("a");
        this.permission = "purpleduels.accept";
        this.correctUsage = "/purpleduels accept";
        this.senderMustBePlayer = true;
    }
    @Override
    public void execute() {
        if (args.length != 0){
            sendCorrectUsage();
            return;
        }

            for (Map.Entry<Player,Player> entry : new ArrayList<Map.Entry<Player,Player>>(PurpleDuels.getInstance().duelManager.requests.entrySet())){
                if (entry.getValue().equals(player)){
                    if (!PurpleDuels.getInstance().duelManager.inDuel.contains(player) && !PurpleDuels.getInstance().duelManager.isInQueue(player)) {

                        player.sendMessage(PurpleDuels.getInstance().duelManager.tryStartFriendlyDuel(entry.getKey(), entry.getValue()));
                    }else{
                        player.sendMessage(Lang.MSG_DUEL_ACCEPT_IN_QUEUE.toMsg());
                    }
                }
            }


    }
}
