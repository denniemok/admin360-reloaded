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

        /* Initialise Command Executor */
        getCommand("admin360").setExecutor(new B3(this));
        getCommand("ticket").setExecutor(new A3(this));

        /* Initialise RequestHandler */
        rh = new RequestHandler(this);

        /* Initialise DataSource */
        if (Config.useMysql) {
            ds = new MySQL(this);
        } else {
            ds = new SQLite(this);
        }

        /* Connect to Database */
        boolean ok = getDataSource().connect(Config.host, Config.port, Config.database
                , Config.username, Config.password);

        if (!ok) {
            getDataSource().disconnect();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        /* Setup Database */
        getDataSource().setUp();

        /* Initialise Listeners */
        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(), this);

        /* Load Admin in list (useful on reloads) */
        Admin.refreshAdmLst();

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

    public Config getConfiguration() {
        return config;
    }

    public static int getServerVersion() {
        return version;
    }

}
