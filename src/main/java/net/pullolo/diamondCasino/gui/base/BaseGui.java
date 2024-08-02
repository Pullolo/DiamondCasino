package net.pullolo.diamondCasino.gui.base;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static net.pullolo.diamondCasino.data.PlayerData.getPlayerData;
import static net.pullolo.diamondCasino.util.Utils.getPlayerSkull;
import static net.pullolo.diamondCasino.util.Utils.translate;

public class BaseGui extends Gui {
    protected final Player owner;

    public BaseGui(@NotNull Player player, @NotNull String id, String title, int rows) {
        super(player, id, title, rows);
        this.owner = player;
    }

    protected ItemStack createFiller(){
        ItemStack i = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = i.getItemMeta();
        meta.itemName(Component.text(""));
        i.setItemMeta(meta);
        return i;
    }

    protected Icon createPlayerStats(){
        Icon icon = new Icon(getPlayerSkull(owner));
        icon.setName(translate(
                "&fWelcome &a" + owner.getName() + "&f, to the Casino."
        ));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(translate(
                "&b✦ &7Diamonds - &b" + getPlayerData(owner).getDiamonds()
        ));
        icon.setLore(lore);

        return icon;
    }
}
