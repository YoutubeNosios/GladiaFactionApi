package fr.gladia.mc.gladiafactionapi.commands;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Factions;
import fr.gladia.mc.gladiafactionapi.GladiaFactionAPI;
import fr.gladia.mc.gladiafactionapi.utils.FactionSqlUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandPlayerPoints implements CommandExecutor {

    GladiaFactionAPI main;

    public CommandPlayerPoints(GladiaFactionAPI main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            String faction = FactionSqlUtil.getPlayerFaction(args[0]);
            if (Factions.getInstance().isTagTaken(faction)) {
                FactionSqlUtil.setFactionPoints(faction, FactionSqlUtil.getFactionPoints(faction) + Integer.parseInt(args[1]));
                for (FPlayer fplayer : Factions.getInstance().getByTag(faction).getFPlayers()) {
                    if (fplayer.isOnline()) {
                        fplayer.sendMessage(main.conf.getConfig().getString("messages.receivedPoints").replaceAll("%POINTS%", args[1]));
                    }
                }
                sender.sendMessage(main.conf.getConfig().getString("messages.gavePoints").replaceAll("%POINTS%", args[1]).replaceAll("%FACTION%", faction));
            }
        }

        return true;
    }

}
