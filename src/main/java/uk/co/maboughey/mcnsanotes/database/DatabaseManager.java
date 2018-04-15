package uk.co.maboughey.mcnsanotes.database;

import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.utils.Configuration;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static Connection connect = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement= null;
    private static ResultSet resultSet = null;

    public static Connection getConnection() {
        if (connect == null)
            connect();
        return connect;
    }

    public static void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Properties connProperties = new Properties();
            connProperties.put("user", Configuration.DBUser);
            connProperties.put("password", Configuration.DBPassword);
            connProperties.put("autoReconnect", true);
            connProperties.put("maxReconnects","4");

            connect = DriverManager.getConnection(Configuration.getDatabaseString(), connProperties);
            McnsaNotes.log.info("Connection to Database Established");

        }
        catch (SQLException e){
            McnsaNotes.log.error("Database Error connecting to databse: "+ e.getMessage());
            connect = null;
        }
        catch (ClassNotFoundException e) {
            McnsaNotes.log.error("Could not find mysql connector");
        }
    }

    public static void close() {
        try {
            if(resultSet != null) resultSet.close();
            if(statement != null) statement.close();
            if(preparedStatement != null) preparedStatement.close();
            if(connect != null) connect.close();
        }
        catch(Exception e) {
            McnsaNotes.log.error("Database exception during close. Message was: "+e.getMessage());
        }
    }

    public void tablesCreate() {
        try {
            // load the database
            connect();

            // create the tables if they does not exist
            preparedStatement = connect.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS notes ( " +
                            "id int(6) NOT NULL AUTO_INCREMENT, " +
                            "date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                            "uuid varchar(50) NOT NULL, "+
                            "noter_uuid varchar(50) NOT NULL, " +
                            "server varchar(20) NOT NULL, " +
                            "note TEXT NOT NULL,"+
                            "PRIMARY KEY (id));");
            preparedStatement.executeUpdate();

            preparedStatement = connect.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS "+McnsaNotes.config.ServerName+"_stats ("+
                            "uuid varchar(50) NOT NULL, "+
                            "firstjoined timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                            "timeonserver bigint NOT NULL, " +
                            "blocksplaced bigint(10) NOT NULL, " +
                            "blocksbroken bigint(10) NOT NULL, " +
                            "numdeaths bigint(10) NOT NULL, " +
                            "numkicks bigint(10) NOT NULL, " +
                            "modreqs bigint(10) NOT NULL, " +
                            "logins bigint(10) NOT NULL);");
            preparedStatement.executeUpdate();

            preparedStatement = connect.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS knownUsernames ("+
                            "id int(6) NOT NULL AUTO_INCREMENT, "+
                            "uuid varchar(50) NOT NULL, "+
                            "name varchar(20) NOT NULL, "+
                            "PRIMARY KEY (id));"
            );
            preparedStatement.executeUpdate();

            preparedStatement = connect.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS taggedPlayers (" +
                            "id int(6) NOT NULL AUTO_INCREMENT, " +
                            "uuid varchar(50) NOT NULL, " +
                            "noter_uuid varchar(50), " +
                            "reason TEXT NOT NULL, " +
                            "PRIMARY KEY (id))"
            );
            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            McnsaNotes.log.error("Database Exception, Disabling plugin. Message: "+e.getMessage());
            McnsaNotes.isEnabled = false;
        }
    }





}
