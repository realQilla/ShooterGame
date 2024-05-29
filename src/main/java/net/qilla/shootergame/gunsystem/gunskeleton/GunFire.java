package net.qilla.shootergame.gunsystem.gunskeleton;

import net.qilla.shootergame.cooldown.GunCooldown;
import net.qilla.shootergame.util.Randomizer;
import net.qilla.shootergame.gunsystem.guncreation.GunRegistry;
import net.qilla.shootergame.gunsystem.guncreation.guntype.GunBase;
import net.qilla.shootergame.gunsystem.guncreation.GunPDC;
import net.qilla.shootergame.gunsystem.gunutil.CheckValid;
import net.qilla.shootergame.gunsystem.gunutil.GetFromGun;
import net.qilla.shootergame.util.SoundModel;
import net.qilla.shootergame.ShooterGame;
import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

import java.util.Objects;

import static net.qilla.shootergame.cooldown.GunCooldown.ActionCooldown.*;

public class GunFire extends GunCore {

    public void fireMain(Player player, ItemStack gunItem) {
        PersistentDataContainer dataContainer = gunItem.getItemMeta().getPersistentDataContainer();
        GunBase gunType = GunRegistry.getGun(GetFromGun.typeID(dataContainer));
        String gunUniqueID = dataContainer.get(GunPDC.GUN_UUID.getKey(), PersistentDataType.STRING);

        int fireMode = gunItem.getItemMeta().getPersistentDataContainer().get(GunPDC.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER);

        if(gunType.getFireMod()[fireMode].fireCooldown() !=0 ) {
            if (GunCooldown.getInstance().fireCooldown(player, gunType, gunItem, true)) return;
        }
        if(dataContainer.get(GunPDC.GUN_RELOAD_STATUS.getKey(), PersistentDataType.BOOLEAN)) return;
        if(GunCooldown.getInstance().genericCooldown(player, RECENT_RELOAD, false)) return;
        GunCooldown.getInstance().nonRemovingCooldown(player, ACTION_PREVENTS_RELOAD, true);

        new BukkitRunnable() {
            int bulletsInBurst = gunType.getFireMod()[fireMode].bulletAmount();
            int bulletsInMagazine = gunItem.getItemMeta().getPersistentDataContainer().get(GunPDC.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER);

            @Override
            public void run() {
                ItemStack gunItem = player.getInventory().getItemInMainHand();
                if(!CheckValid.isValidBoth(gunItem) || !Objects.equals(gunItem.getItemMeta().getPersistentDataContainer().get(GunPDC.GUN_UUID.getKey(), PersistentDataType.STRING), gunUniqueID)) {
                    cancel();
                    return;
                }

                if (bulletsInBurst <= 0 || checkEmptyAmmo(player, bulletsInMagazine, gunType)) {
                    //GunDisplay.getDisplayMap(player).setCurrentMagazine(bulletsInMagazine);
                    cancel();
                    return;
                }
                //GunDisplay.getDisplayMap(player).setCurrentMagazine(bulletsInMagazine);

                RayTraceResult traceBlock = player.rayTraceBlocks(gunType.getFireMod()[fireMode].bulletRange(), FluidCollisionMode.NEVER);
                RayTraceResult traceEntity = player.rayTraceEntities(gunType.getFireMod()[fireMode].bulletRange(), true);

                SoundModel fireSound = gunType.getCosmeticMod().fireSound();
                player.getWorld().playSound(player.getLocation(), fireSound.getSound(), fireSound.getVolume(), new Randomizer().between((fireSound.getPitch() - 0.15f), fireSound.getPitch() + 0.15f));
                fireParticle(gunType, player.getLocation(), gunItem);

                if (traceEntity != null) {
                    new HitEndpoint().onHitEntity(traceEntity.getHitEntity(), player, gunType, gunItem);
                }
                if (traceBlock != null) {
                    new HitEndpoint().onHitBlock(traceBlock.getHitBlock(), player, gunType);
                }
                //new GunRecoil().verticalRecoil(player, true);

                updateGun(gunItem, gunType);
                bulletsInBurst--;
                bulletsInMagazine--;
            }
        }.runTaskTimer(ShooterGame.getInstance(), 0L, gunType.getFireMod()[fireMode].perBulletCooldown());
    }

    protected boolean checkEmptyAmmo(Player player, int bulletsInMagazine, GunBase gunType) {
        if(bulletsInMagazine <= 0) {
            SoundModel emptyMagazine = gunType.getCosmeticMod().emptyMagazine();
            player.playSound(player.getLocation(), emptyMagazine.getSound(), emptyMagazine.getVolume(), emptyMagazine.getPitch());
            return true;
        }
        return false;
    }

    protected int decrementMagazine(ItemMeta meta) {
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        int currentMagazine = dataContainer.get(GunPDC.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER) - 1;
        dataContainer.set(GunPDC.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER, currentMagazine);
        return currentMagazine;
    }

    protected void updateGun(ItemStack gunItem, GunBase gunType) {
        ItemMeta meta = gunItem.getItemMeta();
        Damageable damageable = (Damageable) meta;

        int currentMagazine = decrementMagazine(meta);
        int maxMagazine = gunType.getAmmunitionMod().gunMagazine();
        if(currentMagazine > 0) {
            damageable.setDamage((maxMagazine - currentMagazine) * (gunItem.getType().getMaxDurability() / maxMagazine));
        } else {
            damageable.setDamage(gunItem.getType().getMaxDurability() - 2);
        }
        gunItem.setItemMeta(meta);
    }
}
