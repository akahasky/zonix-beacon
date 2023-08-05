package net.zonixmc.beacon.listener;

import net.zonixmc.beacon.BeaconApi;
import net.zonixmc.beacon.builder.BeaconBuilder;
import net.zonixmc.beacon.attributes.Attribute;
import net.zonixmc.beacon.model.CustomBeacon;
import net.zonixmc.beacon.util.BlockIterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BeaconExplodeListener implements Listener {

    @EventHandler
    void on(EntityExplodeEvent event) {

        if (event.blockList().stream().noneMatch(context -> context.getType() == Material.BEACON)) return;

        Location location = event.getLocation();
        BlockIterator blockIterator = new BlockIterator(location.clone().subtract(1.0, 1.0, 1.0), location.clone().add(1.0, 1.0, 1.0));

        while (blockIterator.hasNext()) {

            Block block = blockIterator.next();

            if (block.getType() != Material.BEACON) continue;

            CustomBeacon customBeacon = BeaconApi.INSTANCE.getBeaconCache().getValue(block.getLocation());

            if (customBeacon == null) continue;

            int durability = customBeacon.getAttributeLevel(Attribute.DURABILITY);

            if (durability <= 1) {

                block.setType(Material.AIR);
                location.getWorld().dropItemNaturally(block.getLocation(), new BeaconBuilder(customBeacon.getAttributes()).buildItem());
                BeaconApi.INSTANCE.getBeaconCache().invalidate(block.getLocation());
                return;

            }

            customBeacon.putAttribute(Attribute.DURABILITY, durability - 1);
            BeaconApi.INSTANCE.getBeaconDAO().saveBeacon(BeaconApi.INSTANCE.getBeaconCache().getKey(customBeacon), customBeacon, true);

        }

        event.blockList().clear();

    }

}
