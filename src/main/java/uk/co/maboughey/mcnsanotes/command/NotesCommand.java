package uk.co.maboughey.mcnsanotes.command;

import com.google.common.math.IntMath;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.database.DBNotes;
import uk.co.maboughey.mcnsanotes.database.DBuuid;
import uk.co.maboughey.mcnsanotes.type.Note;
import uk.co.maboughey.mcnsanotes.utils.Messages;

import java.math.RoundingMode;
import java.util.LinkedList;

public class NotesCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        //Check if command is enabled or not
        if (!McnsaNotes.isEnabled) {
            //It isnt, tell the user
            Messages.sendMessage(src,"&4Notes plugin is currently disabled. Please check config file and reload");
            McnsaNotes.log.warn("Notes is currently disabled. Please check config file and do /notesreload");
            return CommandResult.success();
        }
        else {
            int page = 1;
            //Plugin is still enabled. Process command

            //Get the information needed
            String target = (String) args.getOne("player").get();
            String uuid = DBuuid.getUUID(target);

            //get page number
            if (args.hasAny("page")) {
                //Looking at a page
                page = args.<Integer>getOne("page").get();
            }

            //Get notes from database
            LinkedList<Note> notes = DBNotes.getNotes(target, page);

            //Display note
            Messages.sendMessage(src,
                    "&6Viewing page &F" + page + "/"+ getNumPages(uuid) +"&6 of &F" + target + "'s&6 notes");

            //Check if there are notes
            if (notes.size() > 0){
                for (int i = 0; i < notes.size(); i++) {
                    Note note = notes.get(i);
                    Messages.sendMessage(src, "&6ID: &F"+note.id + "&6Server: &F"+note.server +" &6By: &F"+note.getNoteTaker()+" &6Date: &F"+note.noteDate);
                    Messages.sendMessage(src, "&6Note: &F"+note.note);
                }

            }
            else {
                Messages.sendMessage(src, "There are no notes");
            }
            //end of command processing
            return CommandResult.success();
        }
    }

    public int getNumPages(String uuid) {
        int pages = 1;
        //Get amount of notes
        int count = DBNotes.getNotesCount(uuid);
        //See how many pages that is
        pages = IntMath.divide(count, 5, RoundingMode.CEILING);

        return pages;
    }
}
