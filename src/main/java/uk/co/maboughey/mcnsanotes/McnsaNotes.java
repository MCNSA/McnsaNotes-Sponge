package uk.co.maboughey.mcnsanotes;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import uk.co.maboughey.mcnsanotes.command.CommandManager;
import uk.co.maboughey.mcnsanotes.database.DBuuid;
import uk.co.maboughey.mcnsanotes.database.DatabaseManager;
import uk.co.maboughey.mcnsanotes.listeners.PlayerListener;
import uk.co.maboughey.mcnsanotes.utils.Configuration;
import uk.co.maboughey.mcnsanotes.utils.Log;
import uk.co.maboughey.mcnsanotes.utils.StatsManager;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
    public static StatsManager StatsManager;

    public static HashMap<Integer, String> uuids;
    public static HashMap<Integer, String> usernames;


    @Listener
    public void preInit(GamePreInitializationEvent event){
        log = new Log(logger);
        logger.info("Loading Configuration");
        config = new Configuration(configDir);

        //Support for local copy of knownUsernames
        uuids = new HashMap<Integer, String>();
        usernames = new HashMap<Integer, String>();

        log.info("Loading commands");
        CommandManager CommandManager = new CommandManager(plugin);

        log.info("Loading database");
        DbManager = new DatabaseManager();
        DbManager.tablesCreate();

        DBuuid.getAllUuids();

        //Stats
        StatsManager = new StatsManager();



        log.info("Starting listeners");
        Sponge.getEventManager().registerListeners(plugin, new PlayerListener());

        //Save all stats to DB every 5 mins
        Task.Builder taskBuilder = Task.builder();
        taskBuilder.execute(new Runnable() {
                                @Override
                                public void run() {
                                    log.info("saving stats to DB");
                                    StatsManager.saveChanged();
                                }
                            }
        ).interval(10, TimeUnit.MINUTES)
        .submit(this);
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event){
        logger.info("Welcome to MCNSANotes!");
    }

}
