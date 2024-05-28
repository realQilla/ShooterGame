package net.qilla.shootergame.Cooldown;

import java.util.Objects;
import java.util.UUID;

public class CDPlayer {

    private final GunCooldown.ActionCooldown action;
    private final UUID playerUUID;

    public CDPlayer (UUID playerUUID, GunCooldown.ActionCooldown action) {
        this.playerUUID = playerUUID;
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CDPlayer cdPlayer = (CDPlayer) o;
        return action == cdPlayer.action && Objects.equals(playerUUID, cdPlayer.playerUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, playerUUID);
    }
}
