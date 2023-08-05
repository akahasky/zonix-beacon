package net.zonixmc.beacon.listener;

import net.zonixmc.beacon.BeaconApi;
import net.zonixmc.beacon.builder.BeaconBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BeaconPlaceListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    void on(BlockPlaceEvent event) {

        Block block = event.getBlockPlaced();

        if (block.getType() != Material.BEACON) return;

        ItemStack itemInHand = event.getItemInHand();

        if (itemInHand == null || itemInHand.getType() != Material.BEACON) return;

        if (event.getPlayer().getWorld().getUID() != Bukkit.getWorlds().get(0).getUID()) {

            event.setCancelled(true);
            return;

        }

        ItemMeta itemMeta = itemInHand.getItemMeta();

        if (itemMeta == null
                || !itemMeta.hasDisplayName() || !itemMeta.hasLore()) return;

        BeaconApi.INSTANCE.getBeaconCache().put(block.getLocation(), new BeaconBuilder(itemInHand).buildBeacon());

    }

}
