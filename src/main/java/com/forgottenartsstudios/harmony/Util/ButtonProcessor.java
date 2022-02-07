package com.forgottenartsstudios.harmony.Util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import static com.forgottenartsstudios.harmony.Main.Harmony.out;

import java.awt.Color;

public class ButtonProcessor extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        switch (event.getComponentId()) {
            case "mute":
                out.info(event.getMessage().getContentRaw());
                mute(event);
                break;
            case "timeout":
                timeout(event);
                break;
            case "muteaction":
                //add user to mute list
                //remove all other roles
                break;
            case "unmuteaction":
                //remote user from mute
                //readd previous roles
                break;
            default:
                //stuff
        }
    }
    public static void mute(ButtonInteractionEvent event){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.CYAN);
        embed.setDescription("Mute or Unmute?");
        MessageEmbed msgEmbed = embed.build();
        event.editMessageEmbeds(msgEmbed).setActionRow(
                Button.primary("muteaction", "mute"), // Button with only a label
                Button.primary("unmuteaction", "unmute") // Button with only a label
        ).queue();
    }
    public static void timeout(ButtonInteractionEvent event){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.CYAN);
        embed.setDescription("Timeout or Timein?");
        MessageEmbed msgEmbed = embed.build();
        event.editMessageEmbeds(msgEmbed).setActionRow(
                Button.primary("timeoutaction", "timeout"), // Button with only a label
                Button.primary("timeinaction", "timein") // Button with only a label
        ).queue();
    }
}
