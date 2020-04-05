package me.gmx.purpleduels.config;

import me.gmx.purpleduels.core.BConfig;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public enum Lang {

    PREFIX("&6[&5Purple Duels&6]&r "),
    MSG_ERROR("Error occured, please contact server developer."),
    MSG_PKOTH_USAGE("&4Incorrect usage! See /duels help."),
    MSG_USAGE_SUBCOMMAND("Incorrect usage. Correct usage is %usage%"),
    MSG_FRIENDLYDUEL_CLICK("&5To send a friendly duel request, type /friendlyduel [player]"),
    MSG_PKOTH_HELP("&2Insert generic help here."),
    MSG_QUEUE_JOIN("&2You have joined the queue!"),
    MSG_REMOVED_QUEUE("&4You changed items in your inventory and have been removed from the queue."),
    TEXT_DUEL_MENU("Duel"),
    ARENA_REMOVED("&aArena successfully removed"),
    MSG_DUEL_ACCEPT_IN_QUEUE("&4You cannot accept a duel request while in queue."),
    ARENA_NOTFOUND("&4Arena not found!"),
    MSG_FRIENDLY_REQUEST_IN_QUEUE("&4The player is currently in queue. Please ask them to leave before sending another."),
    MSG_NOACCESS("You don't have access to this command."),
    HOVER_MESSAGE("&4Hover for results!"),
    MSG_CANNOT_START_IN_COMBAT("&4Cannot start a duel while one of the players is in combat!"),
    MSG_REJOIN_QUEUE("&4You have left the queue and joined another"),
    MSG_CMD_DISABLE("&4You cannot use commands while in a duel!"),
    MSG_INVALID_ITEMS("One of the parties in this duel did not meet the item requirements! Cancelling duel.."),
    MSG_FRIENDLYDUEL_DENIED("&5%player% &2has denied your request!"),
    MSG_HEAD_DROP("&6&lHeads &4&lup, your opponent's &6&lhead &4&ldropped!"),
    MSG_TARGET_IN_DUEL("&4Target is in a duel at the moment"),
    LORE_HEAD_DROP("&4%winner% &2defeated &4%loser% &2in &4&l%seconds% seconds &2with &4&l%hearts% &2hearts left!"),
    MSG_ARENA_FULL("Match found! Waiting for an arena to free up.."),
    MSG_FRIENDLYDUEL_DENIER("&2You have denied &5%player%&2's request!"),
    MSG_PLAYERONLY("This command can be used only in the game."),
    MSG_FRIENDLYDUEL_ALREADY("&4This player already has a pending request!"),
    MSG_PLAYER_OFFLINE("&4The other player is no longer online. Cancelling duel.."),
    MSG_KITPVP_PLS("&4Make sure that you have nothing more powerful in your inventory than /kit pvp"),
    MSG_OMEGA_PLS("&4Make sure you have an omega axe in your inventory!"),
    MSG_CONFIGRELOADED("Config reloaded."),
    LANG_CONSOLE("The console cannot perform this action."),
    ARENA_CREATE("Dueling arena successfully created!"),
    MSG_NEED_PVP("&4You must not have anything more powerful than kit pvp!"),
    MSG_FRIENDLYDUEL_USAGE("&2To start a friendly duel, type /friendlyduel [player]"),
    MSG_QUEUE_WAIT("&2You're currently in queue. Searching for match...."),
    MSG_NEED_OMEGA("You need to have an omega!"),
    MSG_FRIENDLYDUEL_REQUEST("&5%player% &2has requested to duel you. Type /duel accept to accept"),
    MSG_PLAYER_NULL("&4Cannot find player!"),
    MSG_NO_ARENAS("&4All arenas are currently being used!"),
    MSG_DUEL_START("&4Good luck!"),
    MSG_LEAVE_QUEUE("&4You must leave the queue or cancel your current request to initiate a friendly duel"),
    MSG_MATCH_FOUND("&3Match found! Searching for arena..."),
    TEXT_FRIENDLYGAME_TITLE("Join Friendly Game"),
    MSG_FRIENDLY_SENT("&7Request sent."),
    TEXT_FRIENDLYGAME_LORE("Friendly/Game/Stuff"),
    TEXT_RANKEDQUEUE_TITLE("Join Ranked Queue"),
    TEXT_RANKEDQUEUE_LORE("Ranked/Queue/Stuff"),
    TEXT_OMEGA_TITLE("&7Omega axe fight"),
    TEXT_OMEGA_LORE("There's only 1 rule: Bring your own omega axe."),
    TEXT_PVP_TITLE("&2Kit pvp fight"),
    MSG_LEAVE_DUEL("&4You have left the duel."),
    MSG_NOT_IN_DUEL("&4&lYou are not currently in a duel."),
    TEXT_PVP_LORE("&5Nothing higher than kitpvp!"),
    MSG_DEFEAT_BROADCAST("&4%winner% &2defeated &4%loser%!"),
    MSG_TOP_ELO_HEADER("&2==========&3Top Elo&2=========="),
    MSG_TOP_ELO("&c[%number%] &4%player%: &2%elo%"),
    TEXT_STATS_TITLE("Stats for %player%"),
    TEXT_STATS_LORE("Elo: %slash%%elo%/Wins: %wins%");



    private String defaultValue;
    private static BConfig config;

    Lang(String str){
        defaultValue = str;
    }


    public String getPath() { return name(); }

    public String getDefaultValue() { return this.defaultValue; }

    public String toString() { return fixColors(config.getConfig().getString(getPath())); }

    public static void setConfig(BConfig paramBConfig) {
        config = paramBConfig;
        load();
    }

    public List<String> getStringList(){
        if (toString().contains("/")) {
            List<String> a = Arrays.asList(fixColors(toString()).split("/"));
            List<String> b = new ArrayList<String>();
            for (String s : a)
                b.add(s.replace("\\", "/"));
            return b;

        }else{
            ArrayList<String> ss = new ArrayList<String>();
            ss.add(fixColors(toString()));
            return ss;
        }
    }

    public String toMsg() {
        boolean bool = true;
        if (bool) {
            return fixColors(config.getConfig().getString(PREFIX.getPath()) + config.getConfig()
                    .getString(getPath()));
        }
        return fixColors(config.getConfig().getString(getPath()));
    }

    public void set( String o){
        config.getConfig().set(getPath(),o);
    }

    private static void load() {
        for (Lang lang : values()) {
            if (config.getConfig().getString(lang.getPath()) == null) {
                config.getConfig().set(lang.getPath(), lang.getDefaultValue());
            }
        }
        config.save();
    }


    public static String fixColors(String paramString) {
        if (paramString == null)
            return "";
        return ChatColor.translateAlternateColorCodes('&', "&r"+paramString);
    }
}
