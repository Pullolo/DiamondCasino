package net.pullolo.diamondCasino.gui;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import net.pullolo.diamondCasino.gui.base.BaseBackGui;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static net.pullolo.diamondCasino.data.PlayerData.getPlayerData;
import static net.pullolo.diamondCasino.util.Utils.translate;

public class Exchange extends BaseBackGui {
    private final boolean deposit;

    public Exchange(@NotNull Player player, Gui prevGui, boolean deposit) {
        super(player, "c-4", deposit ? "Deposit" : "Withdrawal", 3, prevGui);
        this.deposit = deposit;
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        fillGui(createFiller());

        addItem(4, createPlayerStats());

        addItem(10, createExchange(1));
        addItem(11, createExchange(2));
        addItem(12, createExchange(4));
        addItem(13, createExchange(8));
        addItem(14, createExchange(16));
        addItem(15, createExchange(32));
        addItem(16, createExchange(64));

        addItem(18, createBackItem());
    }

    private Icon createExchange(int howMuch){
        Icon icon = new Icon(deposit ? Material.DIAMOND_BLOCK : Material.DIAMOND);
        icon.setName(translate(
                "&7" + (deposit ? "Deposit" : "Withdraw") + " &b" + howMuch + " &7diamonds"
        ));

        icon.onClick(click -> {
            boolean result = false;
            if (deposit){
                result = deposit(howMuch);
            } else result = withdraw(howMuch);
            if (result) owner.playSound(owner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.8f);
        });

        return icon;
    }

    private boolean deposit(int amount){
        if (owner.getInventory().contains(Material.DIAMOND, amount)){
            owner.getInventory().removeItem(new ItemStack(Material.DIAMOND, amount));

            getPlayerData(owner).setDiamonds(getPlayerData(owner).getDiamonds()+amount);
            addItem(4, createPlayerStats());
            return true;
        }
        return false;
    }

    private boolean withdraw(int amount){
        if (getPlayerData(owner).getDiamonds()>=amount){
            getPlayerData(owner).setDiamonds(getPlayerData(owner).getDiamonds()-amount);

            ItemStack item = new ItemStack(Material.DIAMOND, amount);
            if (Arrays.asList(owner.getInventory().getStorageContents()).contains(null)){
                owner.getInventory().addItem(item);
            }else {
                owner.getWorld().dropItem(owner.getLocation(), item);
            }
            addItem(4, createPlayerStats());
            return true;
        }
        return false;
    }
}
