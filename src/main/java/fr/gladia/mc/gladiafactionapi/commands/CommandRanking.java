package fr.gladia.mc.gladiafactionapi.commands;

import fr.gladia.mc.gladiafactionapi.GladiaFactionAPI;
import fr.gladia.mc.gladiafactionapi.utils.FactionSqlUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.HashMap;

public class CommandRanking implements CommandExecutor, Listener {

    GladiaFactionAPI main;

    public CommandRanking(GladiaFactionAPI main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getInventory().getTitle().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Ranking")) {
            event.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            HashMap<String, Integer> factionRanking = new HashMap<String, Integer>(FactionSqlUtil.getAllFactions());
            Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "" + ChatColor.BOLD + "Ranking");

            int diamondRank = 0;
            String diamondFaction = "";
            for (String entry : factionRanking.keySet()) {
                if (factionRanking.get(entry) >= diamondRank) {
                    diamondFaction = entry;
                    diamondRank = factionRanking.get(entry);
                }
            }
            if(factionRanking.containsKey(diamondFaction)) {
                diamondRank = factionRanking.get(diamondFaction);
                factionRanking.remove(diamondFaction);
            }
            ItemStack diamondStack = new ItemStack(Material.DIAMOND_BLOCK, 1);
            ItemMeta diamondMeta = diamondStack.getItemMeta();
            diamondMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "" + diamondFaction);
            diamondMeta.setLore(Arrays.asList(ChatColor.AQUA + "" + diamondRank + " points"));
            diamondStack.setItemMeta(diamondMeta);
            inv.setItem(0, diamondStack);

            int goldRank = 0;
            String goldFaction = "";
            for (String entry : factionRanking.keySet()) {
                if (factionRanking.get(entry) >= goldRank) {
                    goldFaction = entry;
                    goldRank = factionRanking.get(entry);
                }
            }
            if(factionRanking.containsKey(goldFaction)) {
                goldRank = factionRanking.get(goldFaction);
                factionRanking.remove(goldFaction);
            }
            ItemStack goldStack = new ItemStack(Material.GOLD_BLOCK, 1);
            ItemMeta goldMeta = goldStack.getItemMeta();
            goldMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "" + goldFaction);
            goldMeta.setLore(Arrays.asList(ChatColor.YELLOW + "" + goldRank + " points"));
            goldStack.setItemMeta(goldMeta);
            inv.setItem(1, goldStack);

            int ironRank = 0;
            String ironFaction = "";
            for (String entry : factionRanking.keySet()) {
                if (factionRanking.get(entry) >= ironRank) {
                    ironFaction = entry;
                    ironRank = factionRanking.get(entry);
                }
            }
            if(factionRanking.containsKey(ironFaction)) {
                ironRank = factionRanking.get(ironFaction);
                factionRanking.remove(ironFaction);
            }
            ItemStack ironStack = new ItemStack(Material.IRON_BLOCK, 1);
            ItemMeta ironMeta = ironStack.getItemMeta();
            ironMeta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "" + ironFaction);
            ironMeta.setLore(Arrays.asList(ChatColor.GRAY +  "" + ironRank + " points"));
            ironStack.setItemMeta(ironMeta);
            inv.setItem(2, ironStack);

            int redstoneRank = 0;
            String redstoneFaction = "";
            for (String entry : factionRanking.keySet()) {
                if (factionRanking.get(entry) >= redstoneRank) {
                    redstoneFaction = entry;
                    redstoneRank = factionRanking.get(entry);
                }
            }
            if(factionRanking.containsKey(redstoneFaction)) {
                redstoneRank = factionRanking.get(redstoneFaction);
                factionRanking.remove(redstoneFaction);
            }
            ItemStack redstoneStack = new ItemStack(Material.REDSTONE_BLOCK, 1);
            ItemMeta redstoneMeta = redstoneStack.getItemMeta();
            redstoneMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "" + redstoneFaction);
            redstoneMeta.setLore(Arrays.asList(ChatColor.RED +  "" + redstoneRank + " points"));
            redstoneStack.setItemMeta(redstoneMeta);
            inv.setItem(3, redstoneStack);

            int lapisRank = 0;
            String lapisFaction = "";
            for (String entry : factionRanking.keySet()) {
                if (factionRanking.get(entry) >= lapisRank) {
                    lapisFaction = entry;
                    lapisRank = factionRanking.get(entry);
                }
            }
            if(factionRanking.containsKey(lapisFaction)) {
                lapisRank = factionRanking.get(lapisFaction);
                factionRanking.remove(lapisFaction);
            }
            ItemStack lapisStack = new ItemStack(Material.LAPIS_BLOCK, 1);
            ItemMeta lapisMeta = lapisStack.getItemMeta();
            lapisMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "" + lapisFaction);
            lapisMeta.setLore(Arrays.asList(ChatColor.DARK_BLUE +  "" + lapisRank + " points"));
            lapisStack.setItemMeta(lapisMeta);
            inv.setItem(4, lapisStack);

            ItemStack headStack = new ItemStack(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal());
            SkullMeta headMeta = (SkullMeta) headStack.getItemMeta();
            headMeta.setOwner(player.getName());
            headMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "" + FactionSqlUtil.getPlayerFaction(player.getName()));
            headMeta.setLore(Arrays.asList(ChatColor.GREEN + "" + FactionSqlUtil.getFactionPoints(FactionSqlUtil.getPlayerFaction(player.getName())) + " points"));
            headStack.setItemMeta(headMeta);
            inv.setItem(8, headStack);

            player.openInventory(inv);
        }

        return true;
    }

}
