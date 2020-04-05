package me.gmx.purpleduels.util;

import me.gmx.purpleduels.config.Lang;
import net.brcdev.gangs.gang.Gang;

import java.util.*;

public class MapUtils {
    public static LinkedHashMap<String, Integer> sortByValue(LinkedHashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        LinkedHashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }


    public static ArrayList<String> cleanMapText(LinkedHashMap<Gang,Integer> gangs,String format){
        ArrayList<String> s = new ArrayList<String>();
        //header
       // s.add(Lang.KOTH_RESULTS_HEADER.toString());

        for (Map.Entry<Gang,Integer> entry : gangs.entrySet()){
            try {
                s.add(format.replace("%gang%", entry.getKey().getName()).replace("%time%", ChatUtils.getStringFromNumberSecond(entry.getValue())));
                //s.add("\n");
            }catch(NullPointerException e){

            }
        }
        return s;
    }
}
