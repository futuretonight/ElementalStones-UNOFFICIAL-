package com.lennertsoffers.elementalstones.stones.fireStone;

import com.lennertsoffers.elementalstones.customClasses.ActivePlayer;
import com.lennertsoffers.elementalstones.customClasses.StaticVariables;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public class FireStone {

    // MOVE 1
    // A-Quick-Snack
    // -> Turns every rawww stack of food to the cooked version of it
    public static void move1(ActivePlayer activePlayer) {
        Player player = activePlayer.getPlayer();
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null) {
                Material material = itemStack.getType();
                if (material == Material.BEEF) {
                    itemStack.setType(Material.COOKED_BEEF);
                } else if (material == Material.PORKCHOP) {
                    itemStack.setType(Material.COOKED_PORKCHOP);
                } else if (material == Material.SALMON) {
                    itemStack.setType(Material.COOKED_SALMON);
                } else if (material == Material.MUTTON) {
                    itemStack.setType(Material.COOKED_MUTTON);
                } else if (material == Material.POTATO) {
                    itemStack.setType(Material.BAKED_POTATO);
                } else if (material == Material.CHICKEN) {
                    itemStack.setType(Material.COOKED_CHICKEN);
                } else if (material == Material.COD) {
                    itemStack.setType(Material.COOKED_COD);
                } else if (material == Material.RABBIT) {
                    itemStack.setType(Material.COOKED_RABBIT);
                }
            }
        }
    }

    // MOVE 2
    // Floating Fire
    // -> You can summon a fireball and hold it right in front of you
    public static void move2(ActivePlayer activePlayer) {

    }

    // MOVE 3
    // Fire-fly
    // -> You get trusted in the looking direction of the player
    public static void move3(ActivePlayer activePlayer) {
        Player player = activePlayer.getPlayer();
        World world = player.getWorld();
        Random random = new Random();
        player.setGliding(true);
        new BukkitRunnable() {
            int amountOfTicks = 0;
            @Override
            public void run() {
                Location location = player.getLocation();
                Vector direction = location.getDirection();
                player.setVelocity(new Vector(direction.getX(), direction.getY(), direction.getZ()));
                player.setGliding(true);
                for (int i = 0; i < 20; i++) {
                    world.spawnParticle(Particle.FLAME, location, 0, random.nextDouble() / 10, random.nextDouble() / 10, random.nextDouble() / 10);
                }
                amountOfTicks++;
                if (amountOfTicks > 80) {
                    player.setGliding(false);
                    this.cancel();
                }
            }
        }.runTaskTimer(StaticVariables.plugin, 0L, 1L);
        new BukkitRunnable() {
            @Override
            public void run() {
                Location location = player.getLocation();
                if (!(player.getLocation().getY() > world.getHighestBlockYAt(location) + 4)) {
                    player.setVelocity(new Vector(0, 0, 0));
                    player.setFallDistance(0);
                    for (int i = 0; i < 50; i++) {
                        world.spawnParticle(Particle.FLAME, location, 0, random.nextDouble() / 8, random.nextDouble() / 8, random.nextDouble() / 8);
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(StaticVariables.plugin, 80, 1L);
    }
}





























