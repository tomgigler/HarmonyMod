package com.forgottenartsstudios.harmony.Main;

import com.forgottenartsstudios.harmony.Data.SQLCommands;

import static com.forgottenartsstudios.harmony.Main.Harmony.out;

import net.dv8tion.jda.api.entities.Guild;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Database {
    public static Connection connectProductionDB() throws IOException, SQLException {
        Connection c = null;
        Properties prop = Variables.prop;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(prop.getProperty("moddburl"), prop.getProperty("moddbuser"), prop.getProperty("moddbpass"));
            out.info("Connected to database.");
        } catch (ClassNotFoundException e) {
            out.debug("Database connection error.");
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return c;
    }
    public static void createDB() throws Exception{
        Connection c = null;
        String sql;
        Statement statement = null;
        Properties prop = Variables.prop;
        // Connect to default Postgres Database
        out.debug("Connecting to default DB");
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(prop.getProperty("defaultdburl"),
                    prop.getProperty("defaultdbname"), prop.getProperty("defaultdbpass"));
        } catch (ClassNotFoundException e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        out.debug("Opened " + c.getCatalog() + " successfully");

        // Check to see if new DB exists, if not then create it.
        //System.out.println("Checking for HarmonyRPG DB");
        statement = c.createStatement();
        sql = "SELECT * FROM pg_database WHERE datname = 'harmonymod'";
        ResultSet result = statement.executeQuery(sql);
        if(!result.next()) {
            out.debug("HarmonyMOD DB not found. Creating.");
            statement.execute("CREATE DATABASE harmonymod");
        }
        c.close();
    }
    public static void dropTables() throws IOException, SQLException {
        Connection c = connectProductionDB();
        try {
            Statement statement = c.createStatement();
            statement.execute("DROP table if exists botvars");
            out.debug("DROP table if exists botvars");
        } catch (SQLException e){
            printSQLException(e);
        }
        try {
            Statement statement = c.createStatement();
            statement.execute("DROP table if exists guilds");
            out.debug("DROP table if exists guilds");
        } catch (SQLException e){
            printSQLException(e);
        }
        c.close();
    }
    public static void createTables() throws Exception {
        Connection c = connectProductionDB();
        try {
            out.debug(SQLCommands.createBotVarTableSQL);
            Statement statement = c.createStatement();
            statement.execute(SQLCommands.createBotVarTableSQL);
        } catch (SQLException e){
            printSQLException(e);
        }
        try {
            out.debug(SQLCommands.createGuildsTableSQL);
            Statement statement = c.createStatement();
            statement.execute(SQLCommands.createGuildsTableSQL);
        } catch (SQLException e){
            printSQLException(e);
        }
        c.close();
    }
    public static void enterDefaultBotParameters() throws Exception {
        Connection c = connectProductionDB();
        boolean check = false;
        out.debug("Checking for default params.");
        try {
            Statement statement = c.createStatement();
            ResultSet rs = statement.executeQuery("select * from botvars");
            if (rs.next()){
                check = true;
            }
        } catch (SQLException e){
            printSQLException(e);
        }
        if (!check) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            out.debug("Default parameters not found. Setting.");
            String setDefaultParameters = "INSERT INTO botvars" +
                    " (ID, DEFAULTPREFIX, LASTSTART, OWNER) VALUES " +
                    " (?, ?, ?, ?);";
            PreparedStatement preparedStatement = c.prepareStatement(setDefaultParameters);
            {
                preparedStatement.setInt(1, 1);
                preparedStatement.setString(2, Variables.getPrefix());
                preparedStatement.setString(3, dtf.format(now));
                preparedStatement.setLong(4, Long.parseLong(Variables.prop.getProperty("botowner")));
                // Step 3: Execute the query or update query
                preparedStatement.executeUpdate();
            }
            //System.out.println("Set default parameters.");
        } else {
            //System.out.println("Default paramters found. Updating vars.");
            Statement statement = c.createStatement();
            ResultSet rs = statement.executeQuery("SELECT defaultprefix FROM botvars WHERE ID = 1");
            while (rs.next())
                Variables.setPrefix(rs.getString(1));
        }
        c.close();
    }
    public static void enterDefaultGuildParameters(Guild guild) throws Exception {
        Connection c = connectProductionDB();
        boolean check = false;
        //System.out.println("Checking for default params.");
        try {
            Statement statement = c.createStatement();
            ResultSet rs = statement.executeQuery("select * from guilds where id = " + guild.getId());
            if (rs.next()){
                check = true;
            }
        } catch (SQLException e){
            printSQLException(e);
        }
        if (!check) {
            out.info("Not in the DB. Creating " + guild);
            String setDefaultParameters = "INSERT INTO guilds" +
                    " (ID, NAME, PREFIX, SANDBOX, MODROLE) VALUES " +
                    " (?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = c.prepareStatement(setDefaultParameters);
            {
                preparedStatement.setLong(1, guild.getIdLong());
                preparedStatement.setString(2, guild.getName());
                preparedStatement.setString(3, Variables.getPrefix());
                //Leaving this commented out will make the default playerrole null. Need to set.
                //preparedStatement.setLong(4, guild.getIdLong());
                if (guild.getDefaultChannel() != null){
                    preparedStatement.setLong(4, guild.getDefaultChannel().getIdLong());
                }
                //FIX//THIS DOESN'T DO ANYTHING
                preparedStatement.setLong(5, guild.getOwnerIdLong());
                // Step 3: Execute the query or update query
                preparedStatement.executeUpdate();
            }
        }
        c.close();
    }
    public static void updatePrefix(String id, String arg) throws Exception {
        String str = "UPDATE guilds SET prefix = ('" + arg + "') WHERE ID = " + id;
        updateDBValue(str);
    }
    public static void updateModRole(String id, String arg) throws Exception {
        String str = "UPDATE guilds SET modrole = ('" + arg + "') WHERE ID = " + id;
        updateDBValue(str);
    }
    public static void updateDBValue(String str) throws SQLException, IOException {
        Connection c = connectProductionDB();
        try {
            Statement statement = c.createStatement();
            statement.execute(str);
        } catch (SQLException e){
            printSQLException(e);
        }
        c.close();
    }
    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
