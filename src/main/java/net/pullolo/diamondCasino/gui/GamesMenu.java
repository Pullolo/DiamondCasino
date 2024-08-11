package net.pullolo.diamondCasino.gui;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import net.pullolo.diamondCasino.gui.base.BaseBackGui;
import net.pullolo.diamondCasino.gui.base.BaseGui;
import net.pullolo.diamondCasino.gui.games.blackjack.BlackJackPre;
import net.pullolo.diamondCasino.gui.games.dice.DicePre;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static net.pullolo.diamondCasino.util.Utils.prettify;
import static net.pullolo.diamondCasino.util.Utils.translate;

public class GamesMenu extends BaseBackGui {
    public GamesMenu(@NotNull Player player, Gui prevGui) {
        super(player, "c-2", "Choose a Game", 3, prevGui);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        fillGui(createFiller());

        addItem(4, createPlayerStats());

        addItem(11, createGame("Black Jack", getBlackJackDescription(), Material.COAL_BLOCK, new BlackJackPre(owner, this)));
        addItem(15, createGame("Dice", getDiceDescription(), Material.HEAVY_CORE, new DicePre(owner, this)));

        addItem(18, createBackItem());
    }

    private Icon createGame(String name, ArrayList<String> lore, Material item, Gui nextGui){
        Icon icon = new Icon(item);
        icon.setName(translate(
           "&7> &c" + prettify(name) + " &7<"
        ));
        icon.setLore(lore);

        icon.onClick(click -> {
            this.getInventory().close();
            nextGui.open();
        });

        return icon;
    }

    private ArrayList<String> getBlackJackDescription(){
        ArrayList<String> lore = new ArrayList<>();
        lore.add(translate(
                "&7Card game whose object is to be dealt"
        ));
        lore.add(translate(
                "&7cards having a higher count than those of"
        ));
        lore.add(translate(
                "&7the dealer, up to but not exceeding 21."
        ));
        return lore;
    }

    private ArrayList<String> getDiceDescription(){
        ArrayList<String> lore = new ArrayList<>();
        lore.add(translate(
                "&7Dice game whose object is to guess"
        ));
        lore.add(translate(
                "&7if the next roll is higher or lower."
        ));
        return lore;
    }
}
