package net.pullolo.diamondCasino.gui;

import mc.obliviate.inventory.Gui;
import net.pullolo.diamondCasino.gui.base.BaseBackGui;
import net.pullolo.diamondCasino.gui.base.BaseGui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

public class GamesMenu extends BaseBackGui {
    public GamesMenu(@NotNull Player player, Gui prevGui) {
        super(player, "c-2", "Choose a Game", 3, prevGui);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        fillGui(createFiller());

        addItem(4, createPlayerStats());
        addItem(18, createBackItem());
    }
}
