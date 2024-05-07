package net.qilla.zombieshooter.WeaponSystem.Systems;

import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunData;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;

public class GunDamage {

    public void general(Player player, LivingEntity entity, GunBase gunType, ItemStack gun) {
        int fireMod = gun.getItemMeta().getPersistentDataContainer().get(GunData.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER);
        double damage = gunType.getFireMod()[fireMod].bulletDamage();
        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
        damage = damage * (1 - Math.min(20, Math.max(armor / 5, armor - damage / 2)) / 25);
        entity.damage(damage);
        entity.setNoDamageTicks(1);
        entity.setKiller(player);
        if(entity instanceof Player hitPlayer) {
            for(ItemStack item : hitPlayer.getInventory().getArmorContents()) {
                if(item == null || !(item.getItemMeta() instanceof Damageable)) return;
                Damageable damageable = (Damageable) item.getItemMeta();
                int newDurability = damageable.getDamage() + 10;
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

    public void armorPierce() {

    }

}
