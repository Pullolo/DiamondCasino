package net.pullolo.diamondCasino.gui.games.blackjack;

import mc.obliviate.inventory.Icon;
import net.pullolo.diamondCasino.gui.MainCasinoHall;
import net.pullolo.diamondCasino.gui.base.BaseUncloseableGui;
import net.pullolo.diamondCasino.gui.games.PlayingCard;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import static net.pullolo.diamondCasino.data.PlayerData.getPlayerData;
import static net.pullolo.diamondCasino.util.Utils.translate;

public class BlackJack extends BaseUncloseableGui {
    private Icon centerIcon;
    private boolean playerStanding = false;
    private boolean dealerStanding = false;
    private boolean gameOver = false;
    private final int bet;
    private boolean openFirstTime;
    private final ArrayList<PlayingCard> deck;

    private final ArrayList<PlayingCard> playersCards = new ArrayList<>();
    private final ArrayList<PlayingCard> dealersCards = new ArrayList<>();

    public BlackJack(@NotNull Player player, int bet) {
        super(player, "g-1", "Black Jack", 3);
        this.bet = bet;
        this.openFirstTime = true;

        ArrayList<PlayingCard> deck = PlayingCard.getAllCardsList();
        Collections.shuffle(deck);
        this.deck = deck;
        centerIcon = createBetInfo();
    }

    private void initGame(){
        PlayingCard c1 = deck.getLast();
        deck.removeLast();
        playersCards.add(c1);

        PlayingCard c2 = deck.getLast();
        deck.removeLast();
        dealersCards.add(c2);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        if (openFirstTime) {
            initGame();
            openFirstTime = false;
        }
        fillGui(createFiller());

        addItem(4, createPlayerCards(false));
        addItem(13, centerIcon);
        addItem(22, createPlayerCards(true));

        if (playerStanding || gameOver) addItem(15, createFiller());
        else addItem(15, createInteractItem(true));

        if (gameOver) addItem(11, createFiller());
        else addItem(11, createInteractItem(false));;
    }

    private Icon createBetInfo(){
        Icon icon = new Icon(Material.DIAMOND);
        icon.setName(translate(
           "&fBet information"
        ));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(translate(
                " &b✦ &7Your diamonds - &b" + bet
        ));
        lore.add(translate(
                " &b✦ &7Dealer's diamonds - &b" + bet
        ));
        lore.add("");
        lore.add(translate(
                " &b✦ &7Total winnings - &b" + bet*2
        ));
        icon.setLore(lore);

        return icon;
    }

    private Icon createWinIcon(boolean draw, boolean won){
        Material material;
        if (won){
            material = Material.LIME_CONCRETE;
        } else if (draw) {
            material = Material.GRAY_CONCRETE;
        } else material = Material.RED_CONCRETE;

        Icon icon = new Icon(material);
        icon.setName(translate(
                (won ? "&aYOU WON" : (draw ? "&7DRAW" : "&cYOU LOST"))
        ));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(translate(
                "&7Click to claim &b" + (won ? bet*2 : (draw ? bet : 0)) + " &7diamonds"
        ));
        icon.setLore(lore);

        icon.onClick(click->{
            this.getInventory().close();
            if (won || draw){
                getPlayerData(owner).setDiamonds(getPlayerData(owner).getDiamonds() + (won ? bet*2 : bet));
            }
            owner.playSound(owner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
            new MainCasinoHall(owner).open();
        });

        return icon;
    }

    private void updateUi(){
        addItem(4, createPlayerCards(false));
        addItem(22, createPlayerCards(true));

        if (playerStanding || gameOver) addItem(15, createFiller());
        else addItem(15, createInteractItem(true));

        if (gameOver) addItem(11, createFiller());
        else addItem(11, createInteractItem(false));;
    }

    private void hit(boolean player){
        PlayingCard card = deck.getLast();
        deck.removeLast();
        if (card.getValue()==14 && getValue(player ? playersCards : dealersCards)+14>21){
            card.setValue(1);
        }
        if (player){
            playersCards.add(card);
        } else {
            dealersCards.add(card);
        }
    }

    private void dealerWin(){
        centerIcon = createWinIcon(false, false);
        addItem(13, centerIcon);
        removeInteractables();
    }

    private void playerWin(){
        centerIcon = createWinIcon(false, true);
        addItem(13, centerIcon);
        removeInteractables();
    }

    private void draw(){
        centerIcon = createWinIcon(true, false);
        addItem(13, centerIcon);
        removeInteractables();
    }

    private void removeInteractables(){
        addItem(11, createFiller());
        addItem(15, createFiller());
        gameOver = true;
    }

    private void checkWin(){
        int p = getValue(playersCards), d = getValue(dealersCards);

        if (playerStanding && dealerStanding){
            if (p > 21 && d > 21) {
                draw(); // Both bust
            } else if (p > 21) {
                dealerWin(); // Player busts, dealer wins
            } else if (d > 21) {
                playerWin(); // Dealer busts, player wins
            } else if (p == d) {
                draw(); // Scores are equal
            } else if (p > d) {
                playerWin(); // Player has higher score
            } else {
                dealerWin(); // Dealer has higher score
            }
            return;
        }

        if (p > 21 && d > 21) {
            draw(); // Both bust
        } else if (d > 21) {
            playerWin(); // Dealer busts
        } else if (p > 21){
            dealerWin(); // Player busts
        }
    }

    private int getValue(ArrayList<PlayingCard> cards){
        int sum = 0;
        for (PlayingCard card : cards){
            sum+=card.getValue();
        }
        return sum;
    }

    private void stand(boolean player){
        if (player){
            playerStanding = true;
        } else dealerStanding = true;
        return;
    }

    private void dealerTurn(){
        int a = 0;
        for (PlayingCard card : dealersCards){
            a+=card.getValue();
        }
        if (a<17 && !dealerStanding){
            hit(false);
        } else stand(false);
    }

    private Icon createInteractItem(boolean hit){
        Icon icon = new Icon(hit ? Material.NETHERITE_SWORD : Material.SHIELD);
        icon.setName(translate(
           "&f> " + (hit ? "&cHit" : "&3Stand") + " &f<"
        ));

        icon.onClick(click -> {
            if (hit) hit(true);
            else stand(true);
            owner.playSound(owner.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 2);
            dealerTurn();
            checkWin();
            updateUi();
        });

        return icon;
    }

    private Icon createPlayerCards(boolean players){
        Icon icon = new Icon(Material.PAPER);

        icon.setName(translate(
                "&f< " + (players ? "&aYour &fcards" : "&cDealers &fcards") + " >"
        ));

        ArrayList<String> lore = new ArrayList<>();
        int total = 0;
        for (PlayingCard card : (players ? playersCards : dealersCards)){
            lore.add(translate(
                    " " + card.toString()
            ));
            total += card.getValue();
        }

        lore.add("");
        lore.add(translate(
                " &7Total - &a" + total
        ));
        icon.setLore(lore);

        return icon;
    }
}
