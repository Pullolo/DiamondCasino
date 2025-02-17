package net.pullolo.diamondCasino.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

import static net.pullolo.diamondCasino.DiamondCasino.casinoPlugin;
import static net.pullolo.diamondCasino.data.PlayerData.getPlayerData;

public class DroppedDiamonds {
    public static final ArrayList<DroppedDiamonds> droppedDiamonds = new ArrayList<>();
    private ArmorStand entity;
    private ArmorStand diamondDisplayer;
    private final int amount;

    public DroppedDiamonds(Player dead, int amount){
        this.amount=amount;
        droppedDiamonds.add(this);

        entity = dead.getWorld().spawn(dead.getLocation().add(0, 1, 0), ArmorStand.class, (as) -> {
            as.setVisible(false);
            as.setInvisible(true);
            as.setMarker(true);
            as.setGravity(false);
            as.customName(Component.text("✦ ").decoration(TextDecoration.ITALIC, false).color(TextColor.color(26, 228, 255))
                    .append(Component.text("Diamonds - ").decoration(TextDecoration.ITALIC, false).color(TextColor.color(225, 225, 225)))
                    .append(Component.text(String.valueOf(amount)).decoration(TextDecoration.ITALIC, false).color(TextColor.color(26, 228, 255)))
            );
            as.setCustomNameVisible(true);
        });

        diamondDisplayer = dead.getWorld().spawn(dead.getLocation().add(0, 0, 0), ArmorStand.class, (as) -> {
            as.setVisible(false);
            as.setInvisible(true);
            as.setMarker(true);
            as.setGravity(false);
            as.setCustomNameVisible(false);
            as.getEquipment().setHelmet(new ItemStack(Material.DIAMOND, 1));
        });

        new BukkitRunnable() {
            int i = 0;
            double baseY = diamondDisplayer.getY();
            @Override
            public void run() {
                if (entity.isDead()){
                    cancel();
                    remove();
                    return;
                }
                i++;
                if(i>20*60*15){
                    cancel();
                    remove();
                    return;
                }
                if (diamondDisplayer.canTick()){
                    diamondDisplayer.teleport(new Location(diamondDisplayer.getWorld(), diamondDisplayer.getX(), baseY + Math.sin((double) i/8)/4, diamondDisplayer.getZ(), diamondDisplayer.getYaw()+10, diamondDisplayer.getPitch()));
                }
                if (!entity.canTick()) return;
                for (Player p : entity.getLocation().getNearbyPlayers(1.2)){
                    if (p.isDead()) continue;
                    onPickUp(p);
                    cancel();
                    return;
                }
            }
        }.runTaskTimer(casinoPlugin, 0, 2);
    }

    public void onPickUp(Player p){
        p.sendMessage(Component.text("✦ ").decoration(TextDecoration.ITALIC, false).color(TextColor.color(26, 228, 255))
                .append(Component.text("You just picked up ").decoration(TextDecoration.ITALIC, false).color(TextColor.color(145, 145, 145)))
                .append(Component.text(String.valueOf(amount)).decoration(TextDecoration.ITALIC, false).color(TextColor.color(26, 228, 255)))
                .append(Component.text(" diamond(s).").decoration(TextDecoration.ITALIC, false).color(TextColor.color(145, 145, 145))));
        p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
        getPlayerData(p).setDiamonds(getPlayerData(p).getDiamonds()+amount);
        droppedDiamonds.remove(this);
        remove();
    }

    public void remove(){
        diamondDisplayer.remove();
        diamondDisplayer=null;
        entity.remove();
        entity=null;
    }
}
