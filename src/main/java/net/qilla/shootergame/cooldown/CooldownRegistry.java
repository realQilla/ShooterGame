package net.qilla.shootergame.cooldown;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CooldownRegistry {

    private final Set<CDPlayer> normalCD = new HashSet<>();
    private final Map<CDPlayer, Long> overridableCD = new HashMap<>();

    public boolean getOverridable(CDPlayer cdPlayer) {
        return overridableCD.containsKey(cdPlayer) && overridableCD.get(cdPlayer) > System.currentTimeMillis();
    }

    public boolean getNormal(CDPlayer cdPlayer) {
        return normalCD.contains(cdPlayer);
    }

    public void setOverridableCD(CDPlayer cdPlayer, long ms) {
        overridableCD.put(cdPlayer, ms);
    }

    public void startNormalCD(CDPlayer cdPlayer) {
        normalCD.add(cdPlayer);
    }

    public void removeNormalCD(CDPlayer cdPlayer) {
        normalCD.remove(cdPlayer);
    }
}