package com.lennertsoffers.elementalstones;

import com.lennertsoffers.elementalstones.customClasses.StaticVariables;
import com.lennertsoffers.elementalstones.eventHandlers.*;
import com.lennertsoffers.elementalstones.items.CraftItemManager;
import com.lennertsoffers.elementalstones.modMenu.commands.Commands;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import com.lennertsoffers.elementalstones.items.ItemStones;

import java.util.Objects;

public final class ElementalStones extends JavaPlugin {

    public static FileConfiguration configuration;

    @Override
    public void onEnable() {
        configuration = this.getConfig();
        ItemStones.init();
        CraftItemManager.init();
        StaticVariables.staticVariablesInit(this);
        getServer().getPluginManager().registerEvents(new BlockBreakEvent(), this);
        getServer().getPluginManager().registerEvents(new ClickEvent(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityEvent(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageEvent(), this);
        getServer().getPluginManager().registerEvents(new EntityDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new InventoryOpenEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractAtEntityEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerItemHeldEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerToggleFlightEvent(), this);
        getServer().getPluginManager().registerEvents(new PrepareItemCraftEvent(), this);
        getServer().getPluginManager().registerEvents(new VillagerCareerChangeEvent(), this);
        getServer().getPluginManager().registerEvents(new FallingBlockToBlockEvent(), this);

        // Commands
        Objects.requireNonNull(this.getCommand("r")).setExecutor(new Commands());
        Objects.requireNonNull(this.getCommand("giveStone")).setExecutor(new Commands());
        Objects.requireNonNull(this.getCommand("stoneInventory")).setExecutor(new Commands());
        Objects.requireNonNull(this.getCommand("giveItem")).setExecutor(new Commands());
        Objects.requireNonNull(this.getCommand("shamanMajor")).setExecutor(new Commands());
        Objects.requireNonNull(this.getCommand("saveDefaultConfig")).setExecutor(new Commands());
    }

    @Override
    public void onDisable() {

    }
}
