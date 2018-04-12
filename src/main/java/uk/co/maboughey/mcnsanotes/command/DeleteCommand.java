package uk.co.maboughey.mcnsanotes.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.database.DBNotes;
import uk.co.maboughey.mcnsanotes.utils.Messages;

public class DeleteCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!McnsaNotes.isEnabled) {
            //It isnt, tell the user
            Messages.sendMessage(src,"&4Notes plugin is currently disabled. Please check config file and reload");
            McnsaNotes.log.warn("Notes is currently disabled. Please check config file and do /notesreload");
            return CommandResult.success();
        }
        else {
            //Run the command

            //Get the id
            int id = args.<Integer>getOne("id").get();

            if (DBNotes.deleteNote(id)){
                Messages.sendMessage(src, "Note Deleted");
            }
            else {
                Messages.sendMessage(src, "Error deleting note (Are you sure you put in the correct id?)");
            }
        }

        return CommandResult.success();
    }
}
