package com.forgottenartsstudios.harmony.TestsAndExamples;

import com.forgottenartsstudios.harmony.Main.Harmony;
import com.forgottenartsstudios.harmony.Main.Variables;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class BasicMessageReceivedEvents extends ListenerAdapter {
    private static final String DATE_FORMAT = "MM-dd-yyyy hh:mm:ss a";
    Random rand = new Random();
    private static long inputTime;

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {return;}
        String prefix = Harmony.getGuildPrefix(event);
        System.out.println(prefix);
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        // #Ping
        if(args[0].equalsIgnoreCase(prefix + "ping") ) {
            long processing = new Date().getTime() - inputTime;
            long ping = event.getJDA().getGatewayPing();
            event.getTextChannel().sendMessageEmbeds(new EmbedBuilder().setColor(getColorByPing(ping)).setDescription(String.format(":ping_pong:   **Pong!**\n\nThe bot took `%s` milliseconds to response.\nIt took `%s` milliseconds to parse the command and the ping is `%s` milliseconds.",
                    processing + ping, processing, ping)).build()).queue();
        }
        // #Roll
        if (args[0].equalsIgnoreCase(prefix + "roll")) {
                int numDice = Integer.parseInt(args[1]);
                int diceSides = Integer.parseInt(args[2]);
                int firstRoll = rand.nextInt(diceSides + 1);
                int secondRoll = rand.nextInt(diceSides + 1);
                event.getTextChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Color.ORANGE).setDescription(String.format(":game_die::game_die:   **Roll!**\n\nYou rolled `%s` `%s` sided dice for `%s` & `%s` for a total of `%s`.",
                        numDice, diceSides, firstRoll, secondRoll, firstRoll+secondRoll)).build()).queue();
        }
        // #Time No Args
        if(args.length == 1){
            if (args[0].equalsIgnoreCase(Variables.prefix + "time")) {
                buildEmbedNoTime(event);
            }
        }
        // #Time w/Args
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase(Variables.prefix + "time")) {
                SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
                String dateInString = args[1];
                String hourInString = args[2];
                String noonInString = args[3];
                StringBuilder builderString = new StringBuilder();
                builderString.append(dateInString + " ");
                builderString.append(hourInString + " ");
                builderString.append(noonInString);
                String newDateInString = builderString.toString();
                Date date = null;
                try {
                    date = formatter.parse(newDateInString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                buildEmbedWithTime(event, date);
            }
        }
    }
    public static void setInputTime(long inputTimeLong) {
        inputTime = inputTimeLong;
    }
    private Color getColorByPing(long ping) {
        if (ping < 100)
            return Color.cyan;
        if (ping < 400)
            return Color.green;
        if (ping < 700)
            return Color.yellow;
        if (ping < 1000)
            return Color.orange;
        return Color.red;
    }
    public void buildEmbedNoTime(MessageReceivedEvent event){
        EmbedBuilder time = new EmbedBuilder();
        time.setColor(0x66d8ff);
        time.setTitle("Times across the world");
        time.setImage("https://image.freepik.com/free-vector/polygonal-map-digital-globe-map-blue-polygons-earth-maps-world-internet-connection-3d-grid-illustration_102902-902.jpg");
        time.addField("New York:", tzf("America/New_York"), true);
        time.addField("Chicago:", tzf("America/Chicago"), true);
        time.addField("Denver:", tzf("America/Denver"), true);
        time.addField("Phoenix:", tzf("America/Phoenix"), true);
        time.addField("Los Angeles:", tzf("America/Los_Angeles"), true);
        time.addField("Anchorage:", tzf("America/Anchorage"), true);
        time.addField("Honolulu:", tzf("Pacific/Honolulu"), true);
        time.addField("London:", tzf("Europe/London"), true);
        time.addField("Stockholm:", tzf("Europe/Stockholm"), true);
        time.addField("Perth:", tzf("Australia/Perth"), true);
        time.addField("Adelaide:", tzf("Australia/Adelaide"), true);
        time.addField("Sydney:", tzf("Australia/Sydney"), true);
        time.addField("Seoul:", tzf("Asia/Seoul"), true);
        time.addField("Berlin:", tzf("Europe/Berlin"), true);
        time.addField("Hong Kong:", tzf("Asia/Hong_Kong"), true);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessageEmbeds(time.build()).queue();
    }
    public void buildEmbedWithTime(MessageReceivedEvent event, Date date){
        EmbedBuilder time = new EmbedBuilder();
        time.setColor(0x66d8ff);
        time.setTitle("Times across the world");
        time.setImage("https://image.freepik.com/free-vector/polygonal-map-digital-globe-map-blue-polygons-earth-maps-world-internet-connection-3d-grid-illustration_102902-902.jpg");
        time.addField("New York:", tzf("America/New_York", date), true);
        time.addField("Chicago:", tzf("America/Chicago", date), true);
        time.addField("Denver:", tzf("America/Denver", date), true);
        time.addField("Phoenix:", tzf("America/Phoenix", date), true);
        time.addField("Los Angeles:", tzf("America/Los_Angeles", date), true);
        time.addField("Anchorage:", tzf("America/Anchorage", date), true);
        time.addField("Honolulu:", tzf("Pacific/Honolulu", date), true);
        time.addField("London:", tzf("Europe/London", date), true);
        time.addField("Stockholm:", tzf("Europe/Stockholm", date), true);
        time.addField("Perth:", tzf("Australia/Perth", date), true);
        time.addField("Adelaide:", tzf("Australia/Adelaide", date), true);
        time.addField("Sydney:", tzf("Australia/Sydney", date), true);
        time.addField("Seoul:", tzf("Asia/Seoul", date), true);
        time.addField("Berlin:", tzf("Europe/Berlin", date), true);
        time.addField("Hong Kong:", tzf("Asia/Hong_Kong", date), true);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessageEmbeds(time.build()).queue();
    }
    public String tzf(String timeZone) {
        Date today = new Date();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss z");
        df.setTimeZone(TimeZone.getTimeZone(timeZone));
        return df.format(today);
    }

    public String tzf(String timeZone, Date time) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss z");
        df.setTimeZone(TimeZone.getTimeZone(timeZone));
        return df.format(time);
    }
}
