package me.gmx.purpleduels.command.purpleduels;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.core.BSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CmdPurpleDuelsHelp extends BSubCommand {

    public CmdPurpleDuelsHelp(){
        this.aliases.add("help");
        this.correctUsage = "/purpleduels help";
        this.permission = "purpleduels.help";
    }

    @Override
    public void execute() {
        msg(Lang.PREFIX.toString() + "&6&l----Purple Duels Commands----");
        msg(Lang.PREFIX.toString() + "&6/duels " + "&9- Open dueling menu.");
        msg(Lang.PREFIX.toString() + "&6/duels create [arena name] [region name] &9- Registers dueling arena.");
        msg(Lang.PREFIX.toString() + "&6/duels accept &9- Accepts incoming friendly duel request.");
        msg(Lang.PREFIX.toString() + "&6/duels cancel &9- Removes yourself from queue or cancels current outgoing request.");
        msg(Lang.PREFIX.toString() + "&6/duels list &9- Lists all registered dueling arenas.");
        msg(Lang.PREFIX.toString() + "&6/duels reload &9- Reloads config.");
        msg(Lang.PREFIX.toString() + "&6/duels deny &9- Denies all incoming friendly duel requests.");
        msg(Lang.PREFIX.toString() + "&6/duels remove [arena name] &9- Removes dueling arena.");
        msg(Lang.PREFIX.toString() + "&6/duels top &9- Displays ELO scoreboard.");
        msg(Lang.PREFIX.toString() + "&6/duels leave &9- Leaves current duel. WARNING: Kills you");
        msg(Lang.PREFIX.toString() + "&6/friendlyduel [player] &9- Sends a friendly duel request.");

        msg(Lang.PREFIX.toString() + "&4&lVersion: " + ChatColor.AQUA + PurpleDuels.getInstance().getDescription().getVersion());

       /* for (String s : Lang.EXPLANATION.getStringList()){
            msg(Lang.PREFIX.toString() + s);
        }*/


    }
}
