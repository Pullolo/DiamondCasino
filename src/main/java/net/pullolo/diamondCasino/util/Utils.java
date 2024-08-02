package net.pullolo.diamondCasino.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Utils {
    public static String upperFirstLetter(String s){
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    public static String prettify(String s){
        s = s.toLowerCase().replaceAll("_", " ");
        String a = "";
        for (String str : s.split(" ")){
            a += upperFirstLetter(str) + " ";
        }
        return a.substring(0, a.length()-1);
    }

    public static String translate(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static ItemStack getPlayerSkull(Player p){
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) skull.getItemMeta();
        sm.setOwningPlayer(p);
        skull.setItemMeta(sm);
        return skull;
    }
}
