package uk.co.maboughey.mcnsanotes.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class CommandManager {

    private PluginContainer plugin;

    public CommandManager(PluginContainer plugin){
        this.plugin = plugin;

        reloadCommand();
        notesCommand();
    }

    public void reloadCommand() {
        CommandSpec reloadCommand = CommandSpec.builder()
                .description(Text.of("Reload the configuration for MCNSA Notes"))
                .permission("mcnsanotes.reload")
                .executor(new ReloadCommand())
                .build();

        Sponge.getCommandManager().register(this.plugin, reloadCommand, "notesreload", "reloadnotes");
    }
    public void notesCommand() {
        CommandSpec notesCommand = CommandSpec.builder()
                .description(Text.of("View notes about a player"))
                .permission("mcnsanotes.viewnotes")
                .executor(new NotesCommand())
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("player"))),
                        GenericArguments.optional(GenericArguments.integer(Text.of("page")))
                )
                .build();

        Sponge.getCommandManager().register(this.plugin, notesCommand, "notes", "viewnotes");
    }
}
