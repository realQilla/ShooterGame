package net.qilla.zombieshooter.WeaponSystem.Systems;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.zombieshooter.Cooldown.GunCooldown;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunData;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class GunChangeMode {

    public void rotate(Player player, ItemStack item, GunBase gunType) {
        int fireMod = item.getItemMeta().getPersistentDataContainer().get(GunData.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER);

        if(GunCooldown.getInstance().genericCooldown(player, GunCooldown.ActionCooldown.MODE_CHANGE, true)) return;

        if(gunType.getFireMod().length == 1) {
            player.sendActionBar(MiniMessage.miniMessage().deserialize("<red>Fire cannot be changed</red>"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.5f, 2f);
            return;
        }

        if (fireMod == gunType.getFireMod().length - 1) {
            fireMod = 0;
        } else {
            fireMod++;
        }

        final int finalFireMod = fireMod;

        item.editMeta(meta -> {
            meta.getPersistentDataContainer().set(GunData.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER, finalFireMod);
        });
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.5f, 1f);
        player.sendActionBar(MiniMessage.miniMessage().deserialize("<green>" + gunType.getFireMod()[fireMod].modeName() + " fire</green>"));
    }
}
