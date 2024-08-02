package net.pullolo.diamondCasino.events;

import net.kyori.adventure.text.Component;
import net.pullolo.diamondCasino.data.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static net.pullolo.diamondCasino.data.PlayerData.*;

public class DataEventsHandler implements Listener {
    private final Database database;
    public DataEventsHandler(Database database) {
        this.database = database;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if (event.getPlayer().getName().contains(" ")){
            event.getPlayer().kick(Component.text("Invalid Name!"));
        }
        setPlayerDataFromDb(event.getPlayer(), database);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        savePlayerDataToDb(event.getPlayer(), database);
        removePlayerData(event.getPlayer());
    }
}
