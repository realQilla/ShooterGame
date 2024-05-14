package net.qilla.zombieshooter.GunSystem.GunSkeleton;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunPDC;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.Utils.SoundModel;
import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class HitEndpoint {

    public void onHitEntity(Entity entity, Player player, GunBase gunType, ItemStack gun) {
        if (entity instanceof LivingEntity hitEntity && hitEntity.getHealth() > 0 && hitEntity.getNoDamageTicks() == 0) {
            final SoundModel hitMarker = gunType.getCosmeticMod().hitMarkerSound();
            player.playSound(player, hitMarker.getSound(), hitMarker.getVolume(), hitMarker.getPitch());

            Vector playerVector = player.getLocation().getDirection();
            hitEntity.knockback(0.25f, playerVector.getX() * -1, playerVector.getZ() * -1);
            damageMain(player, hitEntity, gunType, gun);
        }
    }

    public void onHitBlock(Block block, Player player, GunBase gunType) {
        int blockDamage = block.hasMetadata("damage_amount") ? block.getMetadata("damage_amount").getFirst().asInt() + 2 : 2;
        block.setMetadata("damage_amount", new FixedMetadataValue(ZombieShooter.getInstance(), blockDamage));
        if (blockDamage < 10) {
            sendBlockDestructionPacket(block, blockDamage);
        } else {
            block.removeMetadata("damage_amount", ZombieShooter.getInstance());
        }
    }

    private void sendBlockDestructionPacket(Block block, int blockDamage) {
        BlockPos blockPos = new BlockPos(block.getX(), block.getY(), block.getZ());
        ClientboundBlockDestructionPacket packet = new ClientboundBlockDestructionPacket(block.hashCode(), blockPos, blockDamage);
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getWorld() == block.getWorld() && player.getLocation().distance(block.getLocation()) < 32)
                .forEach(player -> ((CraftPlayer) player).getHandle().connection.sendPacket(packet));
    }

    public void damageMain(Player player, LivingEntity entity, GunBase gunType, ItemStack gun) {
        double damage = calculateDamage(entity, gunType, gun);
        entity.damage(damage, player);
        entity.setNoDamageTicks(1);
        entity.setKiller(player);
        CraftEntity temp = (CraftEntity) entity;
        if(entity instanceof Player hitPlayer) {
            damageArmor(hitPlayer);
        }
    }

    private double calculateDamage(LivingEntity entity, GunBase gunType, ItemStack gun) {
        int fireMod = gun.getItemMeta().getPersistentDataContainer().get(GunPDC.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER);
        double damage = gunType.getFireMod()[fireMod].bulletDamage();
        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
        return damage * (1 - Math.min(20, Math.max(armor / 5, armor - damage / 2)) / 25);
    }

    private void damageArmor(Player hitPlayer) {
        for(ItemStack item : hitPlayer.getInventory().getArmorContents()) {
            if(item == null || !(item.getItemMeta() instanceof Damageable damageable)) return;
            if(Math.random() <= 0.50) return;
            int newDurability = damageable.getDamage() + 1;
            if(newDurability < item.getType().getMaxDurability()) {
                damageable.setDamage(newDurability);
                item.setItemMeta(damageable);
            } else {
                hitPlayer.getWorld().playSound(hitPlayer.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1);
                item.setAmount(0);
            }
        }
    }
}
