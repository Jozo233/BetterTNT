package me.bettertnt.me.jozo.Me;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import static org.bukkit.Bukkit.getBukkitVersion;

public final class BetterTNT extends JavaPlugin implements Listener {
    private final boolean legacyVersion = getBukkitVersion() < 13;
    @Override
    public void onEnable() {
        // Plugin startup logic
        Server server = Bukkit.getServer();
        PluginManager pluginManager = server.getPluginManager();
        CommandSender commandSender = server.getConsoleSender();

        pluginManager.registerEvents(this, this);
        commandSender.sendMessage("[BetterTNT] I start .");
        commandSender.sendMessage("[BetterTNT] I love all mi plugin");
    }

    private int getBukkitVersion() {
        return Integer.parseInt(getServer().getBukkitVersion()
                .split("\\.")[1]
                .split("\\.")[0]
        );
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityExplode(EntityExplodeEvent entityExplodeEvent) {
        if (entityExplodeEvent.isCancelled()) {
            return;
        }

        for (Block block : entityExplodeEvent.blockList()) {
            float x = (float) ((float) -1 + Math.random() * 2.5);
            float y = (float) ((float) -2 + Math.random() * 3.5);
            float z = (float) ((float) -1 + Math.random() * 2.5);

            World world = block.getWorld();
            Location location = block.getLocation();

            if (block.getType().equals(Material.TNT)) {
                TNTPrimed tntPrimed = (TNTPrimed) block.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
                tntPrimed.setVelocity(new Vector(x, y, z));
                tntPrimed.setFuseTicks(10);
            } else {
                FallingBlock fallingBlock = (legacyVersion)
                        ? world.spawnFallingBlock(location, block.getType(), block.getData())
                        : world.spawnFallingBlock(location, block.getBlockData());
                fallingBlock.setVelocity(new Vector(x, y, z));
                fallingBlock.setDropItem(false);

                if (!legacyVersion) {
                    fallingBlock.setHurtEntities(true);
                }
            }
            block.setType(Material.AIR);
        }
    }
}