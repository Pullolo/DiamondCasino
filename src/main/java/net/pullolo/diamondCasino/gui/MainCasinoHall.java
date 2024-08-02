package net.pullolo.diamondCasino.gui;

import mc.obliviate.inventory.Icon;
import net.pullolo.diamondCasino.gui.base.BaseGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static net.pullolo.diamondCasino.data.PlayerData.getPlayerData;
import static net.pullolo.diamondCasino.util.Utils.translate;

public class MainCasinoHall extends BaseGui {

    public MainCasinoHall(@NotNull Player player) {
        super(player, "c-1", "Diamond Casino", 3);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        fillGui(createFiller());

        addItem(4, createPlayerStats());
        addItem(11, createPlay());
        addItem(15, createDiamonds());
    }

    private Icon createDiamonds() {
        Icon icon = new Icon(Material.DIAMOND);

        icon.setName(translate(
                "&bDiamond Bank"
        ));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(translate(
                "&b✦ &7Diamonds - &b" + getPlayerData(owner).getDiamonds()
        ));
        lore.add("");
        lore.add(translate(
                "&7Withdraw or deposit your diamonds"
        ));
        lore.add(translate(
                "&7to use them in the casino."
        ));

        icon.setLore(lore);

        icon.onClick(click -> {
            this.getInventory().close();
            new DiamondBank(owner, this).open();
        });

        return icon;
    }

    private Icon createPlay() {
        Icon icon = new Icon(Material.COMPASS);
        icon.setName(translate(
                "&cPlay Casino Games"
        ));

        icon.onClick(click -> {
            this.getInventory().close();
            new GamesMenu(owner, this).open();
        });

        return icon;
    }
}
