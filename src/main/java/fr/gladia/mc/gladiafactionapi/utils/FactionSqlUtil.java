package fr.gladia.mc.gladiafactionapi.utils;

import fr.gladia.mc.gladiafactionapi.GladiaFactionAPI;
import org.bukkit.event.Listener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class FactionSqlUtil implements Listener {
    GladiaFactionAPI main;

    public FactionSqlUtil(GladiaFactionAPI main) {
        this.main = main;
    }

    public static String getPlayerFaction(String name) {
        try {
            PreparedStatement statement;
            statement = GladiaFactionAPI.me.getConnection().prepareStatement("SELECT * FROM " + GladiaFactionAPI.me.tableMember + " WHERE NAME=?");
            statement.setString(1, name);
            ResultSet results = statement.executeQuery();
            results.next();
            return (results.getString("FACTION"));
        } catch (SQLException e) {
            System.out.println(GladiaFactionAPI.logo + GladiaFactionAPI.me.conf.getConfig().getString("messages.cantGetData"));
            e.printStackTrace();
        }
        return "";
    }

    public static void setPlayerFaction(String name, String faction) {
        try {
            PreparedStatement statement;
            statement = GladiaFactionAPI.me.getConnection().prepareStatement("UPDATE " + GladiaFactionAPI.me.tableMember + " SET FACTION=? WHERE NAME=?");
            statement.setString(1, faction);
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(GladiaFactionAPI.logo + GladiaFactionAPI.me.conf.getConfig().getString("messages.cantUpdateData"));
            e.printStackTrace();
        }
    }

    public static int getFactionSize(String tag) {
        try {
            PreparedStatement statement;
            statement = GladiaFactionAPI.me.getConnection().prepareStatement("SELECT * FROM " + GladiaFactionAPI.me.tableFaction + " WHERE NAME=?");
            statement.setString(1, tag);
            ResultSet results = statement.executeQuery();
            results.next();
            try {
                return (results.getInt("PLAYERS"));
            } catch (SQLException e) {
            }

        } catch (SQLException e) {
            System.out.println(GladiaFactionAPI.logo + GladiaFactionAPI.me.conf.getConfig().getString("messages.cantGetData"));
            e.printStackTrace();
        }
        return 0;
    }

    public static void setFactionSize(String name, Integer size) {
        try {
            PreparedStatement statement;
            statement = GladiaFactionAPI.me.getConnection().prepareStatement("UPDATE " + GladiaFactionAPI.me.tableFaction + " SET PLAYERS=? WHERE NAME=?");
            statement.setInt(1, size);
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(GladiaFactionAPI.logo + GladiaFactionAPI.me.conf.getConfig().getString("messages.cantUpdateData"));
            e.printStackTrace();
        }
    }

    public static HashMap<String, Integer> getAllFactions() {
        try {
            Statement statement = GladiaFactionAPI.me.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + GladiaFactionAPI.me.tableFaction);

            HashMap<String, Integer> entries = new HashMap<String, Integer>();
            while(resultSet.next()) {
                System.out.println(resultSet.getString("NAME") + " : " + resultSet.getInt("POINTS"));
                entries.put(resultSet.getString("NAME"), resultSet.getInt("POINTS"));
            }
            return (entries);
        } catch (SQLException e) {
            System.out.println(GladiaFactionAPI.logo + GladiaFactionAPI.me.conf.getConfig().getString("messages.cantGetData"));
            e.printStackTrace();
        }
        return null;
    }

    public static int getFactionPoints(String tag) {
        try {
            PreparedStatement statement;
            statement = GladiaFactionAPI.me.getConnection().prepareStatement("SELECT * FROM " + GladiaFactionAPI.me.tableFaction + " WHERE NAME=?");
            statement.setString(1, tag);
            ResultSet results = statement.executeQuery();
            results.next();
            return (results.getInt("POINTS"));
        } catch (SQLException e) {
            System.out.println(GladiaFactionAPI.logo + GladiaFactionAPI.me.conf.getConfig().getString("messages.cantGetData"));
            e.printStackTrace();
        }
        return 0;
    }

    public static void setFactionPoints(String tag, Integer points) {
        try {
            PreparedStatement statement;
            statement = GladiaFactionAPI.me.getConnection().prepareStatement("UPDATE " + GladiaFactionAPI.me.tableFaction + " SET POINTS=? WHERE NAME=?");
            statement.setInt(1, points);
            statement.setString(2, tag);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(GladiaFactionAPI.logo + GladiaFactionAPI.me.conf.getConfig().getString("messages.cantUpdateData"));
            e.printStackTrace();
        }
    }

    public static boolean playerExists(String name) {
        try {
            PreparedStatement statement = GladiaFactionAPI.me.getConnection().prepareStatement("SELECT * FROM " + GladiaFactionAPI.me.tableMember + " WHERE NAME=?");
            statement.setString(1, name);

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(GladiaFactionAPI.me.logo + GladiaFactionAPI.me.conf.getConfig().getString("messages.failedPrepareStatement"));
            e.printStackTrace();
        }
        return false;
    }

    public static void createPlayer(String name) {
        try {
            PreparedStatement statement = GladiaFactionAPI.me.getConnection().prepareStatement("SELECT * FROM " + GladiaFactionAPI.me.tableMember + " WHERE NAME=?");
            statement.setString(1, name);
            ResultSet results = statement.executeQuery();
            results.next();
            String request = "INSERT INTO " + GladiaFactionAPI.me.tableMember + "(NAME,FACTION) VALUES (?,?)";

            PreparedStatement insert = GladiaFactionAPI.me.getConnection().prepareStatement(request);
            insert.setString(1, name);
            insert.setString(2, "");

            insert.executeUpdate();
        } catch (SQLException e) {
            System.out.println(GladiaFactionAPI.me.conf.getConfig().getString("messages.cantUpdateData"));
            e.printStackTrace();
        }
    }

    public static void createFaction(String tag) {
        try {
            PreparedStatement statement = GladiaFactionAPI.me.getConnection().prepareStatement("SELECT * FROM " + GladiaFactionAPI.me.tableMember + " WHERE NAME=?");
            statement.setString(1, tag);
            ResultSet results = statement.executeQuery();
            results.next();
            String request = "INSERT INTO " + GladiaFactionAPI.me.tableFaction + "(NAME,POINTS,PLAYERS) VALUES (?,?,?)";

            PreparedStatement insert = GladiaFactionAPI.me.getConnection().prepareStatement(request);
            insert.setString(1, tag);
            insert.setInt(2, 0);
            insert.setInt(3, 0);

            insert.executeUpdate();
        } catch (SQLException e) {
            System.out.println(GladiaFactionAPI.me.conf.getConfig().getString("messages.CantUpdateData"));
            e.printStackTrace();
        }
    }

    public static void removeFaction(String tag) {
        try {
            PreparedStatement statement = GladiaFactionAPI.me.getConnection().prepareStatement("DELETE FROM " + GladiaFactionAPI.me.tableFaction + " WHERE NAME=?");
            statement.setString(1, tag);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(GladiaFactionAPI.me.conf.getConfig().getString("messages.CantUpdateData"));
            e.printStackTrace();
        }
    }

}
