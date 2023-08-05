package net.zonixmc.beacon;

import lombok.Getter;
import net.zonixmc.beacon.cache.BeaconCache;
import net.zonixmc.beacon.command.GiveBeaconCommand;
import net.zonixmc.beacon.command.GiveKeyCommand;
import net.zonixmc.beacon.dao.BeaconDAO;
import net.zonixmc.beacon.inventory.ConfigurationInventory;
import net.zonixmc.beacon.inventory.PyramidInventory;
import net.zonixmc.beacon.listener.*;
import net.zonixmc.beacon.util.CommandRegister;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@Getter
public enum BeaconApi {
    INSTANCE;

    private BeaconPlugin plugin;

    private YamlConfiguration configuration;
    private BeaconDAO beaconDAO;
    private BeaconCache beaconCache;

    public void start(BeaconPlugin plugin) {

        this.plugin = plugin;

        File folder = plugin.getDataFolder();

        if (!folder.exists()) folder.mkdirs();

        File file = folder.toPath().resolve("config.yml").toFile();

        if (!file.exists()) plugin.saveDefaultConfig();

        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.beaconDAO = new BeaconDAO();
        this.beaconCache = new BeaconCache(beaconDAO);

        Bukkit.getPluginManager().registerEvents(new ConfigurationInventory(), plugin);
        Bukkit.getPluginManager().registerEvents(new PyramidInventory(), plugin);

        Bukkit.getPluginManager().registerEvents(new BeaconBreakListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new BeaconExplodeListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new BeaconInteractListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new BeaconPlaceListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new BeaconTickListener(), plugin);

        CommandRegister.registryCommand(new GiveBeaconCommand());
        CommandRegister.registryCommand(new GiveKeyCommand());

    }

    public void stop(BeaconPlugin plugin) {

        this.plugin = plugin;

        beaconCache.getCustomBeacons().forEach((locationKey, customBeacon) -> beaconDAO.saveBeacon(locationKey, customBeacon, false));

    }

}
