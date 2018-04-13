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
import uk.co.maboughey.mcnsanotes.database.DBuuid;
import uk.co.maboughey.mcnsanotes.type.Note;
import uk.co.maboughey.mcnsanotes.utils.Configuration;
import uk.co.maboughey.mcnsanotes.utils.Messages;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

public class NoteCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        //build note
        Note note = new Note();

        //Get arguments
        String target = (String) args.getOne("player").get();
        Collection<String> noteString = args.getAll("note");
        String message = String.join(" ", noteString);

        //Get the note taker details
        if (src instanceof Player) {
            UUID uuid = ((Player) src).getUniqueId();
            note.noteTaker = uuid.toString();
        }
        else {
            note.noteTaker = "console";
        }



        note.server = Configuration.ServerName;
        note.note = message;
        note.notee = DBuuid.getUUID(target);
        DBNotes.writeNote(note);

        Messages.sendMessage(src, "&3Note for "+DBuuid.getNameFromUUID(note.notee)+" has been recorded");
        return CommandResult.success();
    }


}
