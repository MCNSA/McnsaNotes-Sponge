package uk.co.maboughey.mcnsanotes.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import uk.co.maboughey.mcnsanotes.utils.PlayerNameArgument;

public class CommandManager {

    private PluginContainer plugin;

    public CommandManager(PluginContainer plugin){
        this.plugin = plugin;

        reloadCommand();
        notesCommand();
        deleteCommand();
        newNoteCommand();
        recentNotesCommand();
        viewStatsCommand();
    }

    public void reloadCommand() {
        CommandSpec reloadCommand = CommandSpec.builder()
                .description(Text.of("Reload the configuration for MCNSA Notes"))
                .permission("mcnsanotes.mod.reload")
                .executor(new ReloadCommand())
                .build();

        Sponge.getCommandManager().register(this.plugin, reloadCommand, "notesreload", "reloadnotes");
    }
    public void notesCommand() {
        CommandSpec notesCommand = CommandSpec.builder()
                .description(Text.of("View notes about a player"))
                .permission("mcnsanotes.mod")
                .executor(new NotesCommand())
                .arguments(
                        GenericArguments.onlyOne(new PlayerNameArgument(Text.of("player"))),
                        GenericArguments.optional(GenericArguments.integer(Text.of("page")))
                )
                .build();

        Sponge.getCommandManager().register(this.plugin, notesCommand, "notes", "viewnotes");
    }
    public void deleteCommand() {
        CommandSpec deleteCommand = CommandSpec.builder()
                .description(Text.of("Delete a player's note"))
                .permission("mcnsanotes.mod.delete")
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
                .permission("mcnsanotes.mod")
                .executor(new NoteCommand())
                .arguments(
                        GenericArguments.onlyOne(new PlayerNameArgument(Text.of("player"))),
                        GenericArguments.allOf(GenericArguments.string(Text.of("note")))
                )
                .build();

        Sponge.getCommandManager().register(this.plugin, newNoteCommand, "note", "n");
    }
    public void recentNotesCommand() {
        CommandSpec recentNotesCommand = CommandSpec.builder()
                .description(Text.of("View all recent notes written"))
                .permission("mcnsanotes.mod")
                .executor(new RecentNotesCommand())
                .arguments(
                        GenericArguments.optional(GenericArguments.integer(Text.of("page")))
                )
                .build();

        Sponge.getCommandManager().register(this.plugin, recentNotesCommand, "recentnotes", "rn");
    }

    public void viewStatsCommand() {
        CommandSpec newStatsCommand = CommandSpec.builder()
                .description(Text.of("View your stats"))
                .permission("mcnsanotes.player")
                .executor(new StatsCommand())
                .arguments(
                        GenericArguments.optional(new PlayerNameArgument(Text.of("player")))
                )
                .build();

        Sponge.getCommandManager().register(this.plugin, newStatsCommand, "stats", "viewstats");
    }
}
