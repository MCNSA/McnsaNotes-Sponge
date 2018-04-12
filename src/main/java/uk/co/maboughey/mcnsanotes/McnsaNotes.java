package uk.co.maboughey.mcnsanotes;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;
import uk.co.maboughey.mcnsanotes.command.CommandManager;
import uk.co.maboughey.mcnsanotes.database.DatabaseManager;
import uk.co.maboughey.mcnsanotes.listeners.PlayerListener;
import uk.co.maboughey.mcnsanotes.utils.Configuration;
import uk.co.maboughey.mcnsanotes.utils.Log;


import java.nio.file.Path;

@Plugin(id = "mcnsanotes", name = "MCNSA Notes", version = "1.0-Sponge")
public class McnsaNotes {
    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    @Inject
    private PluginContainer plugin;

    public static Boolean isEnabled = true;

    public static Configuration config;
    public static Log log;
    public static DatabaseManager DbManager;


    @Listener
    public void preInit(GamePreInitializationEvent event){
        log = new Log(logger);
        logger.info("Loading Configuration");

        PluginManager pluginManager = Sponge.getPluginManager();
        Asset asset = Sponge.getAssetManager().getAsset(plugin, "config.conf").get();
        config = new Configuration(configDir, asset);

        log.info("Loading commands");
        CommandManager CommandManager = new CommandManager(plugin);

        log.info("Loading database");
        DbManager = new DatabaseManager();
        DbManager.tablesCreate();

        log.info("Starting listeners");
        Sponge.getEventManager().registerListeners(plugin, new PlayerListener());

    }

    @Listener
    public void onServerStart(GameStartedServerEvent event){
        logger.info("Welcome to MCNSANotes!");
    }

}
