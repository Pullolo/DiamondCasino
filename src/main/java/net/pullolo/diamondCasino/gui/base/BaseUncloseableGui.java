package net.pullolo.diamondCasino.gui.base;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import static net.pullolo.diamondCasino.DiamondCasino.casinoPlugin;

public class BaseUncloseableGui extends BaseGui{
    public BaseUncloseableGui(@NotNull Player player, @NotNull String id, String title, int rows) {
        super(player, id, title, rows);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        super.onClose(event);
        if (event.getReason().equals(InventoryCloseEvent.Reason.PLAYER) ||
                event.getReason().equals(InventoryCloseEvent.Reason.TELEPORT) ||
                event.getReason().equals(InventoryCloseEvent.Reason.DEATH)
        ){
            Bukkit.getScheduler().runTask(casinoPlugin, this::open);
        }
    }

    private void preventClose(){
        this.open();
    }
}
