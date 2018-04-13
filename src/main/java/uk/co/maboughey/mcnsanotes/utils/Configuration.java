package uk.co.maboughey.mcnsanotes.utils;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import uk.co.maboughey.mcnsanotes.McnsaNotes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Configuration {
    private final Path configDir;
    private Path configFile;
    private CommentedConfigurationNode configNode;
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    public static String DBHost;
    public static String DBName;
    public static String DBUser;
    public static String DBPassword;
    public static String ServerName;

    public Configuration(Path confDir) {
        this.configDir = confDir;
        this.configFile = Paths.get(configDir+"/config.conf");
        configLoader = HoconConfigurationLoader.builder().setPath(configFile).build();

        checkConfigDir();
        checkConfFile();
    }

    public void checkConfigDir() {
        if (!Files.exists(configDir)) {
            try {
                Files.createDirectories(configDir);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkConfFile() {
        if (!Files.exists(configFile)) try {
            McnsaNotes.log.info("&4Loading default config. Plugin is disabled until you reload");
            McnsaNotes.isEnabled = false;
            Files.createFile(configFile);
            configNode = configLoader.load();
            configNode.getNode("DatabaseHost").setValue("localhost");
            configNode.getNode("DatabaseName").setValue("mcnsanotes");
            configNode.getNode("DatabaseUser").setValue("mcnsanotes");
            configNode.getNode("DatabasePassword").setValue("mcnsanotes");
            configNode.getNode("ServerName").setValue("Server");
            configLoader.save(configNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        else {
            McnsaNotes.isEnabled = true;
            McnsaNotes.log.warn("Notes plugin is now enabled");
            load();
        }
    }
    public void load(){
        try{
            configNode = configLoader.load();
        }catch(IOException e){
            e.printStackTrace();
        }
        finally {
            DBHost = configNode.getNode("DatabaseHost").getString();
            DBName = configNode.getNode("DatabaseName").getString();
            DBUser = configNode.getNode("DatabaseUser").getString();
            DBPassword = configNode.getNode("DatabasePassword").getString();
            ServerName = configNode.getNode("ServerName").getString();

            showConfig();
        }
    }
    public Path getConfigDir(){
        return configDir;
    }
    public CommentedConfigurationNode get(){
        return configNode;
    }
    public void showConfig() {
        McnsaNotes.log.info("Current settings: DBH:"+DBHost+", DBN:"+DBName+", DBU:"+DBUser);
    }
    public static String getDatabaseString() {
        return "jdbc:mysql://"+DBHost+"/"+DBName;
    }
}
