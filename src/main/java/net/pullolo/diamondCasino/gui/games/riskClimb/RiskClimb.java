package net.pullolo.diamondCasino.gui.games.riskClimb;

import mc.obliviate.inventory.Icon;
import net.pullolo.diamondCasino.gui.MainCasinoHall;
import net.pullolo.diamondCasino.gui.base.BaseUncloseableGui;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

import static net.pullolo.diamondCasino.DiamondCasino.casinoPlugin;
import static net.pullolo.diamondCasino.data.PlayerData.getPlayerData;
import static net.pullolo.diamondCasino.util.Utils.translate;

public class RiskClimb extends BaseUncloseableGui {

    private final Random random = new Random();
    private final int bet;
    private int turns = 0;
    private final int maxTurns = 1 + (random.nextBoolean() ? random.nextInt(6) : random.nextInt(64));
    private boolean endTimer;
    private boolean lost;
    private boolean won;
    private long winnings;
    private double multiplier = 1;
    int[] ids = new int[8];

    public RiskClimb(@NotNull Player player, int betAmount) {
        super(player, "g-3", "Risk Climb", 3);
        bet = betAmount;
        winnings = betAmount;
        lost=false;
        endTimer=false;
        initIds();
        BukkitRunnable timer = new BukkitRunnable() {
            @Override
            public void run() {
                if (!(endTimer && won)) {
                    if (turns >= maxTurns) {
                        endTimer = true;
                        lost = true;
                        winnings = 0;
                        multiplier = 0;
                        updateUi();
                    }
                }
                if (endTimer) {
                    cancel();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            createEndScreen();
                        }
                    }.runTaskLater(casinoPlugin, 10);
                    return;
                }
                turns++;
                multiplier = 1 + ((double) turns / 10);
                winnings = (((long) Math.floor(bet * multiplier)) == bet ? bet + 1 : (long) Math.floor(bet * multiplier));
                updateUi();
            }
        };
        timer.runTaskTimer(casinoPlugin, 20, 20);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        fillGui(createFiller());
        updateUi();
        if (won || lost){
            createEndScreen();
        }
    }

    public void updateUi(){
        addItem(13, createGameIcon());
        if(turns>0) addGrowthIndicators();
    }

    public void addGrowthIndicators(){
        if (!lost && !won){
            addItem(ids[turns % ids.length], createProgressIcon(false));
            return;
        }
        for (int id : ids) {
            addItem(id, createProgressIcon(lost));
        }
    }

    public void createEndScreen(){
        fillGui(createFiller());
        addItem(13, createWinIcon(!lost && won));
    }

    private Icon createProgressIcon(boolean lost){
        Icon icon = new Icon(lost ? Material.RED_STAINED_GLASS : getGlassColorByTurn(turns));
        icon.setName(translate(
                (lost ? "&cLost :(" : "&aWinning :)")
        ));
        return icon;
    }

    public Material getGlassColorByTurn(int turn){
        if (turn>=48) {
            return random.nextBoolean() ? Material.PINK_STAINED_GLASS : Material.MAGENTA_STAINED_GLASS;
        } else if (turn>=40) {
            return Material.PINK_STAINED_GLASS;
        } else if (turn>=32) {
            return Material.YELLOW_STAINED_GLASS;
        } else if (turn>=24) {
            return Material.PURPLE_STAINED_GLASS;
        } else if (turn>=16) {
            return Material.LIGHT_BLUE_STAINED_GLASS;
        } else if (turn>=8) {
            return Material.LIME_STAINED_GLASS;
        } else return Material.LIGHT_GRAY_STAINED_GLASS;
    }

    private Icon createGameIcon(){
        Icon icon = new Icon(lost ? Material.RED_CONCRETE : Material.LIME_CONCRETE);
        icon.setName(translate(
                (lost ? "&c" : "&a") + "x " + multiplier + (lost ? "" : turns>4 ? " - &eClick To Stop" : " - &cToo Early To Stop")
        ));

        ArrayList<String> lore = new ArrayList<>();
        if (!lost) lore.add(translate(
                "&7You could win &b" + winnings + " &7diamonds"
        ));
        icon.setLore(lore);

        icon.onClick(click->{
            if (endTimer) return;
            if(lost || turns<=4) return;
            endTimer=true;
            won=true;
            owner.playSound(owner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
            addGrowthIndicators();
        });

        return icon;
    }

    private Icon createWinIcon(boolean won){
        Icon icon = new Icon(won ? Material.LIME_CONCRETE : Material.RED_CONCRETE);
        icon.setName(translate(
                (won ? "&aYOU WON" : "&cYOU LOST")
        ));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(translate(
                "&7Click to claim &b" + (won ? winnings : 0) + " &7diamonds"
        ));
        icon.setLore(lore);

        icon.onClick(click->{
            this.getInventory().close();
            if (won){
                getPlayerData(owner).setDiamonds(getPlayerData(owner).getDiamonds() + (int) winnings);
            }
            owner.playSound(owner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
            new MainCasinoHall(owner).open();
        });

        return icon;
    }

    private void initIds(){
        ids[0] = 3;
        ids[1] = 4;
        ids[2] = 5;
        ids[3] = 14;
        ids[4] = 23;
        ids[5] = 22;
        ids[6] = 21;
        ids[7] = 12;
    }
}
