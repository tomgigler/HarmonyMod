package com.forgottenartsstudios.harmony.Util;


import com.forgottenartsstudios.harmony.Main.Database;
import com.forgottenartsstudios.harmony.Main.Harmony;
import com.forgottenartsstudios.harmony.Main.Variables;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class GuildOwnerCommands extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) { return; }
        String prefix = Harmony.getGuildPrefix(event);
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if (args[0].startsWith(prefix)) {
            String arg0 = args[0].replace(prefix, "");
            //Check if command author has Manage Server permissions in the guild.
            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_SERVER)) {
                switch (arg0) {
                    case "setprefix":
                        setPrefix(event, args);
                        break;
                    case "purge":
                        purge(event, args);
                        break;
                    case "managertest":
                        managerTest(event);
                        break;
                    case "setmod":
                        setModRole(event, args);
                        break;
                    case "shutdown":
                        Harmony.shutdown();
                        break;
                    default:
                        //stuff
                }
            }
        }
    }
    public static void setPrefix(MessageReceivedEvent event, String[] args){
        if(args.length == 2) {
            if (args[1].length() > 3) {
                event.getMessage().reply("Prefix should less than 4 Characters").queue();
                return;
            }
            try {
                Database.updatePrefix(event.getGuild().getId(), args[1]);
                for(int x = 0; x < Variables.guildData.size(); x++) {
                    if(Variables.guildData.get(x).getID() == event.getGuild().getIdLong() ) {
                        Variables.guildData.get(x).setPrefix(args[1]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            event.getMessage().replyEmbeds(basicMessageEmbed("Prefix updated to " + args[1])).queue();
        }else event.getMessage().reply("Invalid syntax").queue();
    }
    public static void purge(MessageReceivedEvent event, String[] args){
        int values = Integer.parseInt(args[1]);
        event.getMessage().delete().queue();
        List<Message> messages = event.getChannel().getHistory().retrievePast(values).complete();
        event.getTextChannel().deleteMessages(messages).queue();
    }
    public static void setModRole(MessageReceivedEvent event, String[] args){
        Guild guild = event.getGuild();
        try {
            Database.updateModRole(guild.getId(), args[1]);
            String msg = "Mod role set.";
            event.getMessage().replyEmbeds(basicMessageEmbed(msg)).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void managerTest(MessageReceivedEvent event){
        String msg = "You are a manager.";
        event.getMessage().replyEmbeds(basicMessageEmbed(msg)).queue();
    }
    public static MessageEmbed basicMessageEmbed(String str){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription(str);
        embed.setColor(Color.CYAN);
        return embed.build();
    }
}
