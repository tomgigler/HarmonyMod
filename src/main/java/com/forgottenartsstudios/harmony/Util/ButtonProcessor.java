package com.forgottenartsstudios.harmony.Util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import static com.forgottenartsstudios.harmony.Main.Harmony.out;

import com.forgottenartsstudios.harmony.Data.Moderation;
import com.forgottenartsstudios.harmony.Data.MutedMember;
import com.forgottenartsstudios.harmony.Main.Variables;

import java.awt.Color;
import java.util.List;

public class ButtonProcessor extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        Moderation moderation  = new Moderation();
        for(int x = 0; x < Variables.moderations.size(); x++){
            if(Variables.moderations.get(x).isMuteFlag());
            moderation = Variables.moderations.get(x);
        }
        Member member = moderation.getModPanelMember();
        InteractionHook hook = moderation.getHook();
        switch (event.getComponentId()) {
            case "mute":
                mute(event, hook, member);
                break;
            case "timeout":
                timeout(event);
                break;
            case "muteaction":
                muteAction(event, hook, member);
                break;
            case "unmuteaction":
                unMuteAction(event, hook, member);
                break;
            default:
                //stuff
        }
    }
    public static void mute(ButtonInteractionEvent event, InteractionHook hook, Member member){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.CYAN);
        embed.setDescription("Mute or Unmute " + member.getEffectiveName());
        MessageEmbed msgEmbed = embed.build();
        event.editMessageEmbeds(msgEmbed).setActionRow(
                Button.primary("muteaction", "mute"), // Button with only a label
                Button.primary("unmuteaction", "unmute") // Button with only a label
        ).queue();
    }
    public static void muteAction(ButtonInteractionEvent event, InteractionHook hook, Member member){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.CYAN);
        embed.setDescription("Successfully muted " + member.getEffectiveName());
        MessageEmbed msgEmbed = embed.build();
        hook.editOriginalEmbeds(msgEmbed).queue();
        event.replyEmbeds(basicMessageEmbed("Thanks you for using Harmony MOD.")).setEphemeral(true).queue();
        List<Role> roles = member.getRoles();
        for(Role r: roles){
            member.getGuild().removeRoleFromMember(member, r).queue();
        }
        List<Role> mutedRole = member.getGuild().getRolesByName("Muted", true);
        member.getGuild().addRoleToMember(member, mutedRole.get(0)).queue();
        MutedMember mutedMember = new MutedMember();
        mutedMember.setMember(member);
        mutedMember.setRoles(roles);
        Variables.mutedMembers.add(mutedMember);
        System.out.println(roles);
        //remove all other roles
    }
    public static void unMuteAction(ButtonInteractionEvent event, InteractionHook hook, Member member){
        MutedMember mutedMember = new MutedMember();
        for(int x = 0; x < Variables.mutedMembers.size(); x++){
            if(Variables.mutedMembers.get(x) == member);
            mutedMember = Variables.mutedMembers.get(x);
        }
        List<Role> roles = mutedMember.getRoles();
        for(Role r: roles){
            member.getGuild().addRoleToMember(member, r).queue();
        }
        List<Role> mutedRole = member.getGuild().getRolesByName("Muted", true);
        member.getGuild().removeRoleFromMember(member, mutedRole.get(0)).queue();
        Variables.mutedMembers.remove(mutedMember);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.CYAN);
        embed.setDescription("Successfully unmuted " + member.getEffectiveName());
        MessageEmbed msgEmbed = embed.build();
        hook.editOriginalEmbeds(msgEmbed).queue();
        event.replyEmbeds(basicMessageEmbed("Thanks you for using Harmony MOD.")).setEphemeral(true).queue();
        System.out.println(roles);
        //remove all other roles
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
    public static MessageEmbed basicMessageEmbed(String str){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription(str);
        embed.setColor(Color.CYAN);
        return embed.build();
    }
}
