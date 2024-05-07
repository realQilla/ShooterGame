package net.qilla.zombieshooter.WeaponSystem.Systems;

import net.qilla.zombieshooter.Cooldown.GunCooldown;
import net.qilla.zombieshooter.Utils.Randomizer;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunData;
import net.qilla.zombieshooter.WeaponSystem.SoundModel;
import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

public class GunFire extends ShootBase {

    public void onShoot(PlayerInteractEvent event, GunBase gunType) {
        Player player = event.getPlayer();
        ItemStack gunItem = event.getItem();
        int fireMode = gunItem.getItemMeta().getPersistentDataContainer().get(GunData.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER);

        event.setCancelled(true);

        if(gunType.getFireMod()[fireMode].fireCooldown() !=0 ) {
            if (GunCooldown.getInstance().fireCooldown(player, gunType, gunItem, true)) return;
        }
        if(checkReloadingTag(gunItem)) return;
        if(GunCooldown.getInstance().genericCooldown(player, GunCooldown.ActionCooldown.RECENT_RELOAD, false)) return;
        GunCooldown.getInstance().nonRemovingCooldown(player, GunCooldown.ActionCooldown.ACTION_PREVENTS_RELOAD, true);

        RayTraceResult traceBlock = player.rayTraceBlocks(gunType.getFireMod()[fireMode].bulletRange());
        RayTraceResult traceEntity = player.rayTraceEntities(gunType.getFireMod()[fireMode].bulletRange());

        int bulletAmount = gunType.getFireMod()[fireMode].bulletAmount();
        int perBulletCooldown = gunType.getFireMod()[fireMode].perBulletCooldown();

        new BukkitRunnable() {
            int bulletsInBurst = 0;

            @Override
            public void run() {
                if (bulletsInBurst >= bulletAmount || checkEmptyAmmo(player, gunItem)) {
                    cancel();
                    return;
                }

                SoundModel fireSound = gunType.getCosmeticMod().fireSound();
                player.getWorld().playSound(player.getLocation(), fireSound.getSound(), fireSound.getVolume(), new Randomizer().standard((fireSound.getPitch() - 0.15f), fireSound.getPitch() + 0.15f));
                fireParticle(gunType, player.getLocation(), gunItem);

                if (traceEntity != null) {
                    onHitEntity(traceEntity.getHitEntity(), player, gunType, gunItem);
                }
                if (traceBlock != null) {
                    onHitBlock(traceBlock.getHitBlock(), player, gunType);
                }
                //new GunRecoil().verticalRecoil(player, true);

                updateGun(event, gunType);
                bulletsInBurst++;
            }
        }.runTaskTimer(ZombieShooter.getInstance(), 0L, perBulletCooldown);
    }
}
