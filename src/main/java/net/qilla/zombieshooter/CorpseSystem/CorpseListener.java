package net.qilla.zombieshooter.CorpseSystem;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.io.IOException;

public class CorpseListener implements Listener {

    /**
     * Couldn't figure out, I'll return to this eventually
     *
     * @param event
     * @throws IOException
     */

    //@EventHandler
    public void onEntityDeath(EntityDeathEvent event) throws IOException {
        new CorpseDisplay().body(event);
    }
}
