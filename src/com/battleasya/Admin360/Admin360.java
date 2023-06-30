package com.battleasya.Admin360;

import com.battleasya.Admin360.commands.A3;
import com.battleasya.Admin360.commands.B3;
import com.battleasya.Admin360.datasource.DataSource;
import com.battleasya.Admin360.datasource.MySQL_DataSource;
import com.battleasya.Admin360.datasource.SQLite_DataSource;
import com.battleasya.Admin360.entities.Admin;
import com.battleasya.Admin360.handler.RequestHandler;
import com.battleasya.Admin360.listener.JoinLeaveListener;
import com.battleasya.Admin360.util.Config;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Admin360 extends JavaPlugin {

    public DataSource ds;

    public Config config;

    public RequestHandler rh;

    @Override
    public void onEnable() {

        // Setup Default Config if not exists
        saveDefaultConfig();

        // Initialise Config instance
        config = new Config(this);
        config.initConfig();

        // Check Config
        if (!Config.version_set || !Config.version_latest) {
            File configFile = new File(getDataFolder(), "config.yml");
            boolean rename = configFile.renameTo(new File(getDataFolder(), "config_old.yml"));
            if (rename) {
                System.out.println("[Admin360-Reloaded] Renamed the old config file to config_old.yml.");
            } else {
                System.out.println("[Admin360-Reloaded] Failed to rename the old config file to config_old.yml.");
            }
            saveDefaultConfig();
        } else {
            System.out.println("[Admin360-Reloaded] config.yml is at the latest version.");
        }

        config.fetchConfig();

        // Initialise Command Executor instance
        getCommand("admin360").setExecutor(new B3(this));
        getCommand("ticket").setExecutor(new A3(this));

        // Initialise RequestHandler instance
        rh = new RequestHandler(this);

        // Initialise DataSource instance
        if (Config.useMysql) {
            ds = new MySQL_DataSource();
        } else {
            ds = new SQLite_DataSource();
        }

        // Connect to Database
        ds.connect(Config.host, Config.port, Config.database, Config.username, Config.password);
        ds.setUp(); // build database
        ds.addColumn(); // update database

        // Initialise listeners
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);

        // Load Admin in list (useful on reloads)
        Admin.refreshAdminList();
        
    }

    @Override
    public void onDisable() {
        ds.disconnect();
    }

    public RequestHandler getRequestHandler() {
        return rh;
    }

    public DataSource getDataSource() {
        return ds;
    }

    public Config getConfig2() {
        return config;
    }

}
