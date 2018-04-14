package uk.co.maboughey.mcnsanotes.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.database.DBStats;
import uk.co.maboughey.mcnsanotes.database.DBuuid;
import uk.co.maboughey.mcnsanotes.type.Stat;

import java.util.Optional;

public class PlayerListener {

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player p){
        //Get player details
        String uuid = p.getUniqueId().toString();
        String name = p.getName();

        //Add the details to the database
        DBuuid.addUUID(uuid, name);

        //get player stats
        Stat stat = DBStats.getStat(uuid);
        if (stat == null) {
            McnsaNotes.log.info("Creating new stat");
            stat = new Stat(uuid);
            DBStats.saveNewStat(stat);
        }
        McnsaNotes.StatsManager.addStat(stat);

        //TODO: Tag Handling
    }

    @Listener
    public void onPlayerQuit(ClientConnectionEvent.Disconnect event) {
        String uuid = event.getTargetEntity().getUniqueId().toString();
        McnsaNotes.StatsManager.removeStat(uuid);

    }

    @Listener
    public void onBlockBreak(ChangeBlockEvent.Break event){
        Optional<Player> optPlayer = event.getCause().first(Player.class);
        if (optPlayer.isPresent() && optPlayer.get().isOnline()) {
            String uuid = optPlayer.get().getUniqueId().toString();
            McnsaNotes.StatsManager.getStat(uuid).changed = true;
            McnsaNotes.StatsManager.getStat(uuid).blocksBroken += 1;
        }
    }
}
