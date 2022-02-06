package com.forgottenartsstudios.harmony.Data;

public class SQLCommands {
    public static final String createBotVarTableSQL = "CREATE TABLE IF NOT EXISTS botvars " +
            "(ID BIGINT PRIMARY KEY ," +
            " DEFAULTPREFIX TEXT, " +
            " LASTSTART TEXT, " +
            " OWNER BIGINT) ";
    public static final String createGuildsTableSQL = "CREATE TABLE IF NOT EXISTS guilds " +
            " (ID BIGINT PRIMARY KEY, " +
            " NAME TEXT, " +
            " PREFIX TEXT, " +
            " ALIAS TEXT[], " +
            " SANDBOX BIGINT, " +
            " MODROLE BIGINT) ";

}
