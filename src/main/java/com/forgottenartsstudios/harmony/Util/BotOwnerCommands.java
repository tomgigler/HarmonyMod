package com.forgottenartsstudios.harmony.Util;

import com.forgottenartsstudios.harmony.Main.Harmony;
import com.forgottenartsstudios.harmony.Main.Variables;
import net.dv8tion.jda.api.EmbedBuilder;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static com.forgottenartsstudios.harmony.Main.Harmony.jda;

public class BotOwnerCommands extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {return;}
        String prefix = Harmony.getGuildPrefix(event);
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if (args[0].startsWith(prefix)) {
            String arg0 = args[0].replace(prefix, "");
            if(event.getAuthor().getIdLong() == Long.parseLong(Variables.prop.getProperty("botowner"))) {
                switch (arg0){
                    case "ping":
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setDescription(Long.toString(jda.getGatewayPing()));
                        embed.setColor(Color.CYAN);
                        MessageEmbed msg = embed.build();
                        event.getMessage().replyEmbeds(msg).queue();
                        break;
                    default:
                        //stuff
                }
            }
        }
    }
}
