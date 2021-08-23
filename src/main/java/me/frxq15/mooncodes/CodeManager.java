package me.frxq15.mooncodes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeManager {
    MoonCodes plugin = MoonCodes.getInstance();
    public boolean codeExists(String code) {
        if(MoonCodes.getInstance().getFileManager().getCodesFile().isConfigurationSection(code)) {
            return true;
        }
        return false;
    }
    public void createCode(CommandSender sender, String code, int uses) {
        plugin.getFileManager().getCodesFile().set(code + ".CREATED-BY", sender.getName());
        plugin.getFileManager().getCodesFile().set(code + ".USES", uses);
        plugin.getFileManager().getCodesFile().set(code + ".COMMANDS", Arrays.asList("give %player% dirt 1"));
        plugin.getFileManager().saveCodesFile();
    }
    public void deleteCode(String code) {
        plugin.getFileManager().getCodesFile().set(code, null);
        plugin.getFileManager().saveCodesFile();
    }
    public int getUses(String code) { return plugin.getFileManager().getCodesFile().getInt(code + ".USES"); }
    public String getCreator(String code) { return plugin.getFileManager().getCodesFile().getString(code + ".CREATED-BY"); }
    public List<String> getCommands(String code) { return plugin.getFileManager().getCodesFile().getStringList(code + ".COMMANDS"); }
    public boolean hasRedeemed(CommandSender sender, String code) {
        List<String> redeemers = new ArrayList<>();
        for(String player : plugin.getFileManager().getCodesFile().getStringList(code + ".REDEEMED_BY")) {
            redeemers.add(player);
            }
                if(redeemers.contains(sender.getName())) {
                    return true;
            }
            return false;
    }
    public void addRedeemer(String name, String code) {
        List<String> redeemers = new ArrayList<>();
        for(String player : plugin.getFileManager().getCodesFile().getStringList(code + ".REDEEMED_BY")) {
            redeemers.add(player);
        }
        redeemers.add(name);
        int current = plugin.getFileManager().getCodesFile().getInt(code + ".USES");
        plugin.getFileManager().getCodesFile().set(code + ".USES", (current-1));
        plugin.getFileManager().getCodesFile().set(code + ".REDEEMED_BY", redeemers);
        plugin.getFileManager().saveCodesFile();
    }
    public void redeemCode(Player p, String code) {
        addRedeemer(p.getName(), code);
        for(String cmds : plugin.getFileManager().getCodesFile().getStringList(code + ".COMMANDS")) {
            cmds = cmds.replace("%player%", p.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmds);
        }
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        runFireWorkTimer(p);
    }
    public void runFireWorkTimer(Player p) {
        final int[] count = {5};
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (count[0] == 0) {
                    cancel(); // Cancels the timer
                } else {
                    p.getWorld().spawn(p.getLocation(), Firework.class);
                    count[0]--;
                }
            }
        }.runTaskTimer(plugin, 5L, 5L);
    }
}
