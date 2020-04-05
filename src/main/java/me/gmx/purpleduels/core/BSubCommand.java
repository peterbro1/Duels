package me.gmx.purpleduels.core;


import me.gmx.purpleduels.PurpleDuels;
import me.gmx.purpleduels.config.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public abstract class BSubCommand
{
    public PurpleDuels main;
    public List<String> aliases;
    public CommandSender sender;
    public String[] args;
    public Player player;
    public String correctUsage = "";
    public String permission = "";

    public boolean senderMustBePlayer;
    public BSubCommand() {
        this.main = PurpleDuels.getInstance();
        this.aliases = new ArrayList<String>();
    }

    @SuppressWarnings("unchecked")
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            this.player = (Player)sender;
        } else {
            this.player = null;
        }

        this.sender = sender;



        @SuppressWarnings("rawtypes")
        LinkedList linkedList = new LinkedList(Arrays.asList(args));
        linkedList.remove(0);
        this.args = (String[])linkedList.toArray(new String[linkedList.size()]);

        if (!sender.hasPermission(this.permission)) {
            msg(Lang.MSG_NOACCESS.toMsg());
            return;
        }
        if (this.senderMustBePlayer && !isPlayer()) {
            msg(Lang.MSG_PLAYERONLY.toMsg());
            return;
        }

        execute();
    }


    public abstract void execute();
    public void msg(String paramString) { this.sender.sendMessage(ChatColor.translateAlternateColorCodes('&',paramString)); }

    public void sendCorrectUsage() {
        msg(Lang.MSG_USAGE_SUBCOMMAND.toMsg().replace("%usage%", this.correctUsage));
    }

    public boolean isPlayer() { return (this.player != null); }

    public String buildStringFromArgs(int paramInt1, int paramInt2) {
        String str;
        if (paramInt2 < 0) {
            paramInt2 = 0;
        }
        if (this.args.length > paramInt1 + 1) {
            StringBuilder stringBuilder = new StringBuilder(this.args[paramInt1]);
            for (int i = paramInt1 + 1; i < paramInt2 + 1; i++) {
                stringBuilder.append(" " + this.args[i]);
            }
            str = stringBuilder.toString();
        } else {
            str = this.args[paramInt1];
        }
        return str;
    }

    public String str(Object paramObject) {
        return String.valueOf(paramObject);
    }

    public String getSenderName() {
        if (isPlayer()) {
            return this.player.getName();
        }
        return Lang.LANG_CONSOLE.toString();
    }
}





