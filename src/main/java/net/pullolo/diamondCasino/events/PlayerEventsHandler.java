package net.pullolo.diamondCasino.events;


import net.pullolo.diamondCasino.data.PlayerData;
import net.pullolo.diamondCasino.items.DroppedDiamonds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static net.pullolo.diamondCasino.data.PlayerData.containsPlayer;
import static net.pullolo.diamondCasino.data.PlayerData.getPlayerData;

public class PlayerEventsHandler implements Listener {
    private final boolean keepDiamonds;
    public PlayerEventsHandler(boolean keepDiamonds){
        this.keepDiamonds = keepDiamonds;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if (keepDiamonds) return;
        Player p = event.getPlayer();
        if (!containsPlayer(p)) return;
        if (getPlayerData(p).getDiamonds()<1){
            return;
        }
        new DroppedDiamonds(p, getPlayerData(p).getDiamonds());
        getPlayerData(p).setDiamonds(0);
    }
}
