package net.pullolo.diamondCasino.gui.games.dice;

import mc.obliviate.inventory.Icon;
import net.pullolo.diamondCasino.gui.MainCasinoHall;
import net.pullolo.diamondCasino.gui.base.BaseUncloseableGui;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

import static net.pullolo.diamondCasino.data.PlayerData.getPlayerData;
import static net.pullolo.diamondCasino.util.Utils.translate;

public class Dice extends BaseUncloseableGui {
    private final Random r = new Random();
    private final int bet;
    private int turn;
    private boolean lost;
    private boolean won;

    private int dice1;
    private int dice2;

    public Dice(@NotNull Player player, int bet) {
        super(player, "g-2", "Game Of Dice", 3);
        this.bet = bet;
        this.turn = 1;
        this.lost = false;
        this.won = false;
        initDice();
    }

    private void initDice(){
        if (r.nextBoolean()){
            dice1=getRandomNumber();
            dice2=getRandomNumber();
        } else {
            if (r.nextBoolean()){
                dice1=getRandomNumber();
                dice2=7-dice1;
            } else {
                dice2=getRandomNumber();
                dice1=7-dice2;
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        super.onOpen(event);
        fillGui(createFiller());

        updateUi();
    }

    private void updateUi(){
        if (won || lost) addItem(13, createWinIcon(won));
        else addItem(13, createDiceIcon(dice1, dice2));

        if (lost || won) addItem(15, createFiller());
        else addItem(15, createInteractIcon(true));

        if (lost || won) addItem(11, createFiller());
        else addItem(11, createInteractIcon(false));

        if (won || lost) addItem(18, createFiller());
        else addItem(18, createCashOutIcon());
    }

    private Icon createInteractIcon(boolean higher){
        Icon i = new Icon(higher ? Material.GOLD_NUGGET : Material.IRON_NUGGET);
        i.setName(translate(
                "&f> Bet " + (higher ? "&cHigher" : "&bLower") + " &f<"
        ));

        i.onClick(click->{
            if (higher) betHigher();
            else betLower();
            if (lost) owner.playSound(owner.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2, 0.5f);
            else owner.playSound(owner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
            updateUi();
        });

        return i;
    }

    private Icon createWinIcon(boolean won){
        Icon icon = new Icon(won ? Material.LIME_CONCRETE : Material.RED_CONCRETE);
        icon.setName(translate(
                (won ? "&aYOU WON" : "&cYOU LOST")
        ));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(translate(
                "&7Click to claim &b" + (won ? bet*turn : 0) + " &7diamonds"
        ));
        icon.setLore(lore);

        icon.onClick(click->{
            this.getInventory().close();
            if (won){
                getPlayerData(owner).setDiamonds(getPlayerData(owner).getDiamonds() + bet*turn);
            }
            owner.playSound(owner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
            new MainCasinoHall(owner).open();
        });

        return icon;
    }

    private void betHigher(){
        int val = getDice();
        rerollDice();
        if (getDice()>val){
            turn++;
        } else if (getDice()==val){
            return;
        } else {
            lost=true;
        }
    }

    private void betLower(){
        int val = getDice();
        rerollDice();
        if (getDice()<val){
            turn++;
        } else if (getDice()==val){
            return;
        } else {
            lost=true;
        }
    }

    private Icon createCashOutIcon(){
        Icon icon = new Icon(Material.DIAMOND_BLOCK);
        icon.setName(translate(
                 turn<2 ? "&bBet information" : "&f> &bCash Out &f<"
        ));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(translate(
                " &b✦ &7Your bet - &b" + bet
        ));

        if (turn>1){
            lore.add("");
            lore.add(translate(
                    " &b✦ &7Click to Cash Out - &b" + bet*turn
            ));
        }

        icon.setLore(lore);

        icon.onClick(click->{
            if (turn<2) return;
            won=true;
            updateUi();
        });

        return icon;
    }

    private Icon createDiceIcon(int a, int b){
        Icon icon = new Icon(Material.HEAVY_CORE);
        icon.setName(translate("&f< Dice >"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(translate(
                " " + (turn%2==0 ? "&7" : "&c") + "◆ " + a
        ));
        lore.add(translate(
                " " + (turn%2==0 ? "&c" : "&7") + "◆ " + b
        ));
        lore.add("");
        lore.add(translate(
                " &a◆ &7Total - &a" + (a+b)
        ));
        icon.setLore(lore);
        return icon;
    }

    private int getRandomNumber(){
        return r.nextInt(6)+1;
    }

    private void rerollDice(){
        dice1=getRandomNumber();
        dice2=getRandomNumber();
    }

    private int getDice(){
        return dice1+dice2;
    }
}
