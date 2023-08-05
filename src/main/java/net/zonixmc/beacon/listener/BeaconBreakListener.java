package net.zonixmc.beacon.listener;

import net.zonixmc.beacon.BeaconApi;
import net.zonixmc.beacon.builder.BeaconBuilder;
import net.zonixmc.beacon.model.CustomBeacon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BeaconBreakListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    void on(BlockBreakEvent event) {

        Block block = event.getBlock();

        if (block.getType() != Material.BEACON) return;

        Location location = block.getLocation();
        CustomBeacon customBeacon = BeaconApi.INSTANCE.getBeaconCache().getValue(location);

        if (customBeacon == null) return;

        Player player = event.getPlayer();
        BeaconBuilder beaconBuilder = new BeaconBuilder(customBeacon.getAttributes());

        BeaconApi.INSTANCE.getBeaconCache().invalidate(location);
        block.setType(Material.AIR);
        player.getInventory().addItem(beaconBuilder.buildItem()).values().forEach(context -> player.getWorld().dropItemNaturally(player.getLocation(), context));

    }

}
