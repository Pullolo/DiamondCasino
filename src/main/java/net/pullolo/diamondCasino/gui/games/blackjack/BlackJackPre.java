package net.pullolo.diamondCasino.gui.games.blackjack;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import net.pullolo.diamondCasino.gui.base.BaseBackGui;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import static net.pullolo.diamondCasino.data.PlayerData.getPlayerData;
import static net.pullolo.diamondCasino.util.Utils.translate;

public class BlackJackPre extends BaseBackGui {
    private int currentBet = 0;
    private boolean takeMoney = false;
    public BlackJackPre(@NotNull Player player, Gui prevGui) {
        super(player, "g-1-pre", "Black Jack", 3, prevGui);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        fillGui(createFiller());

        addItem(4, createPlayerStats());
        addItem(18, createBackItem());

        if (getPlayerData(player).getDiamonds()>=1000000) addItem(8, bet(Material.GOLD_BLOCK, true, 1000000));
        if (getPlayerData(player).getDiamonds()>=100000) addItem(7, bet(Material.GOLD_BLOCK, true, 100000));
        if (getPlayerData(player).getDiamonds()>=1000) addItem(17, bet(Material.GOLD_BLOCK, true, 1000));
        addItem(16, bet(Material.GOLD_BLOCK, true, 100));
        addItem(15, bet(Material.GOLD_INGOT, true, 10));
        addItem(14, bet(Material.GOLD_NUGGET, true, 1));

        addItem(13, betDisplay());

        addItem(12, bet(Material.IRON_NUGGET, false, 1));
        addItem(11, bet(Material.IRON_INGOT, false, 10));
        addItem(10, bet(Material.IRON_BLOCK, false, 100));
        if (getPlayerData(player).getDiamonds()>=1000) addItem(9, bet(Material.IRON_BLOCK, false, 1000));
        if (getPlayerData(player).getDiamonds()>=100000) addItem(1, bet(Material.IRON_BLOCK, false, 100000));
        if (getPlayerData(player).getDiamonds()>=1000000) addItem(0, bet(Material.IRON_BLOCK, false, 1000000));

        addItem(22, play());
    }

    private Icon play(){
        Icon icon = new Icon(Material.COAL_BLOCK);
        icon.setName(translate(
                "&7> &cPLAY &7<"
        ));

        icon.onClick(click -> {
            if (currentBet<=0){
                owner.sendMessage(translate("&cBet has to be greater than 0!"));
                return;
            }

            takeMoney = true;
            this.getInventory().close();
            new BlackJack(owner, currentBet).open();
        });

        return icon;
    }

    private Icon betDisplay() {
        Icon icon = new Icon(Material.DIAMOND);
        icon.setName(translate(
                "&b✦ &7Current Bet - &b" + currentBet
        ));

        return icon;
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if (!takeMoney){
            getPlayerData(owner).setDiamonds(getPlayerData(owner).getDiamonds()+currentBet);
        }
    }

    private Icon bet(Material item, boolean additive, int amount){
        Icon icon = new Icon(item);
        icon.setName(translate(
                (additive ? "&a+" : "&c-") + " &7" + (additive ? "Add" : "Remove") + " &b" + amount + " &7" + (additive ? "to" : "from") + " bet"
        ));

        icon.onClick(click -> {
            boolean result = false;
            if (additive) result = add(amount);
            else result = subtract(amount);
            if (result) owner.playSound(owner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.8f);
            addItem(4, createPlayerStats());
            addItem(13, betDisplay());
        });

        return icon;
    }

    private boolean add(int amount){
        if (getPlayerData(owner).getDiamonds()>=amount){
            getPlayerData(owner).setDiamonds(getPlayerData(owner).getDiamonds()-amount);
            currentBet+=amount;
            return true;
        }
        return false;
    }

    private boolean subtract(int amount){
        if (currentBet>=amount){
            getPlayerData(owner).setDiamonds(getPlayerData(owner).getDiamonds()+amount);
            currentBet-=amount;
            return true;
        }
        return false;
    }
}
