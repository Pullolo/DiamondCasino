package net.pullolo.diamondCasino;

import mc.obliviate.inventory.InventoryAPI;
import net.pullolo.diamondCasino.commands.BaseCommand;
import net.pullolo.diamondCasino.commands.Casino;
import net.pullolo.diamondCasino.data.Database;
import net.pullolo.diamondCasino.data.PlayerData;
import net.pullolo.diamondCasino.events.DataEventsHandler;
import net.pullolo.diamondCasino.events.PlayerEventsHandler;
import net.pullolo.diamondCasino.gui.games.PlayingCard;
import net.pullolo.diamondCasino.items.DroppedDiamonds;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import static net.pullolo.diamondCasino.items.DroppedDiamonds.droppedDiamonds;

public final class DiamondCasino extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private InventoryAPI inventoryAPI;
    private Database database;
    public static JavaPlugin casinoPlugin;

    @Override
    public void onEnable() {
        casinoPlugin = this;
        saveDefaultConfig();

        PlayingCard.initCards();
        logInfo("Initializing GUI library...");
        try {
            inventoryAPI = new InventoryAPI(this);
            inventoryAPI.init();
            logInfo("GUI library initialized!");
        } catch (Exception e){
            logWarning("GUI library failed to initialize!");
        }

        database = new Database(this);
        database.init();
        checkDb(database);
        setPlayerData(database);

        logInfo("Registering events...");
        getServer().getPluginManager().registerEvents(new DataEventsHandler(database), this);
        getServer().getPluginManager().registerEvents(new PlayerEventsHandler(!getConfig().getBoolean("drop-diamonds-on-death")), this);
        logInfo("Events registered!");

        logInfo("Registering commands...");
        registerCommand(new Casino(), "casino");
        logInfo("Commands registered!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        unloadDrops();
        savePlayers(database);
        database.disconnect();
        disableGuis();
    }

    private void unloadDrops(){
        for (DroppedDiamonds diamonds : droppedDiamonds){
            diamonds.remove();
        }
    }

    private void checkDb(Database db){
        db.connect();
        if (db.isDbEnabled()){
            logInfo("Database is operational.");
        } else logWarning("Database is offline!");
    }
    private void setPlayerData(Database db){
        for (Player p : getServer().getOnlinePlayers()){
            PlayerData.setPlayerDataFromDb(p, db);
        }
    }
    private void savePlayers(Database db){
        for (Player p : getServer().getOnlinePlayers()){
            PlayerData.savePlayerDataToDb(p, db);
            PlayerData.removePlayerData(p);
        }
    }

    private void disableGuis(){
        for (Player p : Bukkit.getOnlinePlayers()){
            try {
                inventoryAPI.getPlayersCurrentGui(p).getInventory().close();
            } catch (Exception e){
                continue;
            }
        }
        if (inventoryAPI!=null) inventoryAPI.unload();
    }

    public static void logInfo(String s){
        log.info(s);
    }

    public static void logWarning(String s){
        log.warning(s);
    }

    private void registerCommand(BaseCommand cmd, String cmdName){
        try {
            getCommand(cmdName).setExecutor(cmd);
            getCommand(cmdName).setTabCompleter(cmd);
        } catch (Exception e){
            logWarning("Nulls registering /" + cmdName);
        }
    }
}
