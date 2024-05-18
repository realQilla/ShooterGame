package net.qilla.zombieshooter.NPCLib;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.ChatVisiblity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class NPCBuilder {

    private String name = "NPC";
    private UUID uuid = UUID.randomUUID();

    private String signature;
    private String value;

    private final double x;
    private final double y;
    private final double z;
    private float yaw;
    private float pitch;

    private String clientLanguage = "en_US";
    private int viewDistance = 0;
    private ChatVisiblity chatVisibility = ChatVisiblity.HIDDEN;
    private boolean chatColors = false;
    private int displayedSkinParts = 0x7F;
    private HumanoidArm mainHand = HumanoidArm.RIGHT;
    private boolean textFiltering = false;
    private boolean showInTabList = false;

    private final ServerLevel nmsLevel;
    private MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();

    private ServerPlayer npc;

    /**
     * Builds a simple NPC, custom arguments can be
     * specified, or the default ones can be used.
     *
     * @param world The NPC's spawning world
     * @param location The NPC's spawning location
     */

    public NPCBuilder(World world, Location location, float yaw, float pitch) {
        this.nmsLevel = ((CraftWorld) world).getHandle();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = Math.round(yaw);
        this.pitch = Math.round(pitch);
    }

    /**
     * Builds a simple NPC, custom arguments can be
     * specified, or the default ones can be used.
     *
     * @param world The NPC's spawning world
     * @param location The NPC's spawning location
     */

    public NPCBuilder(World world, Location location) {
        this.nmsLevel = ((CraftWorld) world).getHandle();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getZ();
        this.yaw = Math.round(location.getYaw());
        this.pitch = Math.round(location.getPitch());
    }

    /**
     * Builds a simple NPC, custom arguments can be
     * specified, or the default ones can be used.
     *
     * @param world The NPC's spawning world
     * @param x The NPC's spawning X coordinate
     * @param y The NPC's spawning Y coordinate
     * @param z The NPC's spawning Z coordinate
     * @param yaw The NPC's spawning yaw
     * @param pitch The NPC's spawning pitch
     */

    public NPCBuilder(World world, double x, double y, double z, float yaw, float pitch) {
        this.nmsLevel = ((CraftWorld) world).getHandle();
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = Math.round(yaw);
        this.pitch = Math.round(pitch);
    }

    /**
     * Sets the NPC's name, and UUID
     *
     * @param name Sets the NPC's displayed name
     * @param uuid Sets the NPC's UUID
     */

    public void setName(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    /**
     * Sets the NPC's displayed name
     *
     * @param name
     */

    public void setName(String name) {
        if (name.length() > 16) throw new IllegalArgumentException("Given name is too long. Max length is 16 characters.");
        this.name = name;
    }

    /**
     * Set possible client information, for typical use, this is unnecessary.
     *
     * @param clientLanguage Language the NPC will use
     * @param viewDistance Chunk view distance for the NPC
     * @param chatVisibility Whether the NPC should be able to view chat.
     * @param chatColors Whether the NPC should see chat colors in chat.
     * @param displayedSkinParts Which skin parts of the NPC should display.
     * @param mainHand The main hand of the NPC
     * @param textFiltering Should text be filtered for the NPC
     * @param showInTabList Should the NPC show in the tab list
     */

    public void setClientInfo(String clientLanguage, int viewDistance, ChatVisiblity chatVisibility, boolean chatColors, int displayedSkinParts, HumanoidArm mainHand, boolean textFiltering, boolean showInTabList) {
        this.clientLanguage = clientLanguage;
        this.viewDistance = viewDistance;
        this.chatVisibility = chatVisibility;
        this.chatColors = chatColors;
        this.displayedSkinParts = displayedSkinParts;
        this.mainHand = mainHand;
        this.textFiltering = textFiltering;
        this.showInTabList = showInTabList;
    }

    /**
     * Typical use NPC client info customization.
     *
     * @param viewDistance Chunk view distance for the NPC
     * @param displayedSkinParts Which skin parts of the NPC should display.
     * @param mainHand The main hand of the NPC
     * @param showInTabList Should the NPC show in the tab list
     */

    public void setClientInfo(int viewDistance, int displayedSkinParts, HumanoidArm mainHand, boolean showInTabList) {
        this.viewDistance = viewDistance;
        this.displayedSkinParts = displayedSkinParts;
        this.mainHand = mainHand;
        this.showInTabList = showInTabList;
    }

    /**
     * Simple toggle for the NPC to show in the tab list.
     *
     * @param showInTabList Should the NPC show in the tab list
     */

    public void setTabListVisibility(boolean showInTabList) {
        this.showInTabList = showInTabList;
    }

    /**
     * Simple setter for the NPC's visible skin layers
     *
     * @param displayedSkinParts
     */

    public void setDisplayedSkinLayers(int displayedSkinParts) {
        this.displayedSkinParts = displayedSkinParts;
    }

    /**
     * Simple skin setter for the NPC, uses a player's skin.
     *
     * @param player
     */

    public void setSkin(Player player) {
        ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        final GameProfile gameProfile = nmsPlayer.getGameProfile();
        Property property = (Property) gameProfile.getProperties().get("textures").toArray()[0];
        this.signature = property.signature();
        this.value = property.value();
    }

    /**
     * Simple skin setter for the NPC, uses a custom property.
     *
     * @param property
     */

    public void setSkin(Property property) {
        this.signature = property.signature();
        this.value = property.value();
    }

    public void build() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        GameProfile gameProfile = new GameProfile(uuid, name);
        Property property = new Property("textures", value, signature);
        gameProfile.getProperties().put("textures", property);
        ClientInformation clientInfo = new ClientInformation(clientLanguage, viewDistance, chatVisibility, chatColors, displayedSkinParts, mainHand, textFiltering, showInTabList);
        ServerPlayer npc = new ServerPlayer(nmsServer, nmsLevel, gameProfile, clientInfo);
        npc.setPos(x, y, z);
        npc.setYRot(yaw);
        npc.setXRot(pitch);

        ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
        Constructor<?> objectConstructor = Object.class.getDeclaredConstructor();
        Constructor<?> constructor = reflectionFactory.newConstructorForSerialization(ServerGamePacketListenerImpl.class, objectConstructor);
        Object instance = ServerGamePacketListenerImpl.class.cast(constructor.newInstance());

        npc.getClass().getField("connection").set(npc, instance);

        this.npc = npc;
    }

    public ServerPlayer getNPC() {
        return npc;
    }
}
