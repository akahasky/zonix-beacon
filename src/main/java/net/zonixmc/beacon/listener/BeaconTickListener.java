package net.zonixmc.beacon.listener;

import net.zonixmc.beacon.BeaconApi;
import net.zonixmc.beacon.attributes.Attribute;
import net.zonixmc.beacon.event.BeaconTickEvent;
import net.zonixmc.beacon.model.CustomBeacon;
import net.zonixmc.beacon.util.CoordinateUtil;
import net.zonixmc.factions.api.model.ILand;
import net.zonixmc.factions.api.model.IUser;
import net.zonixmc.factions.land.manager.LandManager;
import net.zonixmc.factions.user.manager.FUserManager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeaconTickListener implements Listener {

    @EventHandler
    void on(BeaconTickEvent event) {

        World world = event.getWorld();
        Map<Long, CustomBeacon> beacons = event.getCustomBeacons();

        beacons.forEach(((locationKey, customBeacon) -> {

            if (!customBeacon.isActive()) return;

            int locationX = CoordinateUtil.getLocationX(locationKey);
            int locationZ = CoordinateUtil.getLocationZ(locationKey);

            Chunk chunk = world.getChunkAt(locationX >> 4, locationZ >> 4);

            if (!chunk.isLoaded()) return;

            Location location = new Location(world, locationX, CoordinateUtil.getLocationY(locationKey), locationZ);
            Block blockAt = world.getBlockAt(location);

            if (blockAt.getType() != Material.BEACON) {

                BeaconApi.INSTANCE.getBeaconCache().invalidate(location);
                return;

            }

            Set<Attribute> attributes = customBeacon.getActiveEffects().stream().map(Attribute::getByOrdinal).collect(Collectors.toSet());

            attributes.removeIf(context -> !context.hasEffect());

            if (attributes.isEmpty()) return;

            int range = 8 * customBeacon.getAttributeLevel(Attribute.RANGE);

            ILand land = LandManager.getInstance().get(CoordinateUtil.getChunkKey(chunk.getX(), chunk.getZ()));
            Set<Player> nearbyPlayers = world.getNearbyEntities(location, range, range, range).stream().filter(entity -> entity instanceof Player).map(context -> (Player) context).collect(Collectors.toSet());

            if (land != null) {

                nearbyPlayers.removeIf(context -> {

                    IUser user = FUserManager.getInstance().getByName(context.getName());

                    return user == null || !land.getFactionTag().equals(user.getFactionTag());

                });

            }

            attributes.forEach(attribute -> nearbyPlayers.forEach(player -> {

                player.removePotionEffect(attribute.getEffectType());
                player.addPotionEffect(new PotionEffect(attribute.getEffectType(), 20 * 12, customBeacon.getAttributeLevel(attribute) - 1));

            }));

        }));
    }

}
