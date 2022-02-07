package com.forgottenartsstudios.harmony.Util;

import com.forgottenartsstudios.harmony.Data.Moderation;
import com.forgottenartsstudios.harmony.Main.Variables;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import org.jetbrains.annotations.NotNull;
import static com.forgottenartsstudios.harmony.Main.Harmony.out;

import java.awt.Color;
import java.util.Objects;

public class GuildModSlashCommands extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "ping":
                long time = System.currentTimeMillis();
                event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                        .flatMap(v ->
                                event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                        ).queue(); // Queue both reply and edit
                break;
            case "prefix":
                for(int x = 0; x < Variables.guildData.size(); x++) {
                    if(Variables.guildData.get(x).getID() == Objects.requireNonNull(event.getGuild()).getIdLong()) {
                        System.out.println(Variables.guildData.get(x).getPrefix());
                        event.reply(Variables.guildData.get(x).getPrefix()).setEphemeral(true).queue();
                    }
                }
                break;
            case "mod":
                modControlPanel(event);
                break;
            case "test":
                modControlPanel(event);
                break;
            default:
                out.debug("Mod Slash Command not found.");
        }
    }
    public static void modControlPanel(SlashCommandInteractionEvent event){
        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.MODERATE_MEMBERS))
        {
            Moderation moderation = new Moderation();
            moderation.setName(event.getMember().getEffectiveName());
            moderation.setModPanelMember(Objects.requireNonNull(event.getOption("modmention")).getAsMember());
            out.debug("Summon mod menu.");
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.CYAN);
            Member member = moderation.getModPanelMember();
            assert member != null;
            embed.setDescription("Mod panel for " + member.getEffectiveName());
            MessageEmbed msgEmbed = embed.build();
            event.replyEmbeds(msgEmbed)
                    .addActionRow(
                            Button.primary("mute", "   Mute    "), // Button with only a label
                            Button.primary("timeout", "  Timeout  "), // Button with only a label
                            Button.primary("nickname", " Nickname  "), // Button with only a label
                            Button.primary("lookpup", "  Lookup   "), // Button with only a label
                            Button.primary("ban", "    Ban   ")// Button with only a label
                    )
                    .addActionRow(
                            Button.primary("kick", "   Kick    "), // Button with only a label
                            Button.primary("warn", "   Warn    "), // Button with only a label
                            Button.danger("slowmode", " Slowmode "), // Button with only a label
                            Button.success("event", "   Event   "), // Button with only a label
                            Button.primary("moderations", "Moderations")// Button with only a label
                    )
                    .queue();
        }
    }
}
