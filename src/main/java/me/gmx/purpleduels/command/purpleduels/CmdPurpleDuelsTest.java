package me.gmx.purpleduels.command.purpleduels;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.core.BSubCommand;
import me.gmx.purpleduels.handler.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CmdPurpleDuelsTest extends BSubCommand {

    public CmdPurpleDuelsTest(){
        //this.aliases.add("test");
        this.correctUsage = "/purpleduels test";
        this.permission = "purpleduels.test";
    }

    @Override
    public void execute() {
        PurpleDuels.getInstance().dataManager.testElo(100,100);
        PurpleDuels.getInstance().dataManager.testElo(1000,100);
        PurpleDuels.getInstance().dataManager.testElo(100,1000);
        PurpleDuels.getInstance().dataManager.testElo(1,100);
        PurpleDuels.getInstance().dataManager.testElo(100,1);
        PurpleDuels.getInstance().dataManager.testElo(500,350);
        PurpleDuels.getInstance().dataManager.testElo(350,500);


       /* for (String s : Lang.EXPLANATION.getStringList()){
            msg(Lang.PREFIX.toString() + s);
        }*/


    }
}
