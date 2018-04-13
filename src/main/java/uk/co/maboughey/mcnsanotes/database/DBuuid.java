package uk.co.maboughey.mcnsanotes.database;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;
import uk.co.maboughey.mcnsanotes.McnsaNotes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class DBuuid {
    public static String getUUID(String player)
    {
        String uuid = getUUIDFromName(player);
        if (uuid !=null) {
            return uuid;
        }
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
            McnsaNotes.log.error("Database Error getting uuid: " + e.getMessage());
        }
        return null;
    }

    public static String getUsername(String UUID) {

        String user = getNameFromUUID(UUID);
        if (user != null) {
            return user;
        }
        String name = null;
        Connection connect = DatabaseManager.getConnection();

        try {
            PreparedStatement statement = connect.prepareStatement("SELECT name FROM knownUsernames WHERE uuid=? ORDER BY id DESC LIMIT 1");
            statement.setString(1, UUID);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                name = results.getString("name");
            }
            connect.close();
        } catch (SQLException e) {
            McnsaNotes.log.error("Database Error getting name from UUID: "+e.getLocalizedMessage());
        }

        return name;
    }
    public static String getNameFromUUID(String uuid) {
        String name = uuid;


        //Try getting user from server of players that have logged in before
        try {
            UserStorageService uss = Sponge.getServiceManager().provideUnchecked(UserStorageService.class);
            Optional<User> oUser = uss.get(UUID.fromString(uuid));
            if (oUser.isPresent()) {
                name = oUser.get().getName();
            }
        }
        catch (IllegalArgumentException e) {
            //Do nothing
        }
        return name;
    }
    private static String getUUIDFromName(String name) {
        Optional<Player> player = Sponge.getServer().getPlayer(name);
        if (player.isPresent()) {
            return player.get().getUniqueId().toString();
        }
        else {
            //need to try and get from somewhere else
            UserStorageService uss = Sponge.getServiceManager().provideUnchecked(UserStorageService.class);
            Collection<GameProfile> results = uss.match(name);
            if (results.size() > 0) {
                GameProfile profile = results.iterator().next();
                return profile.getUniqueId().toString();
            }
            else {
                //TODO: get uuid from Mojang api
            }
        }
        return null;
    }
    public static void addUUID(String uuid, String player) {
        try {
            //Get the connection
            Connection connect = DatabaseManager.getConnection();

            //Check if uuid and name relationship is in the database
            PreparedStatement statement = connect.prepareStatement("SELECT * FROM knownUsernames WHERE uuid=? AND name=?");
            statement.setString(1, uuid);
            statement.setString(2, player);
            ResultSet results = statement.executeQuery();

            if (!results.next()) {
                //Not in DB, Lets add it
                PreparedStatement put = connect.prepareStatement("INSERT INTO knownUsernames (uuid, name) VALUES (?,?)");
                put.setString(1, uuid);
                put.setString(2, player);
                put.executeUpdate();
            }
            connect.close();
        }
        catch (SQLException e) {
            McnsaNotes.log.error("Error adding uuid into database: "+e.getLocalizedMessage());
        }
    }
}
