package com.forgottenartsstudios.harmony.TestsAndExamples;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SlashTime {
    private static final String DATE_FORMAT = "MM-dd-yyyy hh:mm:ss a";

    public static void slashCommand(SlashCommandInteractionEvent event) {
        String[] args = event.getCommandString().split("\\s");
        if(args.length == 1){
            buildEmbedNoTime(event);
        }
        if (args.length == 5) {
            //System.out.println(args[0] + args[1] + args[2] + args[3]);
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            String dateInString = args[2];
            String hourInString = args[3];
            String noonInString = args[4];
            //String newDateInString = null;
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

    public static void buildEmbedNoTime(SlashCommandInteractionEvent event){
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
        MessageEmbed timeEmbed = time.build();
        event.replyEmbeds(timeEmbed).setEphemeral(true).queue();
    }
    public static void buildEmbedWithTime(SlashCommandInteractionEvent event, Date date){
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
        event.reply("Inputted Global Times").queue();

        InteractionHook hook = event.getHook(); // This is a special webhook that allows you to send messages without having permissions in the channel and also allows ephemeral messages
        hook.setEphemeral(true); // All messages here will now be ephemeral implicitly
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessageEmbeds(time.build()).queue();
    }
    public static String tzf(String timeZone) {
        Date today = new Date();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss z");
        df.setTimeZone(TimeZone.getTimeZone(timeZone));
        return df.format(today);
    }

    public static String tzf(String timeZone, Date time) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss z");
        df.setTimeZone(TimeZone.getTimeZone(timeZone));
        return df.format(time);
    }
}