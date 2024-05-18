package net.qilla.zombieshooter.CorpseSystem;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class CorpseListener implements Listener {

    /**
     * Couldn't figure out, I'll return to this eventually
     *
     * @param event
     * @throws IOException
     */

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) throws NoSuchFieldException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        new CorpseDisplay().body(event.getPlayer());
    }
}
