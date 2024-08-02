package net.pullolo.diamondCasino.gui;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import net.pullolo.diamondCasino.gui.base.BaseBackGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import static net.pullolo.diamondCasino.util.Utils.translate;

public class DiamondBank extends BaseBackGui {
    public DiamondBank(@NotNull Player player, Gui prevGui) {
        super(player, "c-3", "Diamond Bank", 3, prevGui);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        fillGui(createFiller());

        addItem(4, createPlayerStats());
        addItem(11, createDeposit());
        addItem(15, createWithdrawal());

        addItem(18, createBackItem());
    }
    
    private Icon createDeposit(){
        Icon icon = new Icon(Material.DIAMOND_BLOCK);
        icon.setName(translate(
                "&a+ &7Deposit &bDiamonds"
        ));

        icon.onClick(click -> {
            this.getInventory().close();
            new Exchange(owner, this, true).open();
        });

        return icon;
    }

    private Icon createWithdrawal(){
        Icon icon = new Icon(Material.DIAMOND);
        icon.setName(translate(
                "&c- &7Withdraw &bDiamonds"
        ));

        icon.onClick(click -> {
            this.getInventory().close();
            new Exchange(owner, this, false).open();
        });

        return icon;
    }
}
