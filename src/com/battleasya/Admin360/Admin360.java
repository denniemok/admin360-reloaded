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

public class Admin360 extends JavaPlugin {

    public DataSource ds;

    public Config config;

    public RequestHandler rh;

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
            ds = new MySQL_DataSource();
        } else {
            ds = new SQLite_DataSource();
        }

        /* Connect to Database */
        ds.connect(Config.host, Config.port, Config.database, Config.username, Config.password);
        ds.setUp(); // build database
        ds.addColumn(); // update database

        /* Initialise Listeners */
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);

        /* Load Admin in list (useful on reloads) */
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
