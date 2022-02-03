package com.lennertsoffers.elementalstones.eventHandlers;

import com.lennertsoffers.elementalstones.customClasses.models.ActivePlayer;
import com.lennertsoffers.elementalstones.customClasses.models.bukkitRunnables.Comet;
import com.lennertsoffers.elementalstones.stones.earthStone.EarthbendingStone;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class FallingBlockToBlockEvent implements Listener {

    @EventHandler
    public void entityChangeBlock(EntityChangeBlockEvent e) {

        if (e.getEntity() instanceof FallingBlock) {
            FallingBlock fallingBlock = (FallingBlock) e.getEntity();

            for (ActivePlayer activePlayer : ActivePlayer.getActivePlayers()) {
                if (activePlayer.getMove6LaunchedFallingBlocks().contains(fallingBlock)) {
                    e.setCancelled(true);
                    activePlayer.getMove6LaunchedFallingBlocks().remove(fallingBlock);
                } else if (activePlayer.getCometFallingBlocks().contains(fallingBlock)) {
                    e.setCancelled(true);
                    activePlayer.getComet().endComet(fallingBlock.getLocation());
                }

                if (activePlayer.isMove8active()) {
                    if (!activePlayer.getMove8FallingBlocks().isEmpty()) {
                        if (activePlayer.getMove8FallingBlocks().contains(fallingBlock)) {
                            e.setCancelled(true);
                            activePlayer.clearMove8FallingBlocks();
                            EarthbendingStone.move8End(activePlayer);
                        }
                    }
                }
            }
        }
    }
}