package fr.gladia.mc.gladiafactionapi.listeners;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.event.FPlayerJoinEvent;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.event.FactionCreateEvent;
import com.massivecraft.factions.event.FactionDisbandEvent;
import fr.gladia.mc.gladiafactionapi.utils.FactionSqlUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class FactionListeners implements Listener {

    @EventHandler
    public void onFactionJoin(FPlayerJoinEvent event) {
        String tag = event.getFaction().getTag();
        String player = event.getfPlayer().getName();
        FactionSqlUtil.setPlayerFaction(player, tag);
        FactionSqlUtil.setFactionSize(tag, FactionSqlUtil.getFactionSize(tag) + 1);
    }

    @EventHandler
    public void onFactionLeave(FPlayerLeaveEvent event) throws SQLException{
        String tag = event.getFaction().getTag();
        String player = event.getfPlayer().getName();
        FactionSqlUtil.setPlayerFaction(player, "");
        FactionSqlUtil.setFactionSize(tag, FactionSqlUtil.getFactionSize(tag) - 1);

    }

    @EventHandler
    public void onFactionCreate(FactionCreateEvent event) {
        String tag = event.getFactionTag();
        String player = event.getFPlayer().getName();
        FactionSqlUtil.createFaction(tag);
    }

    @EventHandler
    public void onFactionDisband(FactionDisbandEvent event) {
        String tag = event.getFaction().getTag();
        String player = event.getFPlayer().getName();
        ArrayList<String> playersInFaction = new ArrayList<String>();
        for(FPlayer playerInFaction : event.getFaction().getFPlayers()) {
            playersInFaction.add(playerInFaction.getName());
        }
        FactionSqlUtil.removeFaction(tag);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!(FactionSqlUtil.playerExists(player.getName()))) {
            FactionSqlUtil.createPlayer(player.getName());
        }
    }
}
