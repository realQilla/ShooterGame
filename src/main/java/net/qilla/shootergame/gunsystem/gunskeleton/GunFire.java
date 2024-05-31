package net.qilla.shootergame.gunsystem.gunskeleton;

import net.qilla.shootergame.cooldown.GunCooldown;
import net.qilla.shootergame.util.Randomizer;
import net.qilla.shootergame.gunsystem.guncreation.guntype.GunBase;
import net.qilla.shootergame.gunsystem.guncreation.GunPDC;
import net.qilla.shootergame.gunsystem.gunutil.CheckValid;
import net.qilla.shootergame.util.SoundModel;
import net.qilla.shootergame.ShooterGame;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.qilla.shootergame.cooldown.GunCooldown.ActionCooldown.*;

public final class GunFire {

    private final ShooterGame plugin = ShooterGame.getInstance();

    private BukkitTask fireTask = null;
    private final Player player;
    private final GunBase gunBase;
    private ItemStack gunItem;

    private ItemMeta meta;
    private PersistentDataContainer dataContainer;
    private final String gunUniqueID;
    private final int fireMode;
    private int bulletsInBurst;
    private int bulletsInMagazine;
    private final int fireCooldown;
    private final boolean recentReload;

    public GunFire(@NotNull final Player player, @NotNull final GunBase gunBase, @NotNull final ItemStack gunItem) {
        this.player = player;
        this.gunBase = gunBase;
        this.gunItem = gunItem;

        this.meta = this.gunItem.getItemMeta();
        this.dataContainer = this.meta.getPersistentDataContainer();
        this.bulletsInMagazine = this.dataContainer.get(GunPDC.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER);
        this.recentReload = this.dataContainer.get(GunPDC.GUN_RELOAD_STATUS.getKey(), PersistentDataType.BOOLEAN);
        this.gunUniqueID = this.dataContainer.get(GunPDC.GUN_UUID.getKey(), PersistentDataType.STRING);
        this.fireMode = this.dataContainer.get(GunPDC.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER);
        this.fireCooldown = this.gunBase.getFireMod()[fireMode].fireCooldown();
        this.bulletsInBurst = this.gunBase.getFireMod()[fireMode].bulletAmount();
    }

    public void begin() {
        if(fireCooldown !=0 )
            if (GunCooldown.startFireCD(player, fireCooldown, true)) return;
        if(recentReload) return;
        if(GunCooldown.normalCD(player, RECENT_RELOAD, false)) return;
        GunCooldown.overridableCD(player, ACTION_PREVENTS_RELOAD, true);

        fireTask = Bukkit.getScheduler().runTaskTimer(ShooterGame.getInstance(), this::fire, 0L, gunBase.getFireMod()[fireMode].perBulletCooldown());
    }

    private void fire() {
        this.gunItem = this.player.getInventory().getItemInMainHand();
        this.dataContainer = this.gunItem.getItemMeta().getPersistentDataContainer();
        if(!CheckValid.isValidBoth(gunItem) || !Objects.equals(this.dataContainer.get(GunPDC.GUN_UUID.getKey(), PersistentDataType.STRING), gunUniqueID)) {
            this.fireTask.cancel();
            return;
        }

        if (this.bulletsInBurst <= 0 || checkEmptyAmmo()) {
            //GunDisplay.getDisplayMap(player).setCurrentMagazine(bulletsInMagazine);
            this.fireTask.cancel();
            return;
        }
        //GunDisplay.getDisplayMap(player).setCurrentMagazine(bulletsInMagazine);

        final RayTraceResult traceBlock = player.rayTraceBlocks(gunBase.getFireMod()[fireMode].bulletRange(), FluidCollisionMode.NEVER);
        final RayTraceResult traceEntity = player.rayTraceEntities(gunBase.getFireMod()[fireMode].bulletRange(), true);

        final SoundModel fireSound = gunBase.getCosmeticMod().fireSound();
        player.getWorld().playSound(player.getLocation(), fireSound.getSound(), fireSound.getVolume(), new Randomizer().between((fireSound.getPitch() - 0.15f), fireSound.getPitch() + 0.15f));
        fireParticle();

        if (traceEntity != null) {
            new HitEndpoint(player, traceEntity.getHitEntity(), gunBase, gunItem).hitEntity();
        }
        if (traceBlock != null) {
            new HitEndpoint(player, traceBlock.getHitBlock(), gunBase, gunItem).hitBlock();
        }
        //new GunRecoil().verticalRecoil(player, true);

        bulletsInBurst--;
        bulletsInMagazine--;
        updateGun();
    }

    private boolean checkEmptyAmmo() {
        if(this.bulletsInMagazine <= 0) {
            final SoundModel emptyMagazine = this.gunBase.getCosmeticMod().emptyMagazine();
            this.player.playSound(this.player.getLocation(), emptyMagazine.getSound(), emptyMagazine.getVolume(), emptyMagazine.getPitch());
            return true;
        }
        return false;
    }

    private void updateGun() {
        this.meta = this.gunItem.getItemMeta();
        final Damageable damageable = (Damageable) meta;

        final int maxMagazine = this.gunBase.getAmmunitionMod().gunMagazine();
        if(bulletsInMagazine > 0) damageable.setDamage((maxMagazine - bulletsInMagazine) * (this.gunItem.getType().getMaxDurability() / maxMagazine));
        else damageable.setDamage(this.gunItem.getType().getMaxDurability() - 2);
        this.meta.getPersistentDataContainer().set(GunPDC.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER, bulletsInMagazine);
        this.gunItem.setItemMeta(meta);
    }

    private void fireParticle() {
        final Location particleLoc = this.player.getEyeLocation();
        final int particleLength = this.gunBase.getFireMod()[this.fireMode].bulletRange();

        for (double ray = 1f; ray < particleLength; ray += 1) {
            final Vector vector = particleLoc.getDirection().multiply(ray).normalize();
            particleLoc.add(vector);

            particleLoc.getWorld().spawnParticle(this.gunBase.getCosmeticMod().fireParticle(), particleLoc, 1, 0, 0, 0, 0.15f);
        }
    }
}
