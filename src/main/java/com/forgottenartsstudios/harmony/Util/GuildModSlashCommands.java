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
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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
            default:
                out.debug("Mod Slash Command not found.");
        }
    }
    public static void modControlPanel(SlashCommandInteractionEvent event){
        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.MODERATE_MEMBERS))
        {
            out.debug("Mod Control Panel Hook: " + event.getHook());

            //event.deferReply().queue();
            Moderation moderation = new Moderation();
            moderation.setName(event.getMember().getEffectiveName());
            moderation.setModPanelMember(Objects.requireNonNull(event.getOption("modmention")).getAsMember());
            moderation.setHook(event.getHook());
            Variables.moderations.add(moderation);

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.CYAN);
            Member member = moderation.getModPanelMember();

            Instant created = member.getTimeCreated().toInstant();
            Instant join = member.getTimeJoined().toInstant();
            Instant now = Instant.now();
            Duration dCreated = Duration.between(created, now);
            Duration dJoin = Duration.between(join, now);
            String createdAge = format(dCreated);
            String joinAge = format(dJoin);

            embed.setAuthor(member.getEffectiveName());

            embed.addField("id", member.getId(), true);
            embed.addField("avatar", "[Link](" + member.getEffectiveAvatarUrl() + ")", true);
            embed.addField("account created", member.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME), true);
            embed.addField("account age", createdAge, true);
            embed.addField("joined server at", member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME), true);
            embed.addField("join server age", joinAge, true);
            embed.addField("status", member.getOnlineStatus().toString(), false);
            embed.addField("role", String.valueOf(member.getRoles()), false);
            embed.setFooter("id: " + event.getId());
            embed.setThumbnail(member.getEffectiveAvatarUrl());
            MessageEmbed msgEmbed = embed.build();
            event.replyEmbeds(msgEmbed)
                    .addActionRow(
                            Button.primary("mute", "mute"), // Button with only a label
                            Button.primary("timeout", "timeout"), // Button with only a label
                            Button.primary("nickname", "nickname"), // Button with only a label
                            Button.primary("lookpup", "lookup"), // Button with only a label
                            Button.primary("ban", "ban")// Button with only a label
                    ).addActionRow(
                            Button.primary("kick", "kick"), // Button with only a label
                            Button.primary("warn", "warn"), // Button with only a label
                            Button.primary("moderations", "moderations")// Button with only a label
                    ).queue();
        }
    }
    public static String format(Duration d) {
        long years = (d.toDays() / 365);
        d = d.minusDays(years * 365);
        long days = d.toDays();
        d = d.minusDays(days);
        long hours = d.toHours();
        d = d.minusHours(hours);
        long minutes = d.toMinutes();
        d = d.minusMinutes(minutes);
        long seconds = d.getSeconds();
        return
                (years == 0?"":years+" years, ")+
                        (days ==  0?"":days+" days, ")+
                        (hours == 0?"":hours+" hours, ")+
                        (minutes ==  0?"":minutes+" minutes, ")+
                        (seconds == 0?"":seconds+" seconds");
    }
}
