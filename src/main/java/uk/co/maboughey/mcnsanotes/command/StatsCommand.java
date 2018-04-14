package uk.co.maboughey.mcnsanotes.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.database.DBStats;
import uk.co.maboughey.mcnsanotes.database.DBuuid;
import uk.co.maboughey.mcnsanotes.type.Stat;
import uk.co.maboughey.mcnsanotes.utils.Messages;

public class StatsCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String uuid = null;
        if (src instanceof Player) {
            //its a player sending the command, lets get their uuid
            Player player = (Player) src;
            uuid = player.getUniqueId().toString();
        }
        else {
            //if its console, and no player specified, tell thme
            if (!args.hasAny("player") && src instanceof ConsoleSource) {
                Messages.sendMessage(src, "&4You dont have any stats as console. Silly Templar :), Specify a player");
                return CommandResult.success();
            }
        }
        //Check if person is trying to view someone else
        if (args.hasAny("player")) {
            //Check permission, if no permission just ignore it
            if (src.hasPermission("mcnsanotes.stats.other")){
                //We have permission
                String other = (String) args.getOne("player").get();
                uuid = DBuuid.getUUID(other);
            }
        }

        //Get the stat
        Stat stat = McnsaNotes.StatsManager.getStat(uuid);
        if (stat == null) {
            //Stat is not loaded
            stat = DBStats.getStat(uuid);

            //Sanity check
            if (stat == null) {
                Messages.sendMessage(src, "&4Could not find stats");
                return CommandResult.success();
            }
        }
        //we can assume that we have the stats now, lets display it
        Messages.sendMessage(src,"&6------------Stats for &F" + stat.getName() +"&6------------");
        Messages.sendMessage(src,"&6Date first joined: &F" + stat.dateJoined + "&6  Logins: &F" + stat.numJoins);
        Messages.sendMessage(src,"&6Time online: &F" +stat.getTimeOnServer());
        //TODO: When kicks are fired "&6Times kicked: &F" + stat.numKicks +
        Messages.sendMessage(src,"&6Times died: &F"+ stat.numDeaths);
        Messages.sendMessage(src,"&6Blocks placed: &F" + stat.blocksPlaced + " &6Blocks broken: &F"+ stat.blocksBroken);
        return CommandResult.success();
    }
}
