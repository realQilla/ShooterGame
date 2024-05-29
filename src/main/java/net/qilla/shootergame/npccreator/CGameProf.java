package net.qilla.shootergame.npccreator;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CGameProf {

    private final String name;
    private final UUID uuid;
    private Property property;

    public CGameProf(String name, UUID uuid) {
        if(name.length() > 16) throw new IllegalArgumentException("Given name is too long. Max length is 16 characters.");
        this.name = name;
        this.uuid = uuid;
    }

    public void setSkin(Player player) {
        ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        final GameProfile gameProfile = nmsPlayer.getGameProfile();
        Property property = (Property) gameProfile.getProperties().get("textures").toArray()[0];
        this.property = property;
    }


    public void setSkin(Property property) {
        this.property = property;
    }


}
