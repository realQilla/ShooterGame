package net.qilla.shootergame.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

public class BlockManagement {

    public static void setBlock(Material blockType, Location blockLoc, TriSound triSound) {
        blockLoc.getBlock().setType(blockType);
        blockLoc.getWorld().playSound(blockLoc, triSound.sound(), triSound.volume(), triSound.pitch());
        blockLoc.getWorld().spawnParticle(Particle.BLOCK, blockLoc.add(0.6, 0.6, 0.6), 50, 0.25, 0.25, 0.25, 0, blockType.createBlockData());
    }

    public static void removeBlock(Location blockLoc, TriSound triSound) {
        blockLoc.getWorld().playSound(blockLoc, triSound.sound(), triSound.volume(), triSound.pitch());
        blockLoc.getWorld().spawnParticle(Particle.BLOCK, blockLoc.add(0.5, 0.5, 0.5), 50, 0.25, 0.25, 0.25, 0, blockLoc.getBlock().getType().createBlockData());
        blockLoc.getBlock().setType(Material.AIR);
    }

    public static void popBlock(Location blockLoc, TriSound triSound) {
        blockLoc.getWorld().playSound(blockLoc, triSound.sound(), triSound.volume(), triSound.pitch());
        blockLoc.getWorld().spawnParticle(Particle.BLOCK, blockLoc.add(0.5, 0.5, 0.5), 50, 0.25, 0.25, 0.25, 0, blockLoc.getBlock().getType().createBlockData());
    }
}