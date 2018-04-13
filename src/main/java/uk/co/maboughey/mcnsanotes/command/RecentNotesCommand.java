package uk.co.maboughey.mcnsanotes.command;

import com.google.common.math.IntMath;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.database.DBNotes;
import uk.co.maboughey.mcnsanotes.type.Note;
import uk.co.maboughey.mcnsanotes.utils.Messages;

import java.math.RoundingMode;
import java.util.LinkedList;

public class RecentNotesCommand implements CommandExecutor {
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

            //get page number
            if (args.hasAny("page")) {
                //Looking at a page
                page = args.<Integer>getOne("page").get();
            }

            //Get notes from database
            LinkedList<Note> notes = DBNotes.getRecentNotes(page);

            //Display note
            Messages.sendMessage(src,
                    "&6Viewing page &F" + page + "/"+ getNumPages() +"&6 of recent notes");

            //Check if there are notes
            if (notes.size() > 0){
                for (int i = 0; i < notes.size(); i++) {
                    Note note = notes.get(i);
                    Messages.sendMessage(src, "&6ID: &F"+note.id +" &6On: &F"+note.getNotee()+" &6By: &F"+note.getNoteTaker());
                    Messages.sendMessage(src, "&6Server: &F"+note.server+" &6Date: &F"+note.noteDate);
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

    public int getNumPages() {
        int pages = 1;
        //Get amount of notes
        int count = DBNotes.getNotesCount();
        //See how many pages that is
        pages = IntMath.divide(count, 5, RoundingMode.CEILING);

        return pages;
    }
}
