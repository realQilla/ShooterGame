package net.qilla.zombieshooter.GunSystem.GunSkeleton;

import net.qilla.zombieshooter.Cooldown.GunCooldown;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunRegistry;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunPDC;
import net.qilla.zombieshooter.GunSystem.GunUtils.GetFromGun;
import net.qilla.zombieshooter.Utils.SoundModel;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class GunChangeMode extends GunCore {

    public void modeMain(@NotNull Player player, @NotNull ItemStack gunItem) {
        PersistentDataContainer dataContainer = gunItem.getItemMeta().getPersistentDataContainer();
        GunBase gunType = GunRegistry.getGun(GetFromGun.typeID(dataContainer));
        SoundModel changeMode = gunType.getCosmeticMod().changeFireMode();

        if(GunCooldown.getInstance().genericCooldown(player, GunCooldown.ActionCooldown.MODE_CHANGE, true)) return;

        if(gunType.getFireMod().length == 1) {
            player.playSound(player.getLocation(), changeMode.getSound(), changeMode.getVolume(), changeMode.getPitch());
            return;
        }

        final int fireMode = getNextFireMode(gunType, dataContainer);
        //GunDisplay.getDisplayMap(player).setCurrentMode(fireMode);
        updateFireMode(gunItem, fireMode);
        player.playSound(player.getLocation(), changeMode.getSound(), changeMode.getVolume(), changeMode.getPitch());
    }

    private int getNextFireMode(@NotNull GunBase gunType, @NotNull PersistentDataContainer dataContainer) {
        int fireMod = dataContainer.get(GunPDC.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER);
        return fireMod == gunType.getFireMod().length - 1 ? 0 : fireMod + 1;
    }

    private void updateFireMode(@NotNull ItemStack gunItem, int fireMod) {
        gunItem.editMeta(meta -> meta.getPersistentDataContainer().set(GunPDC.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER, fireMod));
    }
}
