package net.qilla.zombieshooter.StatSystem.StatManagement;

import java.util.HashMap;
import java.util.UUID;

public abstract class StatCore {

    private static final HashMap<UUID, StatModel> playerStatMap = new HashMap<>();
    private static final HashMap<UUID, Long> playerHealthMap = new HashMap<>();

    private final UUID playerUUID;

    protected StatCore(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    /**
     * Most stat management
     */

    protected StatModel getStats() {
        return playerStatMap.get(playerUUID);
    }

    protected void setStats(StatModel statModel) {
        playerStatMap.put(playerUUID, statModel);
    }

    protected void clearStats() {
        playerStatMap.remove(playerUUID);
        playerHealthMap.remove(playerUUID);
    }

    /**
     * Current-health management, it's outside the stat model, so I don't
     * have to call the full model each time the player health is changed.
     */

    protected long getHealth() {
        return playerHealthMap.get(playerUUID);
    }

    protected void setHealth(long health) {
        playerHealthMap.put(playerUUID, health);
    }
}
