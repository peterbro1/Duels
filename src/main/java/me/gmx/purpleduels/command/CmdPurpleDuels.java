package me.gmx.purpleduels.command;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.command.purpleduels.*;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.core.BCommand;
import me.gmx.purpleduels.core.BSubCommand;
import me.gmx.purpleduels.handler.GuiManager;
import me.gmx.purpleduels.objects.GuiButton;
import me.gmx.purpleduels.objects.GuiMenu;
import me.gmx.purpleduels.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.*;


public class CmdPurpleDuels extends BCommand implements CommandExecutor {

    public CmdPurpleDuels(PurpleDuels instance) {
        super(instance);
        this.subcommands.add(new CmdPurpleDuelsCreate());
        this.subcommands.add(new CmdPurpleDuelsHelp());
//        this.subcommands.add(new CmdPurpleDuelsReload());
//        this.subcommands.add(new CmdPurpleDuelsRemove());
//        this.subcommands.add(new CmdPurpleDuelsStopAll());
//        this.subcommands.add(new CmdPurpleDuelsStart());
        this.subcommands.add(new CmdPurpleDuelsList());
        this.subcommands.add(new CmdPurpleDuelsCancel());
        this.subcommands.add(new CmdPurpleDuelsAccept());
        this.subcommands.add(new CmdPurpleDuelsRemove());
        this.subcommands.add(new CmdPurpleDuelsReload());
        this.subcommands.add(new CmdPurpleDuelsDeny());
        this.subcommands.add(new CmdPurpleDuelsTop());
        this.subcommands.add(new CmdPurpleDuelsTest());
        this.subcommands.add(new CmdPurpleDuelsLeave());
//        this.subcommands.add(new CmdPurpleDuelsReset());
//        this.subcommands.add(new CmdPurpleDuelsSetTimer());
    }




    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        if (arg3.length < 1) {
            if (!arg0.hasPermission("purpleduels.duel")) {
                arg0.sendMessage(Lang.MSG_NOACCESS.toMsg());
                return true;
            }
            PurpleDuels.getInstance().gui.open((Player)arg0, GuiManager.GuiList.DUEL_FIRST);
            if (!PurpleDuels.getInstance().duelManager.active){
                PurpleDuels.getInstance().duelManager.init();
            }
            return true;

        }

        for (BSubCommand cmd : this.subcommands) {
            if (cmd.aliases.contains(arg3[0])) {
                cmd.execute(arg0,arg3);
                return true;
            }
        }
        arg0.sendMessage(Lang.MSG_PKOTH_USAGE.toMsg());

        return true;
    }

}
