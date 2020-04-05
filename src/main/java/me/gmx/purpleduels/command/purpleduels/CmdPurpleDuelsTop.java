package me.gmx.purpleduels.command.purpleduels;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.core.BSubCommand;
import me.gmx.purpleduels.objects.Arena;
import me.gmx.purpleduels.util.MapUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class CmdPurpleDuelsTop extends BSubCommand {

    public CmdPurpleDuelsTop(){
        this.aliases.add("top");
        this.aliases.add("elotop");
        this.permission = "purpleduels.top";
        this.correctUsage = "/purpleduels top";
        this.senderMustBePlayer = false;
    }
    @Override
    public void execute() {
        if (args.length != 0){
            sendCorrectUsage();
            return;
        }

        Map<String,Object> list = PurpleDuels.getInstance().getStatConfig().getValues(false);
        LinkedHashMap<String,Integer> map = new LinkedHashMap<String,Integer>();
        for (Map.Entry e : list.entrySet()){
            map.put(Bukkit.getOfflinePlayer(UUID.fromString((String)e.getKey())).getName()
            ,PurpleDuels.getInstance().dataManager.getElo(Bukkit.getOfflinePlayer(UUID.fromString((String)e.getKey()))));
        }
        HashMap<String,Integer> a = MapUtils.sortByValue(map);

        player.sendMessage(Lang.MSG_TOP_ELO_HEADER.toMsg());
        for(int i = 0;i<10;i++){
            Map.Entry<String,Integer> entry = (Map.Entry) a.entrySet().toArray()[i];
            player.sendMessage(Lang.MSG_TOP_ELO.toMsg()
                    .replace("%number%",String.valueOf(i+1))
                    .replace("%player%",entry.getKey())
                    .replace("%elo%",String.valueOf(entry.getValue())));
        }


    }
}
