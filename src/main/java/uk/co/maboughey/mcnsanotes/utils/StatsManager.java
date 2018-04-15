package uk.co.maboughey.mcnsanotes.utils;

import uk.co.maboughey.mcnsanotes.database.DBStats;
import uk.co.maboughey.mcnsanotes.type.Stat;

import java.util.ArrayList;
import java.util.List;

public class StatsManager {
    public List<Stat> stats = new ArrayList<Stat>();

    public Stat getStat(String uuid) {
        for (int i = 0; i < stats.size(); i++) {
            if (stats.get(i).uuid.contains(uuid))
                return stats.get(i);
        }
        return null;
    }

    public void addStat(Stat stat) {
        this.stats.add(stat);
    }

    public void removeStat(String uuid) {
        Stat stat = getStat(uuid);
        if (stat != null) {
            //update time played
            stat.timeOnServer += (System.currentTimeMillis() / 1000) - stat.loginTime;
            //save to DB
            DBStats.saveStat(stat);
            //remove from tracking
            stats.remove(stat);
        }
    }
    public void saveChanged() {
        for (int i = 0; i < stats.size(); i++) {
            if (stats.get(i).changed) {
                //update time played
                stats.get(i).timeOnServer += (System.currentTimeMillis() / 1000) - stats.get(i).loginTime;
                DBStats.saveStat(stats.get(i));
                stats.get(i).changed = false;
            }
        }
    }
}
