package net.zonixmc.beacon.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class CoordinateUtil {

    public static long getChunkKey(int x, int z) { return ((long) z << 32) | (x & 0xFFFFFFFFL); }

    public static long getBlockKey(Location location) { return ((long) location.getBlockX() & 0x7FFFFFF) | (((long) location.getBlockZ() & 0x7FFFFFF) << 27) | ((long) location.getBlockY() << 54); }

    public static int getLocationX(long blockKey) { return (int) (blockKey & 0x7FFFFFF); }

    public static int getLocationY(long blockKey) { return (int) (int) (blockKey >> 54); }

    public static int getLocationZ(long blockKey) { return (int) ((blockKey >> 27) & 0x7FFFFFF); }

    public static boolean checkPyramid(Location location) {

        int range = 1;

        for (double targetY = (location.getY() - 1D); targetY > (location.getY() - 5D); targetY--) {

            for (int x = location.getBlockX() - range; x <= location.getBlockX() + range; x++) {

                for (int z = location.getBlockZ() - range; z <= location.getBlockZ() + range; z++) {

                    Material material = location.getWorld().getBlockAt(x, (int) targetY, z).getType();

                    if (!(material.equals(Material.IRON_BLOCK) || material.equals(Material.GOLD_BLOCK)
                            || material.equals(Material.DIAMOND_BLOCK) || material.equals(Material.EMERALD_BLOCK))) return false;

                }

            }

            range++;
        }

        return true;

    }

}