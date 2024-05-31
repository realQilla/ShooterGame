package net.qilla.shootergame.cooldown;

import net.qilla.shootergame.ShooterGame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static net.qilla.shootergame.cooldown.GunCooldown.ActionCooldown.FIRED_GUN;

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
     * @param start  Whether to just check for the cooldown, or to check and start.
     *
     * @return Returns a boolean
     */

    public boolean genericCooldown(Player player, ActionCooldown action, boolean start) {
        CDPlayer cdPlayer = new CDPlayer(player.getUniqueId(), action);

        if(selfRemovingCooldown.contains(cdPlayer)) {
            return true;
        } else if(start) {
            startNormalCD(cdPlayer, action.ticks);
        }
        return false;
    }

    /**
     * Checks if the player has a cooldown for a specified action
     *
     * @param player
     * @param action
     * @param start
     *
     * @return
     */

    public boolean startOverridableCD(Player player, ActionCooldown action, boolean start) {
        CDPlayer cdPlayer = new CDPlayer(player.getUniqueId(), action);
        long setTime = System.currentTimeMillis() + (action.ticks * 50);
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
     * @param length Amount of ticks to put the item on cooldown for.
     * @param start  Whether to just check for the cooldown, or to check and start.
     *
     * @return Returns a boolean
     */

    public boolean startFireCD(Player player, int length, boolean start) {
        CDPlayer cdPlayer = new CDPlayer(player.getUniqueId(), FIRED_GUN);
        if(selfRemovingCooldown.contains(cdPlayer)) return true;
        else if(start) startNormalCD(cdPlayer, length);
        return false;
    }

    private void startNormalCD(CDPlayer cdPlayer, long ticks) {
        selfRemovingCooldown.add(cdPlayer);
        Bukkit.getScheduler().runTaskLaterAsynchronously(ShooterGame.getInstance(), () -> {
            selfRemovingCooldown.remove(cdPlayer);
        }, ticks);
    }

    public enum ActionCooldown {
        FIRED_GUN(0),
        ACTION_PREVENTS_RELOAD(12),
        RECENT_RELOAD(10),
        MODE_CHANGE(10);

        final long ticks;

        ActionCooldown(long ticks) {
            this.ticks = ticks;
        }
    }
}