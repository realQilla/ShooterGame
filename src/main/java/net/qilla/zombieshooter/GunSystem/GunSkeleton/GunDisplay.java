package net.qilla.zombieshooter.GunSystem.GunSkeleton;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunPDC;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunRegistry;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.GunSystem.GunUtils.CheckValid;
import net.qilla.zombieshooter.GunSystem.GunUtils.GetFromGun;
import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GunDisplay {

    private static final Map<UUID, GunDisplay> gunStatDisplay = new HashMap<>();

    private BukkitTask displayTask;
    private final Player player;
    private int currentMagazine = 0;
    private int currentCapacity = 0;
    private int currentMode = 0;
    private GunBase gunType = null;
    private String gunUniqueID = null;

    public GunDisplay(Player player) {
        this.player = player;
        gunStatDisplay.put(player.getUniqueId(), this);
    }

    public void displayLoop() {
        displayTask = new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack heldItem = player.getInventory().getItemInMainHand();
                updateGunFields(heldItem, false);
                magazineDisplay(player);
                capacityDisplay(player);
            }
        }.runTaskTimer(ZombieShooter.getInstance(), 0, 40);
    }

    private void capacityDisplay(Player player) {
        setAmmoDisplay(player, currentCapacity);
    }

    private void magazineDisplay(Player player) {
        if (gunType != null) {
            String currentMode = gunType.getFireMod()[this.currentMode].modeName();
            player.sendActionBar(MiniMessage.miniMessage().deserialize("<!italic><gold>☄</gold> <white>" + currentMagazine + "</white> | <dark_aqua>\uD83D\uDD25</dark_aqua> <white>" + currentMode + "</white>"));
        } else {
            player.sendActionBar(MiniMessage.miniMessage().deserialize("nothing"));
        }
    }

    public void updateGunFields(ItemStack item, boolean forceUpdate) {
        if(CheckValid.isValidBoth(item)) {
            PersistentDataContainer dataContainer = item.getItemMeta().getPersistentDataContainer();
            gunType = GunRegistry.getInstance().getGun(GetFromGun.typeID(dataContainer));
            currentMagazine = dataContainer.get(GunPDC.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER);
            currentCapacity = dataContainer.get(GunPDC.GUN_CAPACITY.getKey(), PersistentDataType.INTEGER);
            currentMode = dataContainer.get(GunPDC.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER);
            gunUniqueID = dataContainer.get(GunPDC.GUN_UUID.getKey(), PersistentDataType.STRING);
        } else {
            clearFields();
        }
        if(forceUpdate) {
            forceUpdate();
        }
    }

    private void clearFields() {
        currentMagazine = 0;
        currentCapacity = 0;
        currentMode = 0;
        gunType = null;
        gunUniqueID = null;
    }

    public void setCurrentMagazine(int currentMagazine) {
        this.currentMagazine = currentMagazine;
        forceUpdate();
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
        forceUpdate();
    }

    public void setCurrentMode(int currentMode) {
        this.currentMode = currentMode;
        forceUpdate();
    }

    public void setGunType(GunBase gunType) {
        this.gunType = gunType;
        forceUpdate();
    }

    public void setGunUniqueID(String gunUniqueID) {
        this.gunUniqueID = gunUniqueID;
    }

    private void forceUpdate() {
        if(displayTask != null) {
            displayTask.cancel();
            displayTask = null;
        }
        displayLoop();
    }

    private void setAmmoDisplay(Player player, Integer ammo) {
        if (ammo < 0) ammo = 0;
        player.setLevel(ammo);
        player.setExp(0);
    }

    public static GunDisplay getDisplayMap(Player player) {
        return gunStatDisplay.get(player.getUniqueId());
    }

    public void remove() {
        gunStatDisplay.remove(player.getUniqueId());
        if(displayTask != null) {
            displayTask.cancel();
            displayTask = null;
        }
    }
}
