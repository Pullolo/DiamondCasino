package net.pullolo.diamondCasino.gui.games;

import java.util.ArrayList;

import static net.pullolo.diamondCasino.util.Utils.translate;

public class PlayingCard {
    private static final ArrayList<PlayingCard> allCards = new ArrayList<>();

    public static ArrayList<PlayingCard> getAllCardsList(){
        return (ArrayList<PlayingCard>) allCards.clone();
    }

    public static void initCards(){
        String[] symbols = new String[4];
        symbols[0] = "♠";
        symbols[1] = "♥";
        symbols[2] = "♦";
        symbols[3] = "♣";

        String[] icons = new String[13];
        icons[0] = "2";
        icons[1] = "3";
        icons[2] = "4";
        icons[3] = "5";
        icons[4] = "6";
        icons[5] = "7";
        icons[6] = "8";
        icons[7] = "9";
        icons[8] = "10";
        icons[9] = "J";
        icons[10] = "Q";
        icons[11] = "K";
        icons[12] = "A";

        for (int a = 0; a<4; a++){
            for (int i = 0; i<13; i++){
                allCards.add(new PlayingCard(i+2, symbols[a], icons[i], a%2==1));
            }
        }
    }

    private int value;
    private final String symbol;
    private final String icon;
    private final boolean red;

    public PlayingCard(int value, String symbol, String icon, boolean red) {
        this.value = value;
        this.symbol = symbol;
        this.icon = icon;
        this.red = red;
    }

    @Override
    public String toString(){
        return translate(
                (red ? "&c" : "&8") + symbol + " " + icon
        );
    }

    public String getIcon() {
        return icon;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value){
        this.value = value;
    }
}
