package com.lennertsoffers.elementalstones.customClasses;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Tools {

    public static boolean checkPlayerCollision(double player, double block) {
        return !(player > block + 1.3 || player < block - 0.3);
    }

    public static double lengthOfVector(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static double lengthOfVector(double x1, double x2, double y1, double y2, double z1, double z2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
    }

    public static Vector directionOfVector(Location a, Location b) {
        double dX = a.getX() - b.getX();
        double dY = a.getY() - b.getY();
        double dZ = a.getZ() - b.getZ();
        double yaw = Math.atan2(dZ, dX);
        double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
        double x = Math.sin(pitch) * Math.cos(yaw);
        double y = Math.sin(pitch) * Math.sin(yaw);
        double z = Math.cos(pitch);
        return new Vector(x, z, y);
    }

    public static Location locationOnCircle(Location location, double radius, double angle, World world) {
        double particleX = location.getX() + radius * Math.cos(angle);
        double particleZ = location.getZ() + radius * Math.sin(angle);
        return new Location(world, particleX, location.getY() , particleZ);
    }

    public static boolean locationAroundClear(Location location, World world) {
        return world.getBlockAt(location.add(0, 1, 0)).getType() == Material.AIR &&
                world.getBlockAt(location.add(1, 0, 0)).getType() == Material.AIR &&
                world.getBlockAt(location.add(0, 0, 1)).getType() == Material.AIR &&
                world.getBlockAt(location.add(-1, 0, 0)).getType() == Material.AIR &&
                world.getBlockAt(location.add(-1, 0, 0)).getType() == Material.AIR &&
                world.getBlockAt(location.add(0, 0, -1)).getType() == Material.AIR &&
                world.getBlockAt(location.add(0, 0, -1)).getType() == Material.AIR &&
                world.getBlockAt(location.add(1, 0, 0)).getType() == Material.AIR &&
                world.getBlockAt(location.add(1, 0, 0)).getType() == Material.AIR;
    }

    public static boolean lavaAroundPlayer(Location location) {
        World world = location.getWorld();
        location.add(1, 0, 1);
        for (int i = 1; i <= 9; i++) {
            if (Objects.requireNonNull(world).getBlockAt(location).getType() == Material.LAVA) {
                return true;
            }
            location.add(-1, 0, 0);
            if (i % 3 == 0) {
                location.add(3, 0, -1);
            }
        }
        return false;
    }

    public static void setBlocks(Location playerLocation, String[] stringList, Map<Character, Material> characterMaterialMap, boolean onlyFillAir, ArrayList<Material> overrideBlocks) {
        int columnLocation = 0;
        int rowLocation = 0;
        for (int row = 0; row < stringList.length; row++) {
            for (int column = 0; column < stringList[0].length(); column++) {
                if (stringList[row].charAt(column) == '*') {
                    columnLocation = column;
                    rowLocation = row;
                }
            }
        }
        Location startingLocation = playerLocation.clone().add(-rowLocation, 0, -columnLocation);
        for (int row = 0; row < stringList.length; row++) {
            String string = stringList[row];
            for (int column = 0; column < stringList[0].length(); column++) {
                Location blockLocation = startingLocation.clone().add(row, 0, column);
                Material material = Objects.requireNonNull(playerLocation.getWorld()).getBlockAt(blockLocation).getType();
                if ((material == Material.AIR || !onlyFillAir) || (overrideBlocks.contains(material))) {
                    if (string.charAt(column) == '?' || string.charAt(column) == '*') {
                        Objects.requireNonNull(playerLocation.getWorld()).getBlockAt(blockLocation).setType(material);
                    } else {
                        try {
                            Objects.requireNonNull(playerLocation.getWorld()).getBlockAt(blockLocation).setType(characterMaterialMap.get(string.charAt(column)));
                        } catch (IllegalArgumentException e) {
                            System.out.println(string.charAt(column));
                        }
                    }
                }
            }
        }
    }



}

























