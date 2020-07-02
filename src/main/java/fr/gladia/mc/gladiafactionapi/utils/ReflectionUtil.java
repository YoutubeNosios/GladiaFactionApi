package fr.gladia.mc.gladiafactionapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    public static void sendActionBar(Player p, String message) {
        Object text = HelpUtil.OBJECTS.getObject(HelpUtil.NMS.getClass("ChatComponentText"),
                ChatColor.translateAlternateColorCodes('&', message));
        Object object = HelpUtil.OBJECTS.getObject(HelpUtil.NMS.getClass("PacketPlayOutChat"), text, (byte) 2);
        HelpUtil.METHODS.invoke(
                HelpUtil.FIELDS.getValue(HelpUtil.METHODS.invoke(p, "getHandle"), "playerConnection"),
                "sendPacket", object);
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeInTime, int showTime,
                                 int fadeOutTime) {
        try {
            Object chatTitle = getNMSClassByName("IChatBaseComponent").getDeclaredClasses()[0]
                    .getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\"}");
            Constructor<?> titleConstructor = getNMSClassByName("PacketPlayOutTitle").getConstructor(
                    getNMSClassByName("PacketPlayOutTitle").getDeclaredClasses()[0],
                    getNMSClassByName("IChatBaseComponent"), int.class, int.class, int.class);
            Object packet = titleConstructor.newInstance(
                    getNMSClassByName("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null),
                    chatTitle, fadeInTime, showTime, fadeOutTime);

            Object chatsTitle = getNMSClassByName("IChatBaseComponent").getDeclaredClasses()[0]
                    .getMethod("a", String.class).invoke(null, "{\"text\": \"" + subtitle + "\"}");
            Constructor<?> timingTitleConstructor = getNMSClassByName("PacketPlayOutTitle").getConstructor(
                    getNMSClassByName("PacketPlayOutTitle").getDeclaredClasses()[0],
                    getNMSClassByName("IChatBaseComponent"), int.class, int.class, int.class);
            Object timingPacket = timingTitleConstructor.newInstance(
                    getNMSClassByName("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null),
                    chatsTitle, fadeInTime, showTime, fadeOutTime);

            sendPacket(player, packet);
            sendPacket(player, timingPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClassByName("Packet")).invoke(playerConnection,
                    packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Class<?> getNMSClassByName(String className) {
        try {
            return Class.forName("net.minecraft.server."
                    + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
