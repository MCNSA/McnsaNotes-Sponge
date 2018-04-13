package uk.co.maboughey.mcnsanotes.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.database.DBuuid;

import java.util.Optional;

public class PlayerListener {

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player p){
        //Get player details
        String uuid = p.getUniqueId().toString();
        String name = p.getName();

        //Add the details to the database
        DBuuid.addUUID(uuid, name);

        //TODO: Stats Handling
        //TODO: Tag Handling
    }

    @Listener
    public void onPlayerQuit(ClientConnectionEvent.Disconnect event) {
        String uuid = event.getTargetEntity().getUniqueId().toString();
        //TODO: Stats handling
    }

    @Listener
    public void onBlockBreak(ChangeBlockEvent.Break event){
        Optional<Player> optPlayer = event.getCause().first(Player.class);
        if (optPlayer.isPresent() && optPlayer.get().isOnline()) {
            String uuid = optPlayer.get().getUniqueId().toString();
            //TODO: Stats Handling
        }
    }
}
