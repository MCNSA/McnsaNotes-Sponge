package uk.co.maboughey.mcnsanotes.command;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.utils.Messages;

public class ReloadCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        McnsaNotes.config.load();
        Messages.sendMessage(src, "&2Reloaded Config for MCNSA Notes");
        McnsaNotes.isEnabled = true;
        return CommandResult.success();
    }
}
