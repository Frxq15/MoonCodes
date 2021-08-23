package me.frxq15.mooncodes;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MoonCodes extends JavaPlugin {
    private static MoonCodes instance;
    public FileManager fileManager;
    public CodeManager codeManager;

    @Override
    public void onEnable() {
        initialize();
        saveDefaultConfig();
        fileManager.createCodesFile();
        log("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        log("Plugin disabled.");
    }
    public static MoonCodes getInstance() { return instance; }
    public void log(String log) { Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE+"[MoonCodes] "+log); }
    public String colourize(String msg) { return ChatColor.translateAlternateColorCodes('&', msg); }
    public String formatMsg(String input) { return ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString(input)); }
    public FileManager getFileManager() { return fileManager; }
    public CodeManager getCodeManager() { return codeManager; }
    public void initialize() {
        instance = this;
        fileManager = new FileManager();
        codeManager = new CodeManager();
        getCommand("code").setExecutor(new CodeCommand());
    }
}
