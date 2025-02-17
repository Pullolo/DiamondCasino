package net.pullolo.diamondCasino.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerData {
    private static final HashMap<Player, PlayerData> playerData = new HashMap<>();
    private final String name;
    private int diamonds;

    public PlayerData(String name, int diamonds) {
        this.name = name;
        this.diamonds = diamonds;
    }
    public static PlayerData getPlayerData(Player p){
        return playerData.get(p);
    }
    public static boolean containsPlayer(Player p) {return playerData.containsKey(p);}
    public static void setPlayerDataFromDb(Player p, Database db){
        playerData.put(p, db.getPlayerData(p));
    }
    public static void savePlayerDataToDb(Player p, Database db){
        db.updatePlayer(p, playerData.get(p));
    }
    public static void removePlayerData(Player p){
        playerData.remove(p);
    }
    public String getName() {
        return name;
    }

    public int getDiamonds() {
        return diamonds;
    }
    public void setDiamonds(int diamonds){
        this.diamonds = diamonds;
    }
}
