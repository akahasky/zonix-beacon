package net.zonixmc.beacon.cache;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import lombok.Getter;
import net.zonixmc.beacon.BeaconApi;
import net.zonixmc.beacon.dao.BeaconDAO;
import net.zonixmc.beacon.event.BeaconTickEvent;
import net.zonixmc.beacon.model.CustomBeacon;
import net.zonixmc.beacon.util.CoordinateUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
public class BeaconCache {

    private final BiMap<Long, CustomBeacon> customBeacons = HashBiMap.create();

    public BeaconCache(BeaconDAO beaconDAO) {

        customBeacons.putAll(beaconDAO.getAllBeacons());

        Bukkit.getScheduler().runTaskTimer(BeaconApi.INSTANCE.getPlugin(), () ->
                        Bukkit.getPluginManager().callEvent(new BeaconTickEvent(Maps.newHashMap(customBeacons))), 20 * 10, 20 * 10);

    }

    public void put(Location location, CustomBeacon customBeacon) {

        long locationKey = CoordinateUtil.getBlockKey(location);

        customBeacons.put(locationKey, customBeacon);
        BeaconApi.INSTANCE.getBeaconDAO().saveBeacon(locationKey, customBeacon, true);

    }

    public void invalidate(Location location) {

        long locationKey = CoordinateUtil.getBlockKey(location);

        customBeacons.remove(locationKey);
        BeaconApi.INSTANCE.getBeaconDAO().deleteBeacon(locationKey);

    }

    public CustomBeacon getValue(Location location) { return customBeacons.get(CoordinateUtil.getBlockKey(location)); }

    public Long getKey(CustomBeacon customBeacon) { return customBeacons.inverse().get(customBeacon); }

}
