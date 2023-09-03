package com.battleasya.Admin360;

import com.battleasya.Admin360.bstats.Metrics;
import com.battleasya.Admin360.commands.A3;
import com.battleasya.Admin360.commands.B3;
import com.battleasya.Admin360.datasource.DataSource;
import com.battleasya.Admin360.datasource.MySQL;
import com.battleasya.Admin360.datasource.SQLite;
import com.battleasya.Admin360.entities.Admin;
import com.battleasya.Admin360.handler.RequestHandler;
import com.battleasya.Admin360.listener.JoinLeaveEvent;
import com.battleasya.Admin360.handler.Config;
import org.bukkit.plugin.java.JavaPlugin;

public class Admin360 extends JavaPlugin {

    public DataSource ds;

    public Config config;

    public RequestHandler rh;

    public static int version;

    @Override
    public void onEnable() {

        /* Setup Default Config if not exists */
        saveDefaultConfig();

        /* Initialise Config */
        config = new Config(this);
        config.initConfig();

        /* Check Config */
        config.checkConfig();

        /* Fetch Config */
        config.fetchConfig();

        /* Initialise Command Executor */
        getCommand("admin360").setExecutor(new B3(this));
        getCommand("ticket").setExecutor(new A3(this));

        /* Initialise RequestHandler */
        rh = new RequestHandler(this);

        /* Initialise DataSource */
        if (Config.useMysql) {
            ds = new MySQL();
        } else {
            ds = new SQLite();
        }

        /* Connect to Database */
        boolean ok = ds.connect(Config.host, Config.port, Config.database, Config.username, Config.password);

        if (!ok) {
            getServer().getPluginManager().disablePlugin(this);
        }

        ds.setUp(); // build database
        ds.addColumn(); // update database

        /* Initialise Listeners */
        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(), this);

        /* Load Admin in list (useful on reloads) */
        Admin.refreshList();

        /* register bstats */
        new Metrics(this, 19710);
        getLogger().info("Starting Metrics. Opt-out using the global bStats config.");

        /* e.g. 1.20.1-R0.1-SNAPSHOT */
        try {
            version = Integer.parseInt(getServer().getBukkitVersion().split("-")[0].split("\\.")[1]);
        } catch (Exception e) {
            version = 8;
        }
        
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

    public static int getServerVersion() {
        return version;
    }

}
