package net.pullolo.diamondCasino.commands;

import net.pullolo.diamondCasino.gui.MainCasinoHall;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Casino extends BaseCommand{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("casino")){
            return false;
        }
        if (!(sender instanceof Player p)){
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        new MainCasinoHall(p).open();
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
