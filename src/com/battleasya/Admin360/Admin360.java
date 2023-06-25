package com.battleasya.Admin360;

import com.battleasya.Admin360.commands.A3;
import com.battleasya.Admin360.commands.B3;
import com.battleasya.Admin360.datasource.DataSource;
import com.battleasya.Admin360.datasource.MySQL_DataSource;
import com.battleasya.Admin360.datasource.SQLite_DataSource;
import com.battleasya.Admin360.entities.Admin;
import com.battleasya.Admin360.listener.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Admin360 extends JavaPlugin {

    public static DataSource ds;
    public static int completedToday = 0;
    public static Admin360 instance;

    @Override
    public void onEnable() {

        instance = this;

        //Setup default config if a config does not exist
        new File("plugins/Admin360-Reloaded").mkdir();
        saveDefaultConfig();

        if (!getConfig().isSet("version") || !(getConfig().getString("version").equalsIgnoreCase("8.1.2"))) {
            File configFile = new File(getDataFolder(), "config.yml");
            configFile.renameTo(new File(getDataFolder(), "config_old.yml"));
            saveDefaultConfig();
            System.out.println("[Admin360-Reloaded] Finished renaming and regenerating the config file.");
        } else {
            System.out.println("[Admin360-Reloaded] The config file is at the latest version.");
        }

        reloadConfig();

        //Set admin360 and request command executor
        getCommand("admin360").setExecutor(new B3(this));
        getCommand("ticket").setExecutor(new A3(this));

        //Connect to the database
        if (getConfig().getBoolean("use-mysql")) {
            ds = new MySQL_DataSource();
        } else {
            ds = new SQLite_DataSource();
        }
        ds.connect(getConfig().getString("host"), getConfig().getString("port"), getConfig().getString("database"),
                getConfig().getString("username"), getConfig().getString("password"));
        ds.setUp();
        ds.addColumn();

        //Set up listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        //Load Admin in list (useful on reloads)
        Admin.refreshAdminList();
        
    }

    @Override
    public void onDisable() {
        ds.disconnect();
    }

    public static Admin360 getInstance(){
        return instance;
    }

}
