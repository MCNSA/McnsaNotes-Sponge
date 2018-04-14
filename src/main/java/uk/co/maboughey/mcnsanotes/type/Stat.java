package uk.co.maboughey.mcnsanotes.type;

import uk.co.maboughey.mcnsanotes.database.DBuuid;

import java.util.Date;

public class Stat {
    public boolean changed = false;
    public String name = new String("");
    public Date dateJoined = new Date();
    public long timeOnServer = 0;
    public long numJoins = 0;
    public long numKicks = 0;
    public long blocksBroken = 0;
    public long blocksPlaced = 0;
    public long modRequests = 0;
    public String uuid;
    public long numDeaths = 0;

    public long loginTime;

    public Stat(String uuid) {
        loginTime = System.currentTimeMillis();
        this.uuid = uuid;
    }
    public Stat() {}

    public String getName() {
        return DBuuid.getNameFromUUID(this.uuid);
    }
    public String getTimeOnServer()
    {
        long currentPlayTime = 0L;
        if (this.loginTime != 0L)
        {
            long currenttime = System.currentTimeMillis();
            currentPlayTime = this.timeOnServer + (currenttime - this.loginTime);
        }
        else
        {
            currentPlayTime = this.timeOnServer;
        }
        currentPlayTime /= 1000L;
        long days = currentPlayTime / 86400L;
        long hours = currentPlayTime / 3600L - days * 24L;
        long minutes = currentPlayTime / 60L - days * 1440L - hours * 60L;
        long seconds = currentPlayTime % 60L;
        String string = days + " Days " + hours + " Hours " + minutes + " Minutes " + seconds + " Seconds";
        return string;
    }

}
