package uk.co.maboughey.mcnsanotes.database;

import uk.co.maboughey.mcnsanotes.McnsaNotes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBuuid {
    public static String getUUID(String player)
    {
        Connection connect = DatabaseManager.getConnection();
        try
        {
            PreparedStatement statement = connect.prepareStatement("SELECT uuid FROM knownUsernames WHERE name LIKE ? ORDER BY id DESC LIMIT 1");
            statement.setString(1, "%" + player + "%");
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                String result = results.getString("uuid");
                DatabaseManager.close();
                return result;
            }
        }
        catch (SQLException e)
        {
            McnsaNotes.log.error("Error getting uuid: " + e.getMessage());
        }
        return null;
    }

    public static String getUsername(String UUID) {
        Connection connect = DatabaseManager.getConnection();

        try {
            PreparedStatement statement = connect.prepareStatement("SELECT name FROM knownUsernames WHERE uuid=? ORDER BY id DESC LIMIT 1");
            statement.setString(1, UUID);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return results.getString("name");
            }
        } catch (SQLException e) {
            McnsaNotes.log.error("Database Error getting name from UUID: "+e.getLocalizedMessage());
        }
        return null;
    }
}
