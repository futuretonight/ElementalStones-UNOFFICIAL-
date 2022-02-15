package com.lennertsoffers.elementalstones.moves.fireMoves.hellfire;

import com.lennertsoffers.elementalstones.customClasses.StaticVariables;
import com.lennertsoffers.elementalstones.customClasses.models.ActivePlayer;
import com.lennertsoffers.elementalstones.customClasses.models.bukkitRunnables.FireFlamethrower;
import com.lennertsoffers.elementalstones.moves.Move;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Flamethrower extends Move {

    public Flamethrower(ActivePlayer activePlayer) {
        super(activePlayer, "Flamethrower", "fire_stone", "hellfire_stone", 7);
    }

    @Override
    public void useMove() {
        Player player = this.getPlayer();
        World world = player.getWorld();

        FireFlamethrower fireFlamethrower = new FireFlamethrower(player, world);
        fireFlamethrower.runTaskTimer(StaticVariables.plugin, 0L, 1L);

        this.setCooldown();
    }
}
