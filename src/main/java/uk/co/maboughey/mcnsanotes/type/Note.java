package uk.co.maboughey.mcnsanotes.type;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import uk.co.maboughey.mcnsanotes.database.DBuuid;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class Note {
    public Integer id;
    public Date noteDate;
    public String noteTaker;
    public String notee;
    public String note;
    public String server;

    public String getNoteTaker() {
        return getNameFromUUID(noteTaker);
    }

    public String getNotee() {
        return getNameFromUUID(notee);
    }

    public String getNameFromUUID(String uuid) {
        String name = null;

        //Try getting user from server of players that have logged in before
        UserStorageService uss = Sponge.getServiceManager().provideUnchecked(UserStorageService.class);
        Optional<User> oUser = uss.get(UUID.fromString(uuid));
        if (oUser.isPresent()) {
            //Person is online
            name = oUser.get().getName();
        }
        else {
            //UUID not found.. look in db
            name = DBuuid.getUsername(uuid);
            if (name == null)
                name = "Console";
        }
        return name;
    }
}
