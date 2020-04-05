package me.gmx.purpleduels.command;

import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.command.purpleduels.*;
import me.gmx.purpleduels.config.Lang;
import me.gmx.purpleduels.core.BCommand;
import me.gmx.purpleduels.core.BSubCommand;
import me.gmx.purpleduels.handler.DuelManager;
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


public class CmdFriendlyDuel extends BCommand implements CommandExecutor {

    public CmdFriendlyDuel(PurpleDuels instance) {
        super(instance);


    }




    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        if (arg3.length != 1) {
            if (!arg0.hasPermission("purpleduels.friendlyduel")) {
                arg0.sendMessage(Lang.MSG_NOACCESS.toMsg());
                return true;
            }
            arg0.sendMessage(Lang.MSG_FRIENDLYDUEL_USAGE.toMsg());
            return true;
        }
        if (PurpleDuels.getInstance().duelManager.inDuel.contains((Player)arg0) || DuelManager.isInQueue((Player)arg0) || PurpleDuels.getInstance().duelManager.requests.containsKey((Player)arg0)){
            arg0.sendMessage(Lang.MSG_LEAVE_QUEUE.toMsg());
            return true;
        }
        if (Bukkit.getPlayer(arg3[0]) == null){
            arg0.sendMessage(Lang.MSG_PLAYER_NULL.toMsg());
            return true;
        }else if (Bukkit.getPlayer(arg3[0]) == (Player)arg0) {
            Lang.MSG_PLAYER_NULL.toMsg();
            return true;
        }
        if (PurpleDuels.getInstance().duelManager.requests.containsValue(Bukkit.getPlayer(arg3[0]))){
            arg0.sendMessage(Lang.MSG_FRIENDLYDUEL_ALREADY.toMsg());
            return true;
        }
        if (PurpleDuels.getInstance().duelManager.isInQueue(Bukkit.getPlayer(arg3[0]))){
            arg0.sendMessage(Lang.MSG_FRIENDLY_REQUEST_IN_QUEUE.toMsg());
            return true;
        }
        if (PurpleDuels.getInstance().duelManager.inDuel.contains(Bukkit.getPlayer(arg3[0]))) {
            arg0.sendMessage(Lang.MSG_TARGET_IN_DUEL.toMsg());
            return true;
        }

        if (PurpleDuels.getInstance().duelManager.inCombat(Bukkit.getPlayer(arg3[0]))){
            arg0.sendMessage(Lang.MSG_CANNOT_START_IN_COMBAT.toMsg());
        }
        PurpleDuels.getInstance().duelManager.requests.put((Player)arg0,Bukkit.getPlayer(arg3[0]));
        arg0.sendMessage(Lang.MSG_FRIENDLY_SENT.toMsg());
        Bukkit.getPlayer(arg3[0]).sendMessage(Lang.MSG_FRIENDLYDUEL_REQUEST.toMsg().replace("%player%",arg0.getName()));

        return true;
    }

}
