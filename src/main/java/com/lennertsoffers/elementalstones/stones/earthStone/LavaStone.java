package com.lennertsoffers.elementalstones.stones.earthStone;

import com.lennertsoffers.elementalstones.customClasses.models.ActivePlayer;
import com.lennertsoffers.elementalstones.customClasses.StaticVariables;
import com.lennertsoffers.elementalstones.customClasses.models.bukkitRunnables.Comet;
import com.lennertsoffers.elementalstones.customClasses.models.bukkitRunnables.LavaWave;
import com.lennertsoffers.elementalstones.customClasses.tools.CheckLocationTools;
import com.lennertsoffers.elementalstones.customClasses.tools.MathTools;
import com.lennertsoffers.elementalstones.items.ItemStones;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class LavaStone extends EarthStone {

    // PASSIVE
    // Passive1: Lava Walker
    public static void passive1(ActivePlayer activePlayer) {
        Player player = activePlayer.getPlayer();
        World world = player.getWorld();
        Location location = player.getLocation().add(0, -1, 0);
        ArrayList<Location> locationGroup = new ArrayList<>();
        if (CheckLocationTools.lavaAroundPlayer(location)) {
            location.add(2, 0, 2);
            Location startLocation = location.clone();
            for (int i = 1; i <= 25; i++) {
                if (!activePlayer.isInLavaBlockLocations(location)) {
                    locationGroup.add(location.clone());
                    if (
                            !(startLocation.getX() == location.getX() && startLocation.getZ() == location.getZ()) &&
                                    !(startLocation.getX() == location.getX() && startLocation.getZ() - 4 == location.getZ()) &&
                                    !(startLocation.getX() - 4 == location.getX() && startLocation.getZ() == location.getZ()) &&
                                    !(startLocation.getX() - 4 == location.getX() && startLocation.getZ() - 4 == location.getZ())
                    ) {
                        world.getBlockAt(location).setType(Material.BASALT);
                    }
                }
                location.add(-1, 0, 0);
                if (i % 5 == 0) {
                    location.add(5, 0, -1);
                }
            }
        }
        activePlayer.setRemoveBasald(new BukkitRunnable() {
            int blocksRemoved = 1;
            @Override
            public void run() {
                if (locationGroup.size() >= 1) {
                    int index = StaticVariables.random.nextInt(Math.abs(locationGroup.size()));
                    world.getBlockAt(locationGroup.get(index)).setType(Material.LAVA);
                    locationGroup.remove(index);
                }
                if (blocksRemoved >= 25) {
                    this.cancel();
                }
                blocksRemoved++;
            }
        });
    }

    // Passive 2: Magma Master
    public static void passive2(ActivePlayer activePlayer, EntityDamageEvent event) {
        if (!Collections.disjoint(Arrays.asList(activePlayer.getPlayer().getInventory().getContents()), ItemStones.lavaStones)) {
            if (event.getCause() == EntityDamageEvent.DamageCause.HOT_FLOOR) {
                event.setCancelled(true);
            }
        }
    }

    // MOVE 4
    // Reverse Logic
    // -> The player heals over time while standing on magma blocks
    public static Runnable move4(ActivePlayer activePlayer) {
        return () -> {
            Player player = activePlayer.getPlayer();
            World world = player.getWorld();
            new BukkitRunnable() {
                int amountOfTicks = 0;

                @Override
                public void run() {
                    Location location = player.getLocation().add(0, 1, 0);
                    for (int i = 0; i < 2; i++) {
                        player.getWorld().spawnParticle(Particle.REDSTONE, location.getX() + StaticVariables.random.nextGaussian() / 3, location.getY() + StaticVariables.random.nextGaussian() / 3, location.getZ() + StaticVariables.random.nextGaussian() / 3, 0, 0, 0, 0, new Particle.DustOptions(Color.RED, 1));
                    }
                    if (amountOfTicks >= 199) {
                        this.cancel();
                    }
                    amountOfTicks++;
                }
            }.runTaskTimer(StaticVariables.plugin, 0L, 1L);
            new BukkitRunnable() {
                int amountOfSecs = 0;

                @Override
                public void run() {
                    Location location = player.getLocation();
                    if (world.getBlockAt(location.add(0, -1, 0)).getType() == Material.MAGMA_BLOCK) {
                        player.setHealth(player.getHealth() + 3);
                    }
                    if (amountOfSecs >= 9) {
                        this.cancel();
                    }
                    amountOfSecs++;
                }
            }.runTaskTimer(StaticVariables.plugin, 0L, 20L);
        };
    }

    /**
     * <b>MOVE 5: Lava Wave</b>
     * <p>
     *     Creates a wave of lava in the looking direction
     *     <ul>
     *         <li><b>Damage:</b> 40</li>
     *         <li><b>Range: </b> 50</li>
     *     </ul>
     * </p>
     *
     * @param activePlayer the activeplayer executing the move
     * @return a BukkitRunnable that can be executed as move
     * @see LavaWave
     */
    public static Runnable move5(ActivePlayer activePlayer) {
        return () -> {
            Player player = activePlayer.getPlayer();
            Location playerLocation = player.getLocation();

            float yaw = playerLocation.getYaw();
            if (yaw > -25 && yaw < 25) {
                new LavaWave(activePlayer, true, true, true).runTaskTimer(StaticVariables.plugin, 0L, 1L);
            } else if (yaw >= 25 && yaw < 65) {
                new LavaWave(activePlayer, false, true, false).runTaskTimer(StaticVariables.plugin, 0L, 1L);
            } else if (yaw >= 65 && yaw < 115) {
                new LavaWave(activePlayer, true, false, true).runTaskTimer(StaticVariables.plugin, 0L, 1L);
            } else if (yaw >= 115 && yaw < 155) {
                new LavaWave(activePlayer, false, false, true).runTaskTimer(StaticVariables.plugin, 0L, 1L);
            } else if (yaw < -155 || yaw > 155) {
                new LavaWave(activePlayer, true, false, false).runTaskTimer(StaticVariables.plugin, 0L, 1L);
            } else if (yaw <= -25 && yaw > -65) {
                new LavaWave(activePlayer, false, true, true).runTaskTimer(StaticVariables.plugin, 0L, 1L);
            } else if (yaw <= -65 && yaw > -115) {
                new LavaWave(activePlayer, true, true, false).runTaskTimer(StaticVariables.plugin, 0L, 1L);
            } else {
                new LavaWave(activePlayer, false, false, false).runTaskTimer(StaticVariables.plugin, 0L, 1L);
            }
        };
    }

    /**
     * <b>MOVE 6: Comet</b>
     * <p>
     *     Marks a spot where a comet wil land after a few seconds
     *     <ul>
     *         <li><b>Range: </b> 30</li>
     *         <li><b>Explosion Power: </b>5</li>
     *     </ul>
     * </p>
     *
     * @param activePlayer the activeplayer executing the move
     * @return a BukkitRunnable that can be executed as move
     * @see Comet
     */
    public static Runnable move6(ActivePlayer activePlayer) {
        return () -> {
            Player player = activePlayer.getPlayer();
            World world = player.getWorld();
            Block targetBlock = player.getTargetBlockExact(30);
            if (targetBlock != null) {
                Location targetBlockLocation = targetBlock.getLocation();

                new BukkitRunnable() {
                    int amountOfTicks = 0;
                    
                    @Override
                    public void run() {
                        for (int i = 0; i < 360; i++) {
                            Location particleLocation = CheckLocationTools.getClosestAirBlockLocation(MathTools.locationOnCircle(targetBlockLocation, 3, i, world));

                            if (particleLocation != null) {
                                world.spawnParticle(Particle.FLAME, particleLocation, 0);
                            }
                        }

                        spawnTriangle(targetBlockLocation, Arrays.asList(0, 90, 180));
                        spawnTriangle(targetBlockLocation, Arrays.asList(45, 135, 225));

                        if (amountOfTicks > 50) {
                            this.cancel();
                        }

                        System.out.println(amountOfTicks);
                        amountOfTicks += 5;
                    }
                }.runTaskTimer(StaticVariables.plugin, 0L, 5L);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location startLocation;
                        Location endLocation;

                        int randomInt = StaticVariables.random.nextInt(4);

                        if (randomInt == 0) {
                            startLocation = targetBlockLocation.clone().add(0.5, 100, 40.5);
                            endLocation = targetBlockLocation.clone().add(0.5, 0, -2.5);
                        } else if (randomInt == 1) {
                            startLocation = targetBlockLocation.clone().add(0.5, 100, -40.5);
                            endLocation = targetBlockLocation.clone().add(0.5, 0, 0.5);
                        } else if (randomInt == 2) {
                            startLocation = targetBlockLocation.clone().add(40.5, 100, 0.5);
                            endLocation = targetBlockLocation.clone().add(-1.5, 0, -1.5);
                        } else {
                            startLocation = targetBlockLocation.clone().add(-40.5, 100, 0.5);
                            endLocation = targetBlockLocation.clone().add(0.5, 0, -1.5);
                        }

                        Comet comet = new Comet(activePlayer, startLocation, endLocation);
                        comet.runTaskTimer(StaticVariables.plugin, 0L, 1L);
                    }
                }.runTaskLater(StaticVariables.plugin, 40L);
            }
        };
    }

    /**
     * <b>Spawns a line of particles connecting 2 points on a circle with a certain angle</b>
     * @param center the center of the circle
     * @param angles the angle at which the line must spawn
     */
    private static void spawnTriangle(Location center, List<Integer> angles) {
        World world = center.getWorld();

        if (world != null) {
            Location loc1 = MathTools.locationOnCircle(center, 3, angles.get(0), world);
            Location loc2 = MathTools.locationOnCircle(center, 3, angles.get(1), world);
            Location loc3 = MathTools.locationOnCircle(center, 3, angles.get(2), world);

            Vector loc1loc2 = new Vector(loc2.getX() - loc1.getX(), 0, loc2.getZ() - loc1.getZ()).multiply(0.05);
            Vector loc1loc3 = new Vector(loc3.getX() - loc1.getX(), 0, loc3.getZ() - loc1.getZ()).multiply(0.05);
            Vector loc2loc3 = new Vector(loc3.getX() - loc2.getX(), 0, loc3.getZ() - loc2.getZ()).multiply(0.05);

            for (double i = 0; i < 20; i += 0.1) {
                Location particleLocation1 = CheckLocationTools.getClosestAirBlockLocation(loc1.clone().add(loc1loc2.clone().multiply(i)));
                Location particleLocation2 = CheckLocationTools.getClosestAirBlockLocation(loc1.clone().add(loc1loc3.clone().multiply(i)));
                Location particleLocation3 = CheckLocationTools.getClosestAirBlockLocation(loc2.clone().add(loc2loc3.clone().multiply(i)));

                if (particleLocation1 != null) {
                    world.spawnParticle(Particle.FLAME, particleLocation1, 0);
                }

                if (particleLocation2 != null) {
                    world.spawnParticle(Particle.FLAME, particleLocation2, 0);
                }

                if (particleLocation3 != null) {
                    world.spawnParticle(Particle.FLAME, particleLocation3, 0);
                }
            }
        }
    }

    // MOVE 7
    // Lava Burst
    // -> The blocks where the player is looking at burst open creating an intense flow of lava
    public static Runnable move7(ActivePlayer activePlayer) {
        return () -> {
            Player player = activePlayer.getPlayer();
            Location middlePoint = player.getLocation();

        };
    }

    // MOVE 8
    // Lava Rider
    // -> The player rides on a sphere of lava
    // -> Increased movement speed
    // -> Damages entities if they get caught by the lava
    public static Runnable move8(ActivePlayer activePlayer) {
        return () -> {
//            Player player = activePlayer.getPlayer();
//            World world = player.getWorld();
//            activePlayer.setLavaStoneMove8Active(true);
//            player.setAllowFlight(true);
//            player.setFlying(true);
//            player.setFlySpeed(0.075f);
//            player.teleport(player.getLocation().add(0, 2, 0));
//            String[] lavaLevel0 = {
//                    "AAAAAAAAA",
//                    "AAALLLAAA",
//                    "AALLLLLAA",
//                    "ALLLLLLLA",
//                    "ALLL*LLLA",
//                    "ALLLLLLLA",
//                    "AALLLLLAA",
//                    "AAALLLAAA",
//                    "AAAAAAAAA"
//            };
//            String[] lavaLevel1 = {
//                    "AAAAAAA",
//                    "AALLLAA",
//                    "ALLLLLA",
//                    "ALL*LLA",
//                    "ALLLLLA",
//                    "AALLLAA",
//                    "AAAAAAA"
//            };
//            String[] lavaLevel2 = {
//                    "AAAAA",
//                    "AALAA",
//                    "AL*LA",
//                    "AALAA",
//                    "AAAAA",
//            };
//            String[] lavaRemoveString = {
//                    "AAAAAAAAA",
//                    "AAAAAAAAA",
//                    "AAAAAAAAA",
//                    "AAAAAAAAA",
//                    "AAAA*AAAA",
//                    "AAAAAAAAA",
//                    "AAAAAAAAA",
//                    "AAAAAAAAA",
//                    "AAAAAAAAA"
//            };
//            ArrayList<Material> overrideBlocks = new ArrayList<>();
//            overrideBlocks.add(Material.LAVA);
//            Map<Character, Material> characterMaterialMap = new HashMap<>();
//            characterMaterialMap.put('A', Material.AIR);
//            characterMaterialMap.put('L', Material.LAVA);
//
//            new BukkitRunnable() {
//                int amountOfTicks = 0;
//                Location previousLocation = player.getLocation();
//
//                @Override
//                public void run() {
//                    boolean placeTopLevel = true;
//                    if (world.getHighestBlockYAt(player.getLocation(), HeightMap.OCEAN_FLOOR) + 3 != player.getLocation().getY()) {
//                        if (world.getHighestBlockYAt(player.getLocation(), HeightMap.OCEAN_FLOOR) + 3 < player.getLocation().getY()) {
//                            placeTopLevel = false;
//                        }
//                        Location teleportLocation = player.getLocation();
//                        teleportLocation.setY(world.getHighestBlockYAt(player.getLocation(), HeightMap.OCEAN_FLOOR) + 3);
//                        player.teleport(teleportLocation);
//                        new BukkitRunnable() {
//                            @Override
//                            public void run() {
//                                Vector direction = player.getLocation().getDirection();
//                                direction.setX(direction.getX() / 3);
//                                direction.setY(0);
//                                direction.setZ(direction.getZ() / 3);
//                                player.setVelocity(direction);
//                            }
//                        }.runTaskLater(StaticVariables.plugin, 1L);
//                    }
//                    for (int i = 1; i >= -3; i--) {
//                        SetBlockTools.setBlocks(previousLocation.clone().add(0, i, 0), lavaRemoveString, characterMaterialMap, true, overrideBlocks, Material.AIR, activePlayer);
//                    }
//                    SetBlockTools.setBlocks(player.getLocation().clone().add(0, -3, 0), lavaRemoveString, characterMaterialMap, true, overrideBlocks, Material.AIR, activePlayer);
//                    SetBlockTools.setBlocks(player.getLocation().clone().add(0, -2, 0), lavaLevel0, characterMaterialMap, true, overrideBlocks, Material.LAVA, activePlayer);
//                    SetBlockTools.setBlocks(player.getLocation().clone().add(0, -1, 0), lavaLevel1, characterMaterialMap, true, overrideBlocks, Material.LAVA, activePlayer);
//                    if (placeTopLevel) {
//                        SetBlockTools.setBlocks(player.getLocation(), lavaLevel2, characterMaterialMap, true, overrideBlocks, Material.LAVA, activePlayer);
//                    }
//                    SetBlockTools.setBlocks(player.getLocation().clone().add(0, 1, 0), lavaRemoveString, characterMaterialMap, true, overrideBlocks, Material.AIR, activePlayer);
//                    previousLocation = player.getLocation().clone();
//                    if (amountOfTicks > 400) {
//                        player.setAllowFlight(false);
//                        player.setFlying(false);
//                        player.setFireTicks(0);
//                        activePlayer.setLavaStoneMove8Active(false);
//                        this.cancel();
//                        for (int i = 0; i >= -2; i--) {
//                            SetBlockTools.setBlocks(player.getLocation().clone().add(0, i, 0), lavaRemoveString, characterMaterialMap, true, overrideBlocks, Material.AIR, activePlayer);
//                        }
//                    }
//                    amountOfTicks++;
//                }
//            }.runTaskTimer(StaticVariables.plugin, 0L, 1L);
        };
    }

    // MOVE 8: Prevent player from getting fire damage
    public static void move8(ActivePlayer activePlayer, EntityDamageEvent event) {
        if (activePlayer.isLavaStoneMove8Active()) {
            if (event.getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.LAVA || event.getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.FIRE) {
                event.setCancelled(true);
            }
        }
    }
}