package com.battleasya.Admin360.handler;

public enum Permission {
    CREATE_TICKET("admin360.player.basic"),
    VIEW_STATUS("admin360.player.status"),
    VIEW_STATS("admin360.player.stats"),
    ATTEND_TICKET("admin360.staff.basic"),
    TELEPORT("admin360.staff.tp"),
    VIEW_INFO("admin360.staff.info"),
    TRANSFER_TICKET("admin360.staff.redirect"),
    DROP_TICKET("admin360.staff.drop"),
    PURGE_TICKET("admin360.staff.purge"),
    REMOVE_TICKET("admin360.staff.delete"),
    VIEW_HP_STATS("admin360.staff.hpstats"),
    VIEW_HP_TOP("admin360.staff.hptop"),
    VIEW_HISTORY("admin360.staff.history"),
    RESET_HP_STATS("admin360.staff.hpreset"),
    RELOAD_CONFIG("admin360.staff.reload");

    private final String permissionNode;

    Permission(String permissionNode) {
        this.permissionNode = permissionNode;
    }

    public String getString(){
        return this.permissionNode;
    }

}
