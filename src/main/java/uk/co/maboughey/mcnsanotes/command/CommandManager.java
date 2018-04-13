package uk.co.maboughey.mcnsanotes.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

public class CommandManager {

    private PluginContainer plugin;

    public CommandManager(PluginContainer plugin){
        this.plugin = plugin;

        reloadCommand();
        notesCommand();
        deleteCommand();
        newNoteCommand();
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
    public void deleteCommand() {
        CommandSpec deleteCommand = CommandSpec.builder()
                .description(Text.of("Delete a player's note"))
                .permission("mcnsanotes.delete")
                .executor(new DeleteCommand())
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("id")))
                )
                .build();

        Sponge.getCommandManager().register(this.plugin, deleteCommand, "deletenote", "nd");
    }

    public void newNoteCommand() {
        CommandSpec newNoteCommand = CommandSpec.builder()
                .description(Text.of("Write a note for a player"))
                .permission("mcnsanotes.note")
                .executor(new NoteCommand())
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("player"))),
                        GenericArguments.allOf(GenericArguments.string(Text.of("note")))
                )
                .build();

        Sponge.getCommandManager().register(this.plugin, newNoteCommand, "note", "n");
    }
}
