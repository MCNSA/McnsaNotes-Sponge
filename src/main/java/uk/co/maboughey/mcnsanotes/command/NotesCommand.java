package uk.co.maboughey.mcnsanotes.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.type.Note;
import uk.co.maboughey.mcnsanotes.utils.Messages;

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

            //get page number
            if (args.hasAny("page")) {
                //Looking at a page
                page = args.<Integer>getOne("page").get();
            }

            //Get notes from database
            LinkedList<Note> notes = McnsaNotes.DbManager.getNotes(target);

            //Display note
            Messages.sendMessage(src,
                    "&6Viewing page &F" + page + "&6 of &F" + target + "'s&6 notes");
            //end of command processing
            return CommandResult.success();
        }
    }
}
