package com.battleasya.admin360;

import com.battleasya.admin360.bstats.Metrics;
import com.battleasya.admin360.commands.A3;
import com.battleasya.admin360.commands.B3;
import com.battleasya.admin360.datasource.DataSource;
import com.battleasya.admin360.datasource.MySQL;
import com.battleasya.admin360.datasource.SQLite;
import com.battleasya.admin360.entities.Admin;
import com.battleasya.admin360.handler.RequestHandler;
import com.battleasya.admin360.listener.JoinLeaveEvent;
import com.battleasya.admin360.handler.Config;
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

        getConfiguration().initConfig();

        /* Check Config */
        getConfiguration().checkConfig();

        /* Fetch Config */
        getConfiguration().fetchConfig();

        /* Register Commands */
        getCommand("admin360").setExecutor(new B3(this));
        getCommand("ticket").setExecutor(new A3(this));

        /* Register Listeners */
        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(), this);

        /* Initialise RequestHandler */
        rh = new RequestHandler(this);

        /* Initialise DataSource */
        if (Config.ds_mysql) {
            ds = new MySQL(this);
        } else {
            ds = new SQLite(this);
        }

        /* Connect to Database */
        getDataSource().connect(Config.ds_host, Config.ds_port, Config.ds_database
                , Config.ds_username, Config.ds_password);

        /* Setup Database */
        getDataSource().setUp();

        /* register bstats */
        new Metrics(this, 19710);
        getLogger().info("Starting Metrics. Opt-out using the global bStats config.");

        /* e.g. 1.20.1-R0.1-SNAPSHOT */
        try {
            version = Integer.parseInt(getServer().getBukkitVersion().split("-")[0].split("\\.")[1]);
        } catch (Exception e) {
            version = 8;
        }

        /* Load Admin in list (useful on reloads) */
        Admin.refreshAdmLst();
        
    }

    @Override
    public void onDisable() {
        getDataSource().disconnect();
    }

    public RequestHandler getRequestHandler() {
        return rh;
    }

    public DataSource getDataSource() {
        return ds;
    }

    public Config getConfiguration() {
        return config;
    }

    public static int getServerVersion() {
        return version;
    }

}
