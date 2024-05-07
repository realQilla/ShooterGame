package net.qilla.zombieshooter.Cooldown;

import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunData;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static net.qilla.zombieshooter.Cooldown.GunCooldown.ActionCooldown.FIRED_GUN;

public class GunCooldown {

    private static final GunCooldown instance = new GunCooldown();

    private static final Set<CDPlayer> selfRemovingCooldown = new HashSet<>();
    private static final Map<CDPlayer, Long> callRemovingCooldown = new HashMap<>();

    private GunCooldown() {
    }

    public static GunCooldown getInstance() {
        return instance;
    }

    /**
     * Checks if the player has a cooldown for a specified action
     *
     * @param player The player to start the cooldown for
     * @param action The action to start the cooldown for
     * @param start Whether to just check for the cooldown, or
     * to check and start.
     * @return Returns a boolean
     */

    public boolean genericCooldown(Player player, ActionCooldown action, boolean start) {
        CDPlayer cdPlayer = new CDPlayer(player.getUniqueId(), action);

        if(selfRemovingCooldown.contains(cdPlayer)) {
            return true;
        } else if(start) {
            startSelfRemoving(cdPlayer, action.ticks);
        }
        return false;
    }

    /**
     * Checks if the player has a cooldown for a specified action
     *
     * @param player
     * @param action
     * @param start
     * @return
     */

    public boolean nonRemovingCooldown(Player player, ActionCooldown action, boolean start) {
        CDPlayer cdPlayer = new CDPlayer(player.getUniqueId(), action);
        long setTime = System.currentTimeMillis() + (action.ticks * 5);
        if(callRemovingCooldown.get(cdPlayer) != null && callRemovingCooldown.get(cdPlayer) > System.currentTimeMillis()) {
            if(start) {
                //Overwrite cooldown
                callRemovingCooldown.put(cdPlayer, setTime);
            }
            return true;
        } else if(start) {
            callRemovingCooldown.put(cdPlayer, setTime);

        }
        return false;
    }

    /**
     * Checks if the player has a cooldown for firing a gun,
     *
     * @param player The player to start the cooldown for
     * @param gunType The action to start the cooldown for
     * @param start Whether to just check for the cooldown, or
     * to check and start.
     * @return Returns a boolean
     */

    public boolean fireCooldown(Player player, GunBase gunType, ItemStack gunItem, boolean start) {
        CDPlayer cdPlayer = new CDPlayer(player.getUniqueId(), FIRED_GUN);
        int fireMode = gunItem.getItemMeta().getPersistentDataContainer().get(GunData.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER);
        if (selfRemovingCooldown.contains(cdPlayer)) {
            return true;
        } else if(start) {
            startSelfRemoving(cdPlayer, gunType.getFireMod()[fireMode].fireCooldown());
        }
        return false;
    }

    private void startSelfRemoving(CDPlayer cdPlayer, long ticks) {
        selfRemovingCooldown.add(cdPlayer);
        new BukkitRunnable() {
            @Override
            public void run() {
                selfRemovingCooldown.remove(cdPlayer);
            }
        }.runTaskLater(ZombieShooter.getInstance(), ticks);
    }

    public enum ActionCooldown {
        FIRED_GUN(0), ACTION_PREVENTS_RELOAD(10), RECENT_RELOAD(10), MODE_CHANGE(10);

        final long ticks;

        ActionCooldown(long ticks) {
            this.ticks = ticks;
        }
    }
}