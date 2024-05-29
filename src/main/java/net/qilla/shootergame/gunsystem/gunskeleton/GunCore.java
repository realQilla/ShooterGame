package net.qilla.shootergame.gunsystem.gunskeleton;

import net.qilla.shootergame.gunsystem.guncreation.guntype.GunBase;
import net.qilla.shootergame.gunsystem.guncreation.GunPDC;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public abstract class GunCore {

    protected void fireParticle(GunBase gunType, Location location, ItemStack gun) {
        int fireMode = gun.getItemMeta().getPersistentDataContainer().get(GunPDC.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER);
        int particleLength = gunType.getFireMod()[fireMode].bulletRange();

        location.add(0, 1.4, 0);
        for (double ray = 1f; ray < particleLength; ray += 1) {
            Vector vector = location.getDirection().multiply(ray).normalize();
            location.add(vector);

            location.getWorld().spawnParticle(gunType.getCosmeticMod().fireParticle(), location, 1, 0, 0, 0, 0.15f);
        }
    }
}