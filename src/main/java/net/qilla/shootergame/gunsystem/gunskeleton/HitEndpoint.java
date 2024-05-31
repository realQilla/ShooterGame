package net.qilla.shootergame.gunsystem.gunskeleton;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.qilla.shootergame.gunsystem.guncreation.GunPDC;
import net.qilla.shootergame.gunsystem.guncreation.guntype.GunBase;
import net.qilla.shootergame.statsystem.statmanagement.StatManager;
import net.qilla.shootergame.statsystem.statutil.Formula;
import net.qilla.shootergame.util.SoundModel;
import net.qilla.shootergame.ShooterGame;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class HitEndpoint {

    private final Player player;
    private LivingEntity target;
    private Block block;
    private GunBase gunBase;
    private ItemStack gunItem;

    private Integer gunDamage;

    public HitEndpoint(Player player, Entity hitEntity, GunBase gunBase, ItemStack gunItem) {
        this.player = player;
        if(!(hitEntity instanceof LivingEntity livingEntity)) return;
        this.target = livingEntity;
        this.block = null;
        this.gunBase = gunBase;
        this.gunItem = gunItem;

        this.gunDamage = this.gunItem.getItemMeta().getPersistentDataContainer().get(GunPDC.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER);
    }

    public HitEndpoint(Player player, Block hitBlock, GunBase gunBase, ItemStack gunItem) {
        this.player = player;
        this.target = null;
        this.block = hitBlock;
        this.gunBase = gunBase;
        this.gunItem = gunItem;

        this.gunDamage = null;
    }

    public void hitEntity() {
        if(target.getHealth() > 0 || target.getNoDamageTicks() != 0) return;

        final SoundModel hitMarker = this.gunBase.getCosmeticMod().hitMarkerSound();
        this.player.playSound(player, hitMarker.getSound(), hitMarker.getVolume(), hitMarker.getPitch());

        final Vector playerVector = player.getLocation().getDirection();
        target.knockback(0.25f, playerVector.getX() * -1, playerVector.getZ() * -1);
        damageMain();
    }

    public void hitBlock() {
        int blockDamage = block.hasMetadata("crack_amount") ? block.getMetadata("crack_amount").getFirst().asInt() + 2 : 2;
        block.setMetadata("crack_amount", new FixedMetadataValue(ShooterGame.getInstance(), blockDamage));
        if (blockDamage < 10) {
            sendBlockDestructionPacket(blockDamage);
        } else {
            block.removeMetadata("crack_amount", ShooterGame.getInstance());
        }
    }

    private void sendBlockDestructionPacket(int blockDamage) {
        BlockPos blockPos = new BlockPos(block.getX(), block.getY(), block.getZ());
        ClientboundBlockDestructionPacket packet = new ClientboundBlockDestructionPacket(block.hashCode(), blockPos, blockDamage);
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getWorld() == block.getWorld() && player.getLocation().distance(block.getLocation()) < 32)
                .forEach(player -> ((CraftPlayer) player).getHandle().connection.sendPacket(packet));
    }

    private void damageMain() {
        final double damage = calculateDamage();
        this.target.damage(damage, this.player);
        this.target.setNoDamageTicks(1);
        this.target.setKiller(this.player);
    }

    private double calculateDamage() {
        return Formula.defenseCalc(this.gunDamage, StatManager.getStatManager(this.target.getUniqueId()).getStats().getDefense());
    }
}
