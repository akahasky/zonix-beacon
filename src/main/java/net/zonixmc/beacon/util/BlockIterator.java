package net.zonixmc.beacon.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BlockIterator implements Iterator<Block> {

    private final World world;
    private final Vector min;
    private final Vector max;
    private final Vector current;

    public BlockIterator(Location loc1, Location loc2) {

        if (loc1.getWorld() != loc2.getWorld())
            throw new IllegalArgumentException("loc1 and loc2 must be in the same world");

        this.world = loc1.getWorld();
        this.min = Vector.getMinimum(loc1.toVector(), loc2.toVector());
        this.max = Vector.getMaximum(loc1.toVector(), loc2.toVector());
        this.current = min.clone().setX(min.getX() - 1);

    }

    @Override
    public boolean hasNext() {
        return current.getY() <= max.getY();
    }

    @Override
    public Block next() {
        if (!hasNext())
            throw new NoSuchElementException("end of iterator");

        skip();
        if (current.getY() > max.getY())
            return world.getBlockAt(max.getBlockX(), max.getBlockY(), max.getBlockZ());

        return world.getBlockAt(current.getBlockX(), current.getBlockY(), current.getBlockZ());
    }

    public int getArea() {
        return (max.getBlockX() - min.getBlockX() + 1) * (max.getBlockY() - min.getBlockY() + 1) * (max.getBlockZ() - min.getBlockZ() + 1);
    }

    private void skip() {
        current.setX(current.getX() + 1);
        if (current.getX() > max.getX()) {
            current.setX(min.getX());
            current.setZ(current.getZ() + 1);
        }

        if (current.getZ() > max.getZ()) {
            current.setZ(min.getZ());
            current.setY(current.getY() + 1);
        }
    }
}