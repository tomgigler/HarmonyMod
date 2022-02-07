package com.forgottenartsstudios.harmony.Main;

import com.forgottenartsstudios.harmony.Data.GuildData;
import com.forgottenartsstudios.harmony.Data.Module;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.forgottenartsstudios.harmony.Main.Database.connectProductionDB;
import static com.forgottenartsstudios.harmony.Main.Harmony.jda;

public class Variables {


    //DEFAULT BOT VARIABLES
    public static String prefix = "=";
    public static Properties prop;
    //PARSE PROPERTY FILE
    static {
        try {
            prop = parseConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getPrefix() { return prefix; }
    public static void setPrefix(String prefix) { Variables.prefix = prefix; }

    //HARMONY MOD VARIABLES
    public static List<Guild> guilds;
    public static List<GuildData> guildData = new ArrayList<>();
    public static List<User> users;
    public static List<Member> members;

    public static List<Module> modules = new ArrayList<>();

    public static void loadModules(){
        Module moderation = new Module();
        moderation.setName("moderation");
        moderation.setRequiredLevel(1);
        Module utility = new Module();
        utility.setName("utility");
        moderation.setRequiredLevel(1);
        Module fun = new Module();
        fun.setName("fun");
        moderation.setRequiredLevel(1);
    }
    public static void loadGuildList(){
        guilds = jda.getGuilds();
    }
    public static void loadGuildDataList() throws Exception {
        Connection c = connectProductionDB();
        for (int x = 0; x < Variables.guilds.size(); x++) {
            Database.enterDefaultGuildParameters(Variables.guilds.get(x));
            GuildData guildData = new GuildData();
            guildData.setID(guilds.get(x).getIdLong());
            guildData.setName(guilds.get(x).getName());
            try {
                Statement statement = c.createStatement();
                ResultSet rs = statement.executeQuery("select * from guilds where id = " + guilds.get(x).getId());
                if (rs.next()) {
                    guildData.setPrefix(rs.getString("prefix")); //getPrefixFromDB
                    guildData.setModRole(rs.getString("modrole"));
                }
            } catch (SQLException e){
                Database.printSQLException(e);
            }
            Variables.guildData.add(guildData);
        }
        c.close();
    }
    public static Properties parseConfig() throws IOException {
        Properties prop  = new Properties();
        InputStream is = null;
        try {
            is = Harmony.class.getClassLoader().getResourceAsStream("config.properties");
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
    public static Properties parseModuleConfig() throws IOException {
        Properties prop  = new Properties();
        InputStream is = null;
        try {
            is = Harmony.class.getClassLoader().getResourceAsStream("moduleconfig.properties");
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
