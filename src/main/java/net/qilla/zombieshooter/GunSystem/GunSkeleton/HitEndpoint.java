package net.qilla.zombieshooter.GunSystem.GunSkeleton;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunData;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.Utils.Randomizer;
import net.qilla.zombieshooter.Utils.SoundModel;
import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
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

    private final Randomizer random = new Randomizer();

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

        int blockDamage = 2;
        if (block.hasMetadata("damage_amount")) {
            blockDamage = block.getMetadata("damage_amount").get(0).asInt() + 2;
            block.setMetadata("damage_amount", new FixedMetadataValue(ZombieShooter.getInstance(), blockDamage));
        } else {
            block.setMetadata("damage_amount", new FixedMetadataValue(ZombieShooter.getInstance(), blockDamage));
        }
        if (blockDamage >= 10) {
            block.removeMetadata("damage_amount", ZombieShooter.getInstance());
        } else {
            for (Player nearby : Bukkit.getOnlinePlayers()) {
                if (nearby.getWorld() != block.getWorld()) continue;
                if (block.getLocation().distance(nearby.getLocation()) < 32) {
                    ServerPlayer nmsPlayer = ((CraftPlayer) nearby).getHandle();
                    nmsPlayer.connection.sendPacket(new ClientboundBlockDestructionPacket(block.hashCode(), new BlockPos(block.getX(), block.getY(), block.getZ()), blockDamage));
                }
            }
        }
    }

    public void damageMain(Player player, LivingEntity entity, GunBase gunType, ItemStack gun) {
        int fireMod = gun.getItemMeta().getPersistentDataContainer().get(GunData.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER);
        double damage = gunType.getFireMod()[fireMod].bulletDamage();
        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
        damage = damage * (1 - Math.min(20, Math.max(armor / 5, armor - damage / 2)) / 25);
        entity.damage(damage);
        entity.setNoDamageTicks(1);
        entity.setKiller(player);
        if(entity instanceof Player hitPlayer) {
            for(ItemStack item : hitPlayer.getInventory().getArmorContents()) {
                if(item == null || !(item.getItemMeta() instanceof Damageable damageable)) return;
                if(random.between(0, 1) <= 0.50) return;
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
}
