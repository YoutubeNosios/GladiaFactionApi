package fr.gladia.mc.gladiafactionapi;

import com.massivecraft.factions.Factions;
import fr.gladia.mc.gladiafactionapi.commands.CommandFactionPoints;
import fr.gladia.mc.gladiafactionapi.commands.CommandPlayerPoints;
import fr.gladia.mc.gladiafactionapi.commands.CommandRanking;
import fr.gladia.mc.gladiafactionapi.listeners.FactionListeners;
import fr.gladia.mc.gladiafactionapi.utils.FactionSqlUtil;
import fr.gladia.mc.gladiafactionapi.utils.YamlUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

public final class GladiaFactionAPI extends JavaPlugin {

    protected Factions massiveFaction;
    public static GladiaFactionAPI me;
    public YamlUtil conf;
    private Connection connection;
    public String host, database, username, password;
    public String tableMember, tableFaction;
    public int port;
    public static String logo = "§a§l[Gladia]";

    @Override
    public void onEnable() {
        conf = new YamlUtil(this, "config.yml");
        massiveFaction = Factions.getInstance();
        me = this;
        host = conf.getConfig().getString("mysql.host");
        port = conf.getConfig().getInt("mysql.port");
        database = conf.getConfig().getString("mysql.database");
        username = conf.getConfig().getString("mysql.username");
        password = conf.getConfig().getString("mysql.password");
        tableFaction = conf.getConfig().getString("mysql.tableFaction");
        tableMember = conf.getConfig().getString("mysql.tableMember");

        getCommand("classement").setExecutor(new CommandRanking(this));
        getCommand("factionpoints").setExecutor(new CommandFactionPoints(this));
        getCommand("playerpoints").setExecutor(new CommandPlayerPoints(this));

        //SQL
        mysqlSetup();

        //LISTENERS
        getServer().getPluginManager().registerEvents(new CommandRanking(this),this);
        getServer().getPluginManager().registerEvents(new FactionSqlUtil(this),this);
        getServer().getPluginManager().registerEvents(new FactionListeners(),this);
    }

    @Override
    public void onDisable() {

    }

    public Connection openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }
        synchronized (this) {
            if (connection != null && !connection.isClosed())
                return connection;
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql//" + host + ":" + port + "/" + database, username, password);
            return connection;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public  void setConnection(java.sql.Connection connection) {
        me.connection = connection;
    }

    public void mysqlSetup() {
        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);

                String requestFaction = "CREATE TABLE IF NOT EXISTS " + tableFaction + " (NAME VARCHAR(50), POINTS INT, PLAYERS INT)";
                PreparedStatement factionStatement = connection.prepareStatement(requestFaction);
                factionStatement.executeUpdate();

                String requestMember = "CREATE TABLE IF NOT EXISTS " + tableMember + " (NAME VARCHAR(50), FACTION VARCHAR(50))";
                PreparedStatement memberStatement = connection.prepareStatement(requestMember);
                memberStatement.executeUpdate();

                System.out.println(logo + conf.getConfig().getString("messages.connectionSuccess"));
            }
        } catch (SQLException e) {
            System.out.println(logo + conf.getConfig().getString("messages.failedConnectDatabase"));
        } catch (ClassNotFoundException e) {
            System.out.println(logo + conf.getConfig().getString("messages.classNotFound"));
        }
    }

    protected GladiaFactionAPI get() {
        return me;
    }

    protected Factions getMassiveFaction() {
        return massiveFaction;
    }
}
