package net.qilla.shootergame.cooldown;

import net.qilla.shootergame.ShooterGame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static net.qilla.shootergame.cooldown.GunCooldown.ActionCooldown.FIRED_GUN;

public class GunCooldown {

    private static final ShooterGame plugin = ShooterGame.getPlugin(ShooterGame.class);
    private static final CooldownRegistry cooldownRegistry = new CooldownRegistry();

    /**
     * Will add the player to the cooldown registry if they are not already on cooldown.
     *
     * @param player The player to start the cooldown for
     * @param action The action to start the cooldown for
     * @param start  Whether to just check for the cooldown, or to check and start.
     *
     * @return Returns true if the user is currently on cooldown.
     */

    public static boolean normalCD(Player player, ActionCooldown action, boolean start) {
        final CDPlayer cdPlayer = new CDPlayer(player.getUniqueId(), action);

        if(cooldownRegistry.getNormal(cdPlayer)) return true;
        else if(start) startNormalCD(cdPlayer, action.ticks);
        return false;
    }

    /**
     * Will overwrite the previous cooldown if the player is already on cooldown.
     *
     * @param player The player to start the cooldown for
     * @param action The action to start the cooldown for
     * @param start  Whether to just check for the cooldown, or to check and start.
     *
     * @return Returns true if the user is currently on cooldown.
     */

    public static boolean overridableCD(Player player, ActionCooldown action, boolean start) {
        final CDPlayer cdPlayer = new CDPlayer(player.getUniqueId(), action);
        final long setTime = System.currentTimeMillis() + (action.ticks * 50);
        if(cooldownRegistry.getOverridable(cdPlayer)) {
            if(start) cooldownRegistry.setOverridableCD(cdPlayer, setTime);
            return true;
        } else if(start) {
            cooldownRegistry.setOverridableCD(cdPlayer, setTime);
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

    public static boolean startFireCD(Player player, int length, boolean start) {
        CDPlayer cdPlayer = new CDPlayer(player.getUniqueId(), FIRED_GUN);
        if(cooldownRegistry.getNormal(cdPlayer)) return true;
        else if(start) startNormalCD(cdPlayer, length);
        return false;
    }

    private static void startNormalCD(CDPlayer cdPlayer, long ticks) {
        cooldownRegistry.startNormalCD(cdPlayer);
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            cooldownRegistry.removeNormalCD(cdPlayer);
        }, ticks);
    }

    public enum ActionCooldown {
        FIRED_GUN(0),
        ACTION_PREVENTS_RELOAD(10),
        RECENT_RELOAD(10),
        MODE_CHANGE(10);

        final long ticks;

        ActionCooldown(long ticks) {
            this.ticks = ticks;
        }
    }
}