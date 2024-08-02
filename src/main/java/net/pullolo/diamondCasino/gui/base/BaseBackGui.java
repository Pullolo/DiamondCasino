package net.pullolo.diamondCasino.gui.base;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static net.pullolo.diamondCasino.util.Utils.translate;

public class BaseBackGui extends BaseGui{
    protected final Gui prevGui;

    public BaseBackGui(@NotNull Player player, @NotNull String id, String title, int rows, Gui prevGui) {
        super(player, id, title, rows);
        this.prevGui=prevGui;
    }

    protected Icon createBackItem(){
        Icon icon = new Icon(Material.ARROW);
        icon.setName(translate("&7< Back"));

        icon.onClick(click -> {
           this.getInventory().close();
           prevGui.open();
        });

        return icon;
    }
}
