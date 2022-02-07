package com.forgottenartsstudios.harmony.Main;

import com.forgottenartsstudios.harmony.Data.GuildData;

import com.forgottenartsstudios.harmony.Util.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import org.slf4j.LoggerFactory;

import org.jetbrains.annotations.NotNull;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


import static com.forgottenartsstudios.harmony.Main.Database.connectProductionDB;

import javax.annotation.Nonnull;

public class Harmony extends ListenerAdapter {

    public static final Logger out = LoggerFactory.getLogger("Harmony");
    static {
        SLF4JBridgeHandler.install();
    }
    public static JDA jda;
    public static void main(String[] args) throws Exception {
        String str = Variables.parseConfig().getProperty("bottoken");
        jda = JDABuilder.create(str, EnumSet.allOf(GatewayIntent.class))
                .addEventListeners(new Harmony()).build();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("Loading..."));
        jda.setAutoReconnect(false);
        jda.awaitReady();
        start();
    }
    public static void start() throws Exception {
        Database.createDB(); //Creates new DB if not exists and connect to it
        Database.dropTables();
        Database.createTables(); //Create tables if not exists using SQLCommands
        Database.enterDefaultBotParameters();
        registerListeners();
        registerGlobalCommands();
        registerGuildCommands();
        Variables.loadGuildList();
        Variables.loadGuildDataList();
        out.info("Ready");
        jda.getPresence().setActivity(Activity.playing("Harmonizing"));
    }
    public static void registerGlobalCommands(){
        //GLOBAL COMMANDS
        List<CommandData> cmds = new ArrayList<>();
        cmds.add(Commands.slash("ping", "Calculate the ping of the bot."));
        cmds.add(Commands.slash("prefix", "Returns bot prefix"));
        cmds.add(Commands.slash("mod", "Brings up Mod Control Panel")
                .addOption(OptionType.MENTIONABLE, "modmention", "User, Role, or Member."));
        //Add commands global
        jda.updateCommands().addCommands(cmds).queue();
    }
    public static void registerGuildCommands(){
        //Add commands global
        Guild guild = jda.getGuildById(852740148247658526L);
        assert guild != null;
        guild.updateCommands()
                .addCommands(Commands.slash("test", "Brings up Mod Control Panel"))
                .addCommands(Commands.slash("mod", "Brings up Mod Control Panel")
                    .addOption(OptionType.MENTIONABLE, "modmention", "User, Role, or Member."))
                .queue();
    }
    public static void registerListeners(){
        //Add Command Events Listeners
        out.debug("Registering Harmony MOD Command Event Listeners");
        jda.addEventListener(new BotOwnerCommands());
        jda.addEventListener(new GuildOwnerCommands());
        jda.addEventListener(new GuildModCommands());
        jda.addEventListener(new GuildModSlashCommands());

        //Add Event Listeners
        jda.addEventListener(new ButtonProcessor());
    }
    public static String getGuildPrefix(MessageReceivedEvent event){
        String prefix = null;
        for(int x = 0; x < Variables.guildData.size(); x++) {
            if(Variables.guildData.get(x).getID() == event.getGuild().getIdLong()) {
                prefix = Variables.guildData.get(x).getPrefix();
            }
        }
        return prefix;
    }
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        Variables.loadGuildList();
        Guild guild = event.getGuild();
        Connection c = null;
        try {
            c = connectProductionDB();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        try {
            Database.enterDefaultGuildParameters(guild);
        } catch (Exception e) {
            e.printStackTrace();
        }
        GuildData guildData = new GuildData();
        guildData.setID(event.getGuild().getIdLong());
        guildData.setName(event.getGuild().getName());
        try {
            assert c != null;
            Statement statement = c.createStatement();
            ResultSet rs = statement.executeQuery("select * from guilds where id = " + event.getGuild().getIdLong());
            if (rs.next()) {
                guildData.setPrefix(rs.getString("prefix")); //getPrefixFromDB
                guildData.setModRole(rs.getString("modrole"));
            }
        } catch (SQLException e){
            Database.printSQLException(e);
        }
        Variables.guildData.add(guildData);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        out.info("Joined: " + event.getGuild() + " at " + dtf.format(now));
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        out.info("Left: " + event.getGuild() + " at " + dtf.format(now));

    }
    @Override
    public void onReady(ReadyEvent event)
    {
        out.info("Total Guilds: " + event.getGuildTotalCount());
        out.info("Available Guilds: " + event.getGuildAvailableCount());
        out.info("Unavailable Guilds: " + event.getGuildUnavailableCount());
    }
    public static void shutdown(){
        jda.shutdown();
    }
    @Override
    public void onShutdown(@Nonnull ShutdownEvent event){
        jda.getPresence().setActivity(Activity.playing("Shutting Down"));
    }
}