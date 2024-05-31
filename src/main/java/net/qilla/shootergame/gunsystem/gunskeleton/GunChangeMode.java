package net.qilla.shootergame.gunsystem.gunskeleton;

import net.qilla.shootergame.cooldown.GunCooldown;
import net.qilla.shootergame.gunsystem.guncreation.guntype.GunBase;
import net.qilla.shootergame.gunsystem.guncreation.GunPDC;
import net.qilla.shootergame.util.SoundModel;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class GunChangeMode {

    private final Player player;
    private final GunBase gunBase;
    private final ItemStack gunItem;

    public GunChangeMode(@NotNull final Player player, @NotNull final GunBase gunBase, @NotNull final ItemStack gunItem) {
        this.player = player;
        this.gunBase = gunBase;
        this.gunItem = gunItem;
    }

    public void change() {
        PersistentDataContainer dataContainer = gunItem.getItemMeta().getPersistentDataContainer();
        SoundModel changeMode = this.gunBase.getCosmeticMod().changeFireMode();

        if(GunCooldown.getInstance().genericCooldown(player, GunCooldown.ActionCooldown.MODE_CHANGE, true)) return;

        if(this.gunBase.getFireMod().length == 1) {
            player.playSound(player.getLocation(), changeMode.getSound(), changeMode.getVolume(), changeMode.getPitch());
            return;
        }

        final int fireMode = getNextFireMode(this.gunBase, dataContainer);
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
