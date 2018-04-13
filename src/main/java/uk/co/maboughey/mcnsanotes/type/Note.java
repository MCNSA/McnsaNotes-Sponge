package uk.co.maboughey.mcnsanotes.type;

import uk.co.maboughey.mcnsanotes.database.DBuuid;

import java.util.Date;

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
