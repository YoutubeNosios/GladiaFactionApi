package fr.gladia.mc.gladiafactionapi.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class YamlUtil {
    Plugin plugin;
    File confDataFile;
    FileConfiguration confDataConfig;

    public YamlUtil(Plugin plugin, String pathName) {
        this.plugin = plugin;
             confDataFile = new File(plugin.getDataFolder(), pathName);
             confDataConfig = YamlConfiguration.loadConfiguration(confDataFile);
             loadYml();
    }
    public FileConfiguration getConfig(){
        return confDataConfig;
    }
    public File getFile(){
        return confDataFile;
    }
    public void save(){
        try {
            confDataConfig.save(confDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadYml() {
        if(!confDataFile.exists()){
            confDataFile.getParentFile().mkdirs();
            plugin.saveResource(confDataFile.getName(), false);
        }
        confDataConfig= new YamlConfiguration();
        try {
            confDataConfig.load(confDataFile);
        } catch (Exception e) {
            System.out.println("ERROR LOADING FILE");
        }
    }
}
