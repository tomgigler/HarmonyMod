package com.forgottenartsstudios.harmony.Data;

public class GuildData {
    Long ID;
    String name;
    String prefix;
    String[] alias;
    String modRole;
    int guildLevel;
    int modmodule;
    int utilmodule;

    public int getModmodule() {
        return modmodule;
    }
    public void setModmodule(int modmodule) {
        this.modmodule = modmodule;
    }
    public int getUtilmodule() {
        return utilmodule;
    }
    public void setUtilmodule(int utilmodule) {
        this.utilmodule = utilmodule;
    }
    public String[] getAlias() {
        return alias;
    }
    public void setAlias(String[] alias) {
        this.alias = alias;
    }
    public int getGuildLevel() {
        return guildLevel;
    }

    public void setGuildLevel(int guildLevel) {
        this.guildLevel = guildLevel;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getModRole() {
        return modRole;
    }

    public void setModRole(String modRole) {
        this.modRole = modRole;
    }
}
