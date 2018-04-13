package uk.co.maboughey.mcnsanotes.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;
import uk.co.maboughey.mcnsanotes.database.DBNotes;
import uk.co.maboughey.mcnsanotes.type.Note;
import uk.co.maboughey.mcnsanotes.utils.Configuration;
import uk.co.maboughey.mcnsanotes.utils.Messages;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class NoteCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        //build note
        Note note = new Note();

        //Get arguments
        String target = (String) args.getOne("player").get();
        String noteString = (String) args.getAll("note").toString();

        //Get the note taker details
        if (src instanceof Player) {
            UUID uuid = ((Player) src).getUniqueId();
            note.noteTaker = uuid.toString();
        }
        else {
            note.noteTaker = "console";
        }



        note.server = Configuration.ServerName;
        note.note = noteString;
        note.notee = getUUID(target);
        DBNotes.writeNote(note);

        Messages.sendMessage(src, "&3Note for "+target+" has been recorded");
        return CommandResult.success();
    }

    public static String getUUID(String name) {
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
        }
        return null;
    }
}
