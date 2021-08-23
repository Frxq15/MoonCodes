package me.frxq15.mooncodes;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {
    public File CodesFile;
    public FileConfiguration CodesConfig;

    public void createCodesFile() {
        CodesFile = new File(MoonCodes.getInstance().getDataFolder(), "codes.yml");
        if (!CodesFile.exists()) {
            CodesFile.getParentFile().mkdirs();
            MoonCodes.getInstance().log("codes.yml was created successfully");
            MoonCodes.getInstance().getInstance().saveResource("codes.yml", false);
        }

        CodesConfig = new YamlConfiguration();
        try {
            CodesConfig.load(CodesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadCodesFile() { CodesConfig = YamlConfiguration.loadConfiguration(CodesFile); }
    public void saveCodesFile() {
        try {
            CodesConfig.save(CodesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getCodesFile() { return CodesConfig; }
}
