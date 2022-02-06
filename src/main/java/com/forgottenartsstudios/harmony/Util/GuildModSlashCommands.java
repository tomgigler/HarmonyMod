package com.forgottenartsstudios.harmony.Util;

import com.forgottenartsstudios.harmony.Main.Variables;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import static com.forgottenartsstudios.harmony.Main.Harmony.out;

public class GuildModSlashCommands extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "ping":
                long time = System.currentTimeMillis();
                for(int x = 0; x < Variables.guildData.size(); x++) {
                    if(Variables.guildData.get(x).getID() == event.getGuild().getIdLong()) {
                        out.info("Event: '" + event.getName() + "': " + Variables.guildData.get(x).getID());
                        int finalX = x;
                        event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                                .flatMap(v ->
                                        event.getHook().editOriginalFormat("Prefix: " + Variables.guildData.get(finalX).getPrefix() + "\nPong: %d ms", System.currentTimeMillis() - time) // then edit original
                                ).queue(); // Queue both reply and edit
                    }
                }

                break;
            case "prefix":
                for(int x = 0; x < Variables.guildData.size(); x++) {
                    if(Variables.guildData.get(x).getID() == event.getGuild().getIdLong()) {
                        System.out.println(Variables.guildData.get(x).getPrefix());
                        event.reply(Variables.guildData.get(x).getPrefix()).setEphemeral(true).queue();
                    }
                }
                break;
        }
    }
}
