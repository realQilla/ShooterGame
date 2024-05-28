package net.qilla.shootergame.Database;

import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;

public class Database {

    private static final String url = "jdbc:mysql://localhost:3306/players";
    private static final String username = "Shooter";
    private static final String password = "P2Z3vGXq7d&6Qnzpst";

    private static Connection connection = null;

    public Database() throws SQLException {
        if(connection == null) {
            connection = DriverManager.getConnection(url, username, password);
            Bukkit.getLogger().info("Successfully connected to the shooter database.");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void createTables() {

    }

    public void getPlayerRank(UUID playerUUID) throws SQLException {
        String sql = "SELECT player_rank FROM player_stats WHERE uuid = " + playerUUID.toString();

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, playerUUID.toString());

        ResultSet results = statement.executeQuery();
        if(results.next()) {

        }
    }
 }
