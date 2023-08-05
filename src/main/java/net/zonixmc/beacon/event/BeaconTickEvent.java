package net.zonixmc.beacon.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.zonixmc.beacon.model.CustomBeacon;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class BeaconTickEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    private final World world = Bukkit.getWorlds().get(0);
    private final Map<Long, CustomBeacon> customBeacons;

    @Override
    public HandlerList getHandlers() { return handlerList; }
}
