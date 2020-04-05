package me.gmx.purpleduels.util;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatUtils {

    public static TextComponent getHoverText(String text, List<String> hover){
            TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&',text));
            List<BaseComponent> bc = new ArrayList<BaseComponent>();

            for (String s : hover){
                bc.add(new TextComponent(ChatColor.translateAlternateColorCodes('&',s)));
                bc.add( new TextComponent("\n"));
            }
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, bc.toArray(new BaseComponent[bc.size()])));
            return message;

    }
    public static String getMinutesFromSeconds(int seconds){
        return seconds >= 60 ? String.valueOf((int)seconds/60) + " minutes" : String.valueOf(seconds) + " seconds";
    }

    public static List<Integer> getTimeFromString(String s){
        List<String> array = Arrays.asList(s.split("/"));
        List<Integer> num = new ArrayList<>();
        for (String str : array){
            if (str.contains("m")){
                int inte = Integer.parseInt(str.replace("m","")) * 60;
                num.add(inte);
            }else if (str.contains("s")){
                int inte = Integer.parseInt(str.replace("s",""));
                num.add(inte);
            }else if (str.contains("h")){
                int inte = Integer.parseInt(str.replace("h",""))* 60 * 60;
                num.add(inte);
            }
        }
        return num;
    }

    public static int getSecondsFromString (String s){
        if (s.contains("m")){
            return (Integer.parseInt(s.replace("m",""))) * 60;
        }else if (s.contains("s")){
            return (Integer.parseInt(s.replace("s","")));
        }else if (s.contains("h")){
            return (Integer.parseInt(s.replace("h",""))) * 3600;
        }else{
            return Integer.parseInt(s);
        }
    }

    public static int getSecondsFromStringMultiple(String st){
        int i = 0;
        String s = st;
        if (!s.toLowerCase().contains("h") && !s.toLowerCase().contains("m") && !s.toLowerCase().contains("s")){
            try{
                return Integer.parseInt(st);
            }catch(Exception e){
                return 20;
            }
        }
        try {
            if (s.toLowerCase().contains("h")) {
                i += (Integer.parseInt(s.substring(0, s.indexOf("h"))) * 60 * 60);
                s = s.split("h")[1];
            }
            if (s.toLowerCase().contains("m")) {
                i += (Integer.parseInt(s.substring(0, s.indexOf("m"))) * 60);
                s = s.split("m")[1];
            }
            if (s.toLowerCase().contains("s")) {
                i += (Integer.parseInt(s.substring(0, s.indexOf("s"))));
                //s=s.split("s")[1];
            }
        }catch(IndexOutOfBoundsException e){
            return i;
        }
        return i;
    }

    public static String getStringFromNumberSecond(long sec){
        long seconds = sec;
        StringBuilder str = new StringBuilder();
        int hour = 0;
        int minute = 0;
        int second = 0;
        while ((seconds / 60 / 60) > 0){
            hour++;
            seconds -= 3600;
        }
        while ((seconds / 60) > 0){
            minute++;
            seconds -= 60;
        }
        while (seconds > 0){
            second++;
            seconds--;
        }
        if (hour > 0){
            str.append(String.valueOf(hour));
            str.append(" ");
            str.append("Hours");
            str.append(" ");
        }
        if (minute > 0){
            str.append(String.valueOf(minute));
            str.append(" ");
            str.append("Minutes");
            str.append(" ");
        }
        if (second > 0){
            str.append(String.valueOf(second));
            str.append(" ");
            str.append("Seconds");
        }
        return str.toString();
    }

    public static String getEncodeFromSec(int sec){
        int seconds = sec;
        StringBuilder str = new StringBuilder();
        int hour = 0;
        int minute = 0;
        int second = 0;
        while ((seconds / 60 / 60) > 0){
            hour++;
            seconds -= 3600;
        }
        while ((seconds / 60) > 0){
            minute++;
            seconds -= 60;
        }
        while (seconds > 0){
            second++;
            seconds--;
        }
        if (hour > 0){
            str.append(String.valueOf(hour));
            str.append("h");

        }
        if (minute > 0){
            str.append(String.valueOf(minute));
            str.append("m");

        }
        if (second > 0){
            str.append(String.valueOf(second));
            str.append("s");

        }
        return str.toString();
    }
}
