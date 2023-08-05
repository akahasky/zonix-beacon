package net.zonixmc.beacon;

import org.bukkit.plugin.java.JavaPlugin;

public class BeaconPlugin extends JavaPlugin {

    public void onEnable() { BeaconApi.INSTANCE.start(this); }

    public void onDisable() { BeaconApi.INSTANCE.stop(this); }

}