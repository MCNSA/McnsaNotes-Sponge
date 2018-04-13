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
        return DBuuid.getNameFromUUID(noteTaker);
    }

    public String getNotee() {
        return DBuuid.getNameFromUUID(notee);
    }

}
