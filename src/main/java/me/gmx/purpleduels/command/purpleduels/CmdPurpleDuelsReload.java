package me.gmx.purpleduels.command.purpleduels;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.core.BSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CmdPurpleDuelsReload extends BSubCommand {

    public CmdPurpleDuelsReload(){
        this.aliases.add("reload");
        this.correctUsage = "/purpleduels reload";
        this.permission = "purpleduels.reload";
    }

    @Override
    public void execute() {
        PurpleDuels.getInstance().reloadArenaConfig();
        PurpleDuels.getInstance().reloadConfig();
        PurpleDuels.getInstance().reloadstatConfig();
        msg(Lang.MSG_CONFIGRELOADED.toMsg());


    }
}
