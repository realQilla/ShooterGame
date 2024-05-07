package net.qilla.zombieshooter.WeaponSystem.Systems;

import net.qilla.zombieshooter.Utils.Randomizer;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunData;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public abstract class ShootBase {

    protected void onHitEntity(Entity entity, Player player, GunBase gunType, ItemStack gun) {

        if (entity instanceof LivingEntity hitEntity && hitEntity.getHealth() > 0 && hitEntity.getNoDamageTicks() == 0) {
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, 2f, 1);

            Vector playerVector = player.getLocation().getDirection();
            hitEntity.knockback(0.25f, playerVector.getX() * -1, playerVector.getZ() * -1);

            new GunDamage().general(player, hitEntity, gunType, gun);
        }
    }

    protected void onHitBlock(Block block, Player player, GunBase gunType) {
        //Block hitting code
    }

    protected void fireParticle(GunBase gunType, Location location, ItemStack gun) {
        int fireMode = gun.getItemMeta().getPersistentDataContainer().get(GunData.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER);
        int particleLength = gunType.getFireMod()[fireMode].bulletRange();

        location.add(0, 1.4, 0);
        for (double ray = 1f; ray < particleLength; ray += 1) {
            Vector vector = location.getDirection().multiply(ray).normalize();
            location.add(vector);

            location.getWorld().spawnParticle(gunType.getCosmeticMod().fireParticle(), location, 1, 0, 0, 0, 0.15f);
        }
    }

    protected int decrementMagazine(ItemMeta meta) {
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        int currentMagazine = dataContainer.get(GunData.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER) - 1;
        dataContainer.set(GunData.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER, currentMagazine);
        return currentMagazine;
    }

    protected void updateGun(PlayerInteractEvent event, GunBase gunType) {
        ItemStack item = event.getItem();
        ItemMeta meta = item.getItemMeta();
        Damageable damageable = (Damageable) meta;

        int currentMagazine = decrementMagazine(meta);
        int maxMagazine = gunType.getAmmunitionMod().gunMagazine();
        if(currentMagazine > 0) {
            damageable.setDamage((maxMagazine - currentMagazine) * (item.getType().getMaxDurability() / maxMagazine));
        } else {
            damageable.setDamage(item.getType().getMaxDurability() - 2);
        }
        item.setItemMeta(meta);
    }

    protected boolean checkEmptyAmmo(Player player, ItemStack item) {
        PersistentDataContainer dataContainer = item.getItemMeta().getPersistentDataContainer();
        if(dataContainer.get(GunData.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER) <= 0) {
            player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_STEP, 1.5f, new Randomizer().standard(1.5f, 2f));
            return true;
        }
        return false;
    }

    protected boolean checkReloadingTag(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().get(GunData.GUN_IS_RELOADING.getKey(), PersistentDataType.BOOLEAN);
    }
}
