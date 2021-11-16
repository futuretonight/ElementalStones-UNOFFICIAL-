package com.lennertsoffers.elementalstones.stones.earthStone;

import com.lennertsoffers.elementalstones.ElementalStones;
import com.lennertsoffers.elementalstones.customClasses.models.ActivePlayer;
import com.lennertsoffers.elementalstones.customClasses.StaticVariables;
import com.lennertsoffers.elementalstones.customClasses.tools.CheckLocationTools;
import com.lennertsoffers.elementalstones.customClasses.tools.MathTools;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Objects;

public class EarthStone {

    private static void placePillar(Location location) {
        for (int i = 0; i < 3; i++) {
            location.setY(location.getY() + 1.0);
            location.getBlock().setType(Material.STONE);
        }
    }

    // MOVE 1
    // Stone Pillar
    // -> Creates a pillar on the targeted location
    // -> If an entity collides with the pillar it flies up
    // -> The player will not get fall damage when he lands
    public static Runnable move1(ActivePlayer activePlayer) {
        return () -> {
            Player player = activePlayer.getPlayer();
            Block targetBlock = player.getTargetBlockExact(20);
            if (targetBlock != null) {
                World world = player.getWorld();
                Location targetLocation = targetBlock.getLocation();
                Material material = targetBlock.getType();
                Location location = player.getLocation();
                if (targetBlock.getType().isSolid()) {
                    if (
                            world.getBlockAt(targetLocation.clone().add(0, 1, 0)).getType() == Material.AIR &&
                                    world.getBlockAt(targetLocation.clone().add(0, 2, 0)).getType() == Material.AIR &&
                                    world.getBlockAt(targetLocation.clone().add(0, 3, 0)).getType() == Material.AIR
                    ) {
                        for (Entity entity : world.getNearbyEntities(targetLocation, 1, 3, 1)) {
                            entity.setVelocity(new Vector(0, 1, 0));
                        }
                        new BukkitRunnable() {
                            int amountOfBlocks = 0;

                            @Override
                            public void run() {
                                targetLocation.add(0, 1, 0);
                                Material placeMaterial = material;
                                if (
                                        material == Material.DIAMOND_BLOCK ||
                                                material == Material.NETHERITE_BLOCK ||
                                                material == Material.IRON_BLOCK ||
                                                material == Material.GOLD_BLOCK ||
                                                material == Material.BEACON ||
                                                material == Material.EMERALD_BLOCK ||
                                                material == Material.DIAMOND_ORE ||
                                                material == Material.ANCIENT_DEBRIS
                                ) {
                                    placeMaterial = Material.DIRT;
                                }
                                world.getBlockAt(targetLocation).setType(placeMaterial);
                                for (Entity entity : world.getNearbyEntities(targetLocation, 1, 3, 1)) {
                                    entity.setVelocity(new Vector(0, 1, 0));
                                }

                                amountOfBlocks++;
                                if (amountOfBlocks > 2) {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(StaticVariables.plugin, 3L, 1L);
                    }
                }
            }
        };
    }

    // MOVE 2
    // Flying Rock
    // -> The targeted block will fly up a bit
    // -> Primer for moves in the upgraded versions of the stone
    public static Runnable move2(ActivePlayer activePlayer) {
        return () -> {
            Player player = activePlayer.getPlayer();
            Location middlePoint = player.getLocation();
            World world = player.getWorld();

            ArrayList<Location> earthquakeLocations = new ArrayList<>();

            for (int radius = 0; radius < 10; radius++) {
                for (int angle = 0; angle < 360; angle++) {
                    Location newLocation = MathTools.locationOnCircle(middlePoint, radius, angle, world);
                    Location roundedLocation = newLocation.getBlock().getLocation();
                    if (!earthquakeLocations.contains(roundedLocation)) {
                        earthquakeLocations.add(roundedLocation);
                    }
                }
            }

            new BukkitRunnable() {
                int amountOfTicks = 0;
                @Override
                public void run() {
                    for (Location location : earthquakeLocations) {
                        Block highestBlock = world.getHighestBlockAt(location);
                        Location highestBlockLocation = highestBlock.getLocation();
                        for (Entity entity : world.getNearbyEntities(highestBlockLocation.add(0, 1, 0), 1, 1, 1)) {
                            if (entity != null) {
                                if (entity instanceof LivingEntity) {
                                    LivingEntity livingEntity = (LivingEntity) entity;
                                    if (entity == player) {
                                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 1, false, false, false));
                                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400, 1, false, false, true));
                                        livingEntity.damage(1);
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < 30; i++) {
                            double x = highestBlockLocation.getX() + StaticVariables.random.nextInt(10) / 10d;
                            double z = highestBlockLocation.getZ() + StaticVariables.random.nextInt(10) / 10d;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    world.spawnParticle(Particle.BLOCK_CRACK, x, highestBlockLocation.getY(), z, 1, highestBlock.getBlockData());
                                }
                            }.runTaskLater(StaticVariables.plugin, StaticVariables.random.nextInt(9));
                        }
                    }
                    if (amountOfTicks > 6) {
                        this.cancel();
                    }
                    amountOfTicks++;
                }
            }.runTaskTimer(StaticVariables.plugin, 0L, 10L);
        };
    }

    // MOVE 3
    // Bunker
    // -> Hides the player in a bunker under the ground
    public static Runnable move3(ActivePlayer activePlayer) {
        return () -> {
            Player player = activePlayer.getPlayer();
            World world = player.getWorld();
            Location location = player.getLocation();
            for (int i = 0; i < 3; i++) {
                world.getBlockAt(location).setType(Material.AIR);
                world.getBlockAt(location.add(1, -1, 0)).setType(Material.AIR);
                world.getBlockAt(location.add(0, 0, 1)).setType(Material.AIR);
                world.getBlockAt(location.add(-1, 0, 0)).setType(Material.AIR);
                world.getBlockAt(location.add(-1, 0, 0)).setType(Material.AIR);
                world.getBlockAt(location.add(0, 0, -1)).setType(Material.AIR);
                world.getBlockAt(location.add(0, 0, -1)).setType(Material.AIR);
                world.getBlockAt(location.add(1, 0, 0)).setType(Material.AIR);
                world.getBlockAt(location.add(1, 0, 0)).setType(Material.AIR);
                world.getBlockAt(location.add(-1, 0, 1)).setType(Material.AIR);
            }
            world.getBlockAt(location).setType(Material.AIR);
            player.setVelocity(new Vector(0, -10, 0));
            StaticVariables.scheduler.scheduleSyncDelayedTask(StaticVariables.plugin, () -> {
                world.getBlockAt(location.add(0, 2, 0)).setType(Material.SAND);
                world.getBlockAt(location.add(1, 0, 0)).setType(Material.SAND);
                world.getBlockAt(location.add(0, 0, 1)).setType(Material.SAND);
                world.getBlockAt(location.add(-1, 0, 0)).setType(Material.SAND);
                world.getBlockAt(location.add(-1, 0, 0)).setType(Material.SAND);
                world.getBlockAt(location.add(0, 0, -1)).setType(Material.SAND);
                world.getBlockAt(location.add(0, 0, -1)).setType(Material.SAND);
                world.getBlockAt(location.add(1, 0, 0)).setType(Material.SAND);
                world.getBlockAt(location.add(1, 0, 0)).setType(Material.SAND);
            }, 10L);
        };
    }
}
