package net.qilla.shootergame.Permission;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class PermissionCommand {

    public static final Permission getBlock = new Permission("getblock", "Allows you to obtain custom blocks.", PermissionDefault.OP);
    public static final Permission getArmor = new Permission("getarmor", "Allows you to obtain custom armor sets.", PermissionDefault.OP);
    public static final Permission getGun = new Permission("getgun", "Allows you to obtain custom weapons", PermissionDefault.OP);
}
